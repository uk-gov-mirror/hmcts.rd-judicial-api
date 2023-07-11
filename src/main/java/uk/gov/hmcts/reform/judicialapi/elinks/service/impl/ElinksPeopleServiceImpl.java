package uk.gov.hmcts.reform.judicialapi.elinks.service.impl;

import feign.FeignException;
import feign.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.hmcts.reform.judicialapi.elinks.controller.request.AppointmentsRequest;
import uk.gov.hmcts.reform.judicialapi.elinks.controller.request.AuthorisationsRequest;
import uk.gov.hmcts.reform.judicialapi.elinks.controller.request.PeopleRequest;
import uk.gov.hmcts.reform.judicialapi.elinks.controller.request.ResultsRequest;
import uk.gov.hmcts.reform.judicialapi.elinks.controller.request.RoleRequest;
import uk.gov.hmcts.reform.judicialapi.elinks.domain.JudicialRoleType;
import uk.gov.hmcts.reform.judicialapi.elinks.domain.Location;
import uk.gov.hmcts.reform.judicialapi.elinks.domain.LocationMapping;
import uk.gov.hmcts.reform.judicialapi.elinks.domain.UserProfile;
import uk.gov.hmcts.reform.judicialapi.elinks.exception.ElinksException;
import uk.gov.hmcts.reform.judicialapi.elinks.feign.ElinksFeignClient;
import uk.gov.hmcts.reform.judicialapi.elinks.repository.AppointmentsRepository;
import uk.gov.hmcts.reform.judicialapi.elinks.repository.AuthorisationsRepository;
import uk.gov.hmcts.reform.judicialapi.elinks.repository.BaseLocationRepository;
import uk.gov.hmcts.reform.judicialapi.elinks.repository.DataloadSchedularAuditRepository;
import uk.gov.hmcts.reform.judicialapi.elinks.repository.JudicialRoleTypeRepository;
import uk.gov.hmcts.reform.judicialapi.elinks.repository.LocationMapppingRepository;
import uk.gov.hmcts.reform.judicialapi.elinks.repository.LocationRepository;
import uk.gov.hmcts.reform.judicialapi.elinks.repository.ProfileRepository;
import uk.gov.hmcts.reform.judicialapi.elinks.response.ElinkPeopleWrapperResponse;
import uk.gov.hmcts.reform.judicialapi.elinks.service.ElinksPeopleService;
import uk.gov.hmcts.reform.judicialapi.elinks.util.CommonUtil;
import uk.gov.hmcts.reform.judicialapi.elinks.util.ElinkDataExceptionHelper;
import uk.gov.hmcts.reform.judicialapi.elinks.util.ElinkDataIngestionSchedularAudit;
import uk.gov.hmcts.reform.judicialapi.elinks.util.RefDataElinksConstants;
import uk.gov.hmcts.reform.judicialapi.util.JsonFeignResponseUtil;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.time.LocalDateTime.now;
import static java.util.Objects.nonNull;
import static uk.gov.hmcts.reform.judicialapi.elinks.util.RefDataElinksConstants.APPOINTMENT_TABLE;
import static uk.gov.hmcts.reform.judicialapi.elinks.util.RefDataElinksConstants.AUTHORISATION_TABLE;
import static uk.gov.hmcts.reform.judicialapi.elinks.util.RefDataElinksConstants.BASE_LOCATION_ID;
import static uk.gov.hmcts.reform.judicialapi.elinks.util.RefDataElinksConstants.CFTREGIONIDFAILURE;
import static uk.gov.hmcts.reform.judicialapi.elinks.util.RefDataElinksConstants.DATA_UPDATE_ERROR;
import static uk.gov.hmcts.reform.judicialapi.elinks.util.RefDataElinksConstants.ELINKS_ACCESS_ERROR;
import static uk.gov.hmcts.reform.judicialapi.elinks.util.RefDataElinksConstants.ELINKS_ERROR_RESPONSE_BAD_REQUEST;
import static uk.gov.hmcts.reform.judicialapi.elinks.util.RefDataElinksConstants.ELINKS_ERROR_RESPONSE_FORBIDDEN;
import static uk.gov.hmcts.reform.judicialapi.elinks.util.RefDataElinksConstants.ELINKS_ERROR_RESPONSE_NOT_FOUND;
import static uk.gov.hmcts.reform.judicialapi.elinks.util.RefDataElinksConstants.ELINKS_ERROR_RESPONSE_TOO_MANY_REQUESTS;
import static uk.gov.hmcts.reform.judicialapi.elinks.util.RefDataElinksConstants.ELINKS_ERROR_RESPONSE_UNAUTHORIZED;
import static uk.gov.hmcts.reform.judicialapi.elinks.util.RefDataElinksConstants.JUDICIALROLETYPE;
import static uk.gov.hmcts.reform.judicialapi.elinks.util.RefDataElinksConstants.JUDICIAL_REF_DATA_ELINKS;
import static uk.gov.hmcts.reform.judicialapi.elinks.util.RefDataElinksConstants.LOCATION;
import static uk.gov.hmcts.reform.judicialapi.elinks.util.RefDataElinksConstants.LOCATIONIDFAILURE;
import static uk.gov.hmcts.reform.judicialapi.elinks.util.RefDataElinksConstants.PEOPLEAPI;
import static uk.gov.hmcts.reform.judicialapi.elinks.util.RefDataElinksConstants.PEOPLE_DATA_LOAD_SUCCESS;
import static uk.gov.hmcts.reform.judicialapi.elinks.util.RefDataElinksConstants.REGION_DEFAULT_ID;
import static uk.gov.hmcts.reform.judicialapi.elinks.util.RefDataElinksConstants.SPTW;
import static uk.gov.hmcts.reform.judicialapi.elinks.util.RefDataElinksConstants.THREAD_INVOCATION_EXCEPTION;
import static uk.gov.hmcts.reform.judicialapi.elinks.util.RefDataElinksConstants.USER_PROFILE;

@Slf4j
@Service
public class ElinksPeopleServiceImpl implements ElinksPeopleService {

    @Autowired
    private ElinksFeignClient elinksFeignClient;

    @Autowired
    private BaseLocationRepository baseLocationRepository;

    @Autowired
    ElinkDataExceptionHelper elinkDataExceptionHelper;

    @Autowired
    private AppointmentsRepository appointmentsRepository;

    @Autowired
    private AuthorisationsRepository authorisationsRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private JudicialRoleTypeRepository judicialRoleTypeRepository;

    @Autowired
    private LocationMapppingRepository locationMappingRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private DataloadSchedularAuditRepository dataloadSchedularAuditRepository;

    @Autowired
    ElinkDataIngestionSchedularAudit elinkDataIngestionSchedularAudit;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    CommonUtil commonUtil;

    private boolean baseLocationUnavailableFlag = false;

    @Value("${elinks.people.lastUpdated}")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String lastUpdated;

    @Value("${elinks.people.perPage}")
    private String perPage;

    @Value("${elinks.people.threadPauseTime}")
    private String threadPauseTime;

    @Value("${elinks.people.page}")
    private String page;

    @Value("${elinks.people.includePreviousAppointments}")
    private String includePreviousAppointments;

    @Override
    @Transactional("transactionManager")
    public ResponseEntity<ElinkPeopleWrapperResponse> updatePeople() {
        boolean isMorePagesAvailable = true;
        HttpStatus httpStatus = null;
        LocalDateTime schedulerStartTime = now();
        String status = RefDataElinksConstants.JobStatus.SUCCESS.getStatus();

        elinkDataIngestionSchedularAudit.auditSchedulerStatus(JUDICIAL_REF_DATA_ELINKS,
                schedulerStartTime,
                null,
                RefDataElinksConstants.JobStatus.IN_PROGRESS.getStatus(), PEOPLEAPI);

        int pageValue = Integer.parseInt(page);
        do {
            Response peopleApiResponse = getPeopleResponseFromElinks(pageValue++, schedulerStartTime);
            httpStatus = HttpStatus.valueOf(peopleApiResponse.status());
            ResponseEntity<Object> responseEntity;

            if (httpStatus.is2xxSuccessful()) {
                responseEntity = JsonFeignResponseUtil.toResponseEntity(peopleApiResponse, PeopleRequest.class);
                PeopleRequest elinkPeopleResponseRequest = (PeopleRequest) responseEntity.getBody();
                if (Optional.ofNullable(elinkPeopleResponseRequest).isPresent()
                        && Optional.ofNullable(elinkPeopleResponseRequest.getPagination()).isPresent()
                        && Optional.ofNullable(elinkPeopleResponseRequest.getResultsRequests()).isPresent()) {
                    isMorePagesAvailable = elinkPeopleResponseRequest.getPagination().getMorePages();
                    processPeopleResponse(elinkPeopleResponseRequest, schedulerStartTime);
                } else {
                    auditStatus(schedulerStartTime, RefDataElinksConstants.JobStatus.FAILED.getStatus());
                    throw new ElinksException(HttpStatus.FORBIDDEN, ELINKS_ACCESS_ERROR, ELINKS_ACCESS_ERROR);
                }
            } else {
                auditStatus(schedulerStartTime, RefDataElinksConstants.JobStatus.FAILED.getStatus());
                handleELinksErrorResponse(httpStatus);
            }
            pauseThread(Long.valueOf(threadPauseTime),schedulerStartTime);
        } while (isMorePagesAvailable);


        if (baseLocationUnavailableFlag) {
            status = RefDataElinksConstants.JobStatus.PARTIAL_SUCCESS.getStatus();
        }

        updateEpimsServiceCodeMapping(schedulerStartTime);
        auditStatus(schedulerStartTime, status);
        ElinkPeopleWrapperResponse response = new ElinkPeopleWrapperResponse();
        response.setMessage(PEOPLE_DATA_LOAD_SUCCESS);

        return ResponseEntity
                .status(httpStatus)
                .body(response);
    }

    private void auditStatus(LocalDateTime schedulerStartTime, String status) {
        elinkDataIngestionSchedularAudit.auditSchedulerStatus(JUDICIAL_REF_DATA_ELINKS,
                schedulerStartTime,
                now(),status,PEOPLEAPI);
    }

    private Response getPeopleResponseFromElinks(int currentPage, LocalDateTime schedulerStartTime) {
        String updatedSince = getUpdateSince();
        try {
            return elinksFeignClient.getPeopleDetials(updatedSince, perPage, String.valueOf(currentPage),
                    Boolean.parseBoolean(includePreviousAppointments));
        } catch (FeignException ex) {
            auditStatus(schedulerStartTime, RefDataElinksConstants.JobStatus.FAILED.getStatus());
            throw new ElinksException(HttpStatus.FORBIDDEN, ELINKS_ACCESS_ERROR, ELINKS_ACCESS_ERROR);
        }
    }

    private void pauseThread(Long thredPauseTime, LocalDateTime schedulerStartTime) {
        try {
            Thread.sleep(thredPauseTime);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            auditStatus(schedulerStartTime, RefDataElinksConstants.JobStatus.FAILED.getStatus());
            throw new ElinksException(HttpStatus.NOT_ACCEPTABLE, THREAD_INVOCATION_EXCEPTION,
                    THREAD_INVOCATION_EXCEPTION);
        }
    }

    private String getUpdateSince() {
        String updatedSince;
        LocalDateTime maxSchedulerEndTime;
        try {
            maxSchedulerEndTime = dataloadSchedularAuditRepository.findLatestSchedularEndTime();
        } catch (Exception ex) {
            throw new ElinksException(HttpStatus.NOT_ACCEPTABLE, DATA_UPDATE_ERROR, DATA_UPDATE_ERROR);
        }
        if (Optional.ofNullable(maxSchedulerEndTime).isEmpty()) {
            updatedSince = commonUtil.getUpdatedDateFormat(lastUpdated);
        } else {
            updatedSince = maxSchedulerEndTime.toString();
            updatedSince = updatedSince.substring(0, updatedSince.indexOf('T'));
        }
        log.info("updatedSince : " + updatedSince);
        return updatedSince;
    }


    private void processPeopleResponse(PeopleRequest elinkPeopleResponseRequest, LocalDateTime schedulerStartTime) {
        try {
            // filter the profiles that do have email address for leavers
            List<ResultsRequest> resultsRequests = elinkPeopleResponseRequest.getResultsRequests()
                    .stream()
                    .filter(resultsRequest -> nonNull(resultsRequest.getEmail()))
                    .toList();

            resultsRequests.forEach(this::savePeopleDetails);

        } catch (Exception ex) {
            auditStatus(schedulerStartTime, RefDataElinksConstants.JobStatus.FAILED.getStatus());
            throw new ElinksException(HttpStatus.NOT_ACCEPTABLE, DATA_UPDATE_ERROR, DATA_UPDATE_ERROR);
        }

    }

    private void savePeopleDetails(
        ResultsRequest resultsRequest) {

        saveUserProfile(resultsRequest);
        saveAppointmentDetails(resultsRequest.getPersonalCode(),resultsRequest
            .getObjectId(),resultsRequest.getAppointmentsRequests());
        saveAuthorizationDetails(resultsRequest.getPersonalCode(),resultsRequest
            .getObjectId(),resultsRequest.getAuthorisationsRequests());
        saveRoleDetails(resultsRequest.getPersonalCode(),resultsRequest.getJudiciaryRoles());
    }

    private void saveRoleDetails(String personalCode, List<RoleRequest> judiciaryRoles) {

        for (RoleRequest roleRequest: judiciaryRoles) {
            JudicialRoleType judicialRoleType = JudicialRoleType.builder()
                .title(roleRequest.getName())
                .startDate(convertToLocalDateTime(roleRequest.getStartDate()))
                .endDate(convertToLocalDateTime(roleRequest.getEndDate()))
                .personalCode(personalCode)
                .jurisdictionRoleId(roleRequest.getJudiciaryRoleId())
                .build();

            try {
                judicialRoleTypeRepository.save(judicialRoleType);
            } catch (Exception e) {
                log.warn("Role type  not loaded for " + personalCode);
                baseLocationUnavailableFlag = true;
                elinkDataExceptionHelper.auditException(JUDICIAL_REF_DATA_ELINKS,
                    now(),
                    personalCode,
                    JUDICIALROLETYPE, e.getMessage(), JUDICIALROLETYPE,personalCode);
            }
        }

    }


    private void saveUserProfile(ResultsRequest resultsRequest) {
        UserProfile userProfile = UserProfile.builder()
            .personalCode(resultsRequest.getPersonalCode())
            .knownAs(resultsRequest.getKnownAs())
            .surname(resultsRequest.getSurname())
            .fullName(resultsRequest.getFullName())
            .postNominals(resultsRequest.getPostNominals())
            .ejudiciaryEmailId(resultsRequest.getEmail())
            .lastWorkingDate(convertToLocalDate(resultsRequest.getLastWorkingDate()))
            .activeFlag(true)
            .createdDate(now())
            .lastLoadedDate(now())
            .objectId(resultsRequest.getObjectId())
            .initials(resultsRequest.getInitials())
            .title(resultsRequest.getTitle())
            .retirementDate(convertToLocalDate(resultsRequest.getRetirementDate()))
            .build();

        try {
            profileRepository.save(userProfile);
            appointmentsRepository.deleteByPersonalCodeIn(List.of(userProfile.getPersonalCode()));
        } catch (Exception e) {
            log.warn("User Profile not loaded for " + resultsRequest.getPersonalCode());
            baseLocationUnavailableFlag = true;
            String personalCode = resultsRequest.getPersonalCode();
            elinkDataExceptionHelper.auditException(JUDICIAL_REF_DATA_ELINKS,
                now(),
                resultsRequest.getPersonalCode(),
                LOCATION, e.getMessage(), USER_PROFILE,personalCode);
        }
    }


    private void saveAppointmentDetails(String personalCode, String objectId,
                                        List<AppointmentsRequest> appointmentsRequests) {

        final List<AppointmentsRequest> validappointmentsRequests =
            validateAppointmentRequest(appointmentsRequests,personalCode);

        for (AppointmentsRequest appointmentsRequest: validappointmentsRequests) {
            log.info("Retrieving appointment.getBaseLocationId() from DB " + appointmentsRequest.getBaseLocationId());
            // Check for base location in static table

            String baseLocationId = fetchBaseLocationId(appointmentsRequest);
            try {
                appointmentsRepository
                    .save(uk.gov.hmcts.reform.judicialapi.elinks.domain.Appointment.builder()
                        .baseLocationId(baseLocationId)
                        .regionId(fetchRegionId(appointmentsRequest.getLocation()))
                        .isPrincipleAppointment(appointmentsRequest.getIsPrincipleAppointment())
                        .startDate(convertToLocalDate(appointmentsRequest.getStartDate()))
                        .endDate(convertToLocalDate(appointmentsRequest.getEndDate()))
                        .personalCode(personalCode)
                        .epimmsId(locationMappingRepository.fetchEpimmsIdfromLocationId(baseLocationId))
                        .serviceCode(locationMappingRepository.fetchServiceCodefromLocationId(baseLocationId))
                        .objectId(objectId)
                        .appointment(appointmentsRequest.getRoleName())
                        .appointmentType(appointmentsRequest.getContractType()
                            .contains(SPTW) ? "SPTW" : appointmentsRequest
                            .getContractType())
                        .createdDate(now())
                        .lastLoadedDate(now())
                        .appointmentId(appointmentsRequest.getAppointmentId())
                        .roleNameId(appointmentsRequest.getRoleNameId())
                        .type(appointmentsRequest.getType())
                        .contractTypeId(appointmentsRequest.getContractType().contains(SPTW) ? "5" : appointmentsRequest
                            .getContractTypeId())
                        .location(appointmentsRequest.getLocation())
                        .joBaseLocationId(appointmentsRequest.getBaseLocationId())
                        .build());
                authorisationsRepository.deleteByPersonalCodeIn(List.of(personalCode));
            } catch (Exception e) {
                log.warn("failed to load appointment details for " + appointmentsRequest.getAppointmentId());
                baseLocationUnavailableFlag = true;
                elinkDataExceptionHelper.auditException(JUDICIAL_REF_DATA_ELINKS,
                    now(),
                    appointmentsRequest.getAppointmentId(),
                    APPOINTMENT_TABLE, e.getMessage(), APPOINTMENT_TABLE,personalCode);
            }
        }
    }

    private void saveAuthorizationDetails(String personalCode, String objectId,
                                          List<AuthorisationsRequest> authorisationsRequests) {

        for (AuthorisationsRequest authorisationsRequest : authorisationsRequests) {

            try {
                authorisationsRepository
                    .save(uk.gov.hmcts.reform.judicialapi.elinks.domain.Authorisation.builder()
                        .jurisdiction(authorisationsRequest.getJurisdiction())
                        .startDate(convertToLocalDateTime(authorisationsRequest.getStartDate()))
                        .endDate(convertToLocalDateTime(authorisationsRequest.getEndDate()))
                        .createdDate(LocalDateTime.now())
                        .lastUpdated(LocalDateTime.now())
                        .lowerLevel(authorisationsRequest.getTicket())
                        .personalCode(personalCode)
                        .objectId(objectId)
                        .ticketCode(authorisationsRequest.getTicketCode())
                        .ticketCode(authorisationsRequest.getTicketCode())
                        .appointmentId(authorisationsRequest.getAppointmentId())
                        .authorisationId(authorisationsRequest.getAuthorisationId())
                        .jurisdictionId(authorisationsRequest.getJurisdictionId())
                        .build());
                judicialRoleTypeRepository.deleteByPersonalCodeIn(List.of(personalCode));
            } catch (Exception e) {
                log.warn("failed to load Authorisation details for " + authorisationsRequest.getAuthorisationId());
                baseLocationUnavailableFlag = true;
                elinkDataExceptionHelper.auditException(JUDICIAL_REF_DATA_ELINKS,
                    now(),
                    authorisationsRequest.getAuthorisationId(),
                    AUTHORISATION_TABLE, e.getMessage(), AUTHORISATION_TABLE,personalCode);
            }
        }
    }

    private List<AppointmentsRequest> validateAppointmentRequest(List<AppointmentsRequest> appointmentsRequests,
                                                                 String personalCode) {

        return appointmentsRequests.stream().filter(appointmentsRequest ->
            validAppointments(appointmentsRequest,personalCode)).toList();
    }

    private boolean validAppointments(AppointmentsRequest appointmentsRequest, String personalCode) {

        if (StringUtils.isEmpty(appointmentsRequest.getBaseLocationId())
            || baseLocationRepository.findById(appointmentsRequest.getBaseLocationId()).isEmpty()
            || StringUtils.isEmpty(fetchBaseLocationId(appointmentsRequest))) {
            log.warn("Mapped Base location not found in base table " + appointmentsRequest.getBaseLocationId());
            baseLocationUnavailableFlag = true;
            String baseLocationId = appointmentsRequest.getBaseLocationId();
            String errorDescription = appendBaseLocationIdInErroDescription(LOCATIONIDFAILURE, baseLocationId);
            elinkDataExceptionHelper.auditException(JUDICIAL_REF_DATA_ELINKS,
                now(),
                appointmentsRequest.getAppointmentId(),
                BASE_LOCATION_ID, errorDescription, APPOINTMENT_TABLE,personalCode);
            return false;
        } else if (StringUtils.isEmpty(fetchRegionId(appointmentsRequest.getLocation()))) {
            log.warn("Mapped  location not found in region table " + appointmentsRequest.getBaseLocationId());
            baseLocationUnavailableFlag = true;
            String location = appointmentsRequest.getLocation();
            String errorDescription = appendBaseLocationIdInErroDescription(CFTREGIONIDFAILURE, location);
            elinkDataExceptionHelper.auditException(JUDICIAL_REF_DATA_ELINKS,
                now(),
                appointmentsRequest.getAppointmentId(),
                LOCATION, errorDescription, APPOINTMENT_TABLE,personalCode);
            return false;
        }
        return true;
    }

    private String fetchBaseLocationId(AppointmentsRequest appointment) {

        String baseLocationId = null;
        if ("Tribunals".equals(appointment.getType())) {
            baseLocationId = baseLocationRepository.fetchParentId(appointment.getBaseLocationId());
        } else if (!StringUtils.isEmpty(appointment.getType())) {
            baseLocationId = appointment.getBaseLocationId();
        }
        return baseLocationId;

    }

    private String fetchRegionId(String location) {

        String regionId = null;
        if ("Unassigned".equals(location) || StringUtils.isEmpty(location)) {
            regionId = "0";
        } else {
            regionId = locationRepository.fetchRegionIdfromCftRegionDescEn(location);
        }
        return regionId;

    }

    // Append the string to add error description for the given format
    private String appendBaseLocationIdInErroDescription(String errorDescription, String wordToAppend) {

        String wordAfterWhichAppend = ":";
        String errorDescriptionInGivenFormat = errorDescription.substring(0,
                errorDescription.indexOf(wordAfterWhichAppend)
                + wordAfterWhichAppend.length())
                + " " + wordToAppend + " "
                + errorDescription.substring(errorDescription.indexOf(wordAfterWhichAppend)
                + wordAfterWhichAppend.length(), errorDescription.length());
        return errorDescriptionInGivenFormat;
    }

    // moved the logic to outside
    public void updateEpimsServiceCodeMapping(LocalDateTime schedulerStartTime) {
        try {
            List<Triple<String, String, String>> epimsLocationId = new ArrayList<>();
            List<String> locationIds = appointmentsRepository.fetchAppointmentBaseLocation();
            List<LocationMapping> locationMappings = locationMappingRepository.findAllById(locationIds);

            String updateEpimmsid = "UPDATE dbjudicialdata.judicial_office_appointment SET epimms_id = ? , "
                    + " service_code = ? WHERE base_location_id = ?";
            locationMappings.stream().filter(location -> nonNull(location.getJudicialBaseLocationId())).forEach(s ->
                    epimsLocationId.add(Triple.of(s.getJudicialBaseLocationId(), s.getEpimmsId(), s.getServiceCode())));
            log.info("Insert Query batch Response from IDAM" + epimsLocationId.size());
            jdbcTemplate.batchUpdate(
                    updateEpimmsid,
                    epimsLocationId,
                    10,
                    new ParameterizedPreparedStatementSetter<Triple<String, String, String>>() {
                        public void setValues(PreparedStatement ps, Triple<String, String, String> argument)
                                throws SQLException {
                            ps.setString(1, argument.getMiddle());
                            ps.setString(2, argument.getRight());
                            ps.setString(3, argument.getLeft());
                        }
                    });
        } catch (Exception ex) {
            auditStatus(schedulerStartTime, RefDataElinksConstants.JobStatus.FAILED.getStatus());
            throw new ElinksException(HttpStatus.NOT_ACCEPTABLE, DATA_UPDATE_ERROR, DATA_UPDATE_ERROR);
        }
    }



    private LocalDate convertToLocalDate(String date) {
        if (Optional.ofNullable(date).isPresent()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return LocalDate.parse(date, formatter);
        }
        return null;
    }

    private LocalDateTime convertToLocalDateTime(String date) {
        if (Optional.ofNullable(date).isPresent()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return LocalDate.parse(date, formatter).atStartOfDay();
        }
        return null;
    }

    // Need to be removed after Region ID  CR fix
    private String regionMapping(AppointmentsRequest appointment) {

        String region = appointment.getCircuit() != null ? appointment.getCircuit() : appointment.getLocation();
        Location location = locationRepository.findByRegionDescEnIgnoreCase(region);

        return location != null ? location.getRegionId() : REGION_DEFAULT_ID;
    }

    private void handleELinksErrorResponse(HttpStatus httpStatus) {

        int value = httpStatus.value();

        if (HttpStatus.BAD_REQUEST.value() == value) {
            throw new ElinksException(httpStatus, ELINKS_ERROR_RESPONSE_BAD_REQUEST,
                    ELINKS_ERROR_RESPONSE_BAD_REQUEST);
        } else if (HttpStatus.UNAUTHORIZED.value() == value) {
            throw new ElinksException(httpStatus, ELINKS_ERROR_RESPONSE_UNAUTHORIZED,
                    ELINKS_ERROR_RESPONSE_UNAUTHORIZED);
        } else if (HttpStatus.FORBIDDEN.value() == value) {
            throw new ElinksException(httpStatus, ELINKS_ERROR_RESPONSE_FORBIDDEN,
                    ELINKS_ERROR_RESPONSE_FORBIDDEN);
        } else if (HttpStatus.NOT_FOUND.value() == value) {
            throw new ElinksException(httpStatus, ELINKS_ERROR_RESPONSE_NOT_FOUND,
                    ELINKS_ERROR_RESPONSE_NOT_FOUND);
        } else if (HttpStatus.TOO_MANY_REQUESTS.value() == value) {
            throw new ElinksException(httpStatus, ELINKS_ERROR_RESPONSE_TOO_MANY_REQUESTS,
                    ELINKS_ERROR_RESPONSE_TOO_MANY_REQUESTS);
        } else {
            throw new ElinksException(HttpStatus.FORBIDDEN, ELINKS_ACCESS_ERROR, ELINKS_ACCESS_ERROR);
        }
    }
}
