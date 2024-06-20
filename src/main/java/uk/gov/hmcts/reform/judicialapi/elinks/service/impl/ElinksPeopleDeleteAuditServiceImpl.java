package uk.gov.hmcts.reform.judicialapi.elinks.service.impl;


import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.hmcts.reform.judicialapi.elinks.domain.Appointment;
import uk.gov.hmcts.reform.judicialapi.elinks.domain.Authorisation;
import uk.gov.hmcts.reform.judicialapi.elinks.domain.JudicialRoleType;
import uk.gov.hmcts.reform.judicialapi.elinks.domain.UserProfile;
import uk.gov.hmcts.reform.judicialapi.elinks.repository.audit.AppointmentsRepository;
import uk.gov.hmcts.reform.judicialapi.elinks.repository.audit.AuthorisationsRepository;
import uk.gov.hmcts.reform.judicialapi.elinks.repository.audit.JudicialRoleTypeRepository;
import uk.gov.hmcts.reform.judicialapi.elinks.repository.audit.ProfileRepository;
import uk.gov.hmcts.reform.judicialapi.elinks.service.ElinksPeopleDeleteAuditService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Setter
public class ElinksPeopleDeleteAuditServiceImpl implements ElinksPeopleDeleteAuditService {

    @Autowired
    private JudicialRoleTypeRepository judicialRoleTypeRepositoryAudit;

    @Autowired
    private AuthorisationsRepository authorisationsRepositoryAudit;

    @Autowired
    private AppointmentsRepository appointmentsRepositoryAudit;

    @Autowired
    private ProfileRepository profileRepositoryAudit;


    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void auditPeopleDelete(List<Authorisation> authorisations,
                                  List<Appointment> appointments,
                                  List<JudicialRoleType> judicialRoleTypes,
                                  List<UserProfile> userProfiles) {

        if (authorisations != null) {
            authorisationsRepositoryAudit.saveAll(authorisations.stream().map(authorisation ->
                            uk.gov.hmcts.reform.judicialapi.elinks.domain.audit.Authorisation.builder()
                                    .authorisationId(authorisation.getAuthorisationId())
                                    .appointmentId(authorisation.getAppointmentId())
                                    .createdDate(authorisation.getCreatedDate())
                                    .endDate(authorisation.getEndDate())
                                    .jurisdiction(authorisation.getJurisdiction())
                                    .jurisdictionId(authorisation.getJurisdictionId())
                                    .lastUpdated(authorisation.getLastUpdated())
                                    .lowerLevel(authorisation.getLowerLevel())
                                    .officeAuthId(authorisation.getOfficeAuthId())
                                    .personalCode(authorisation.getPersonalCode())
                                    .startDate(authorisation.getStartDate())
                                    .ticketCode(authorisation.getTicketCode())
                                    .build())
                    .collect(Collectors.toList()));
        }

        if (appointments != null) {
            appointmentsRepositoryAudit.saveAll(appointments.stream()
                    .map(appointment -> uk.gov.hmcts.reform.judicialapi.elinks.domain.audit.Appointment.builder()
                            .appointmentMapping(appointment.getAppointmentMapping())
                            .appointmentType(appointment.getAppointmentType())
                            .baseLocationId(appointment.getBaseLocationId())
                            .appointmentId(appointment.getAppointmentId())
                            .contractTypeId(appointment.getContractTypeId())
                            .createdDate(appointment.getCreatedDate())
                            .epimmsId(appointment.getEpimmsId())
                            .isPrincipleAppointment(appointment.getIsPrincipleAppointment())
                            .joBaseLocationId(appointment.getJoBaseLocationId())
                            .lastLoadedDate(appointment.getLastLoadedDate())
                            .personalCode(appointment.getPersonalCode())
                            .location(appointment.getLocation())
                            .officeAppointmentId(appointment.getOfficeAppointmentId())
                            .regionId(appointment.getRegionId())
                            .roleNameId(appointment.getRoleNameId())
                            .type(appointment.getType())
                            .endDate(appointment.getEndDate())
                            .startDate(appointment.getStartDate())
                            .build()).collect(Collectors.toList()));
        }

        if (judicialRoleTypes != null) {
            judicialRoleTypeRepositoryAudit.saveAll(judicialRoleTypes.stream()
                    .map(judicialRoleType -> uk.gov.hmcts.reform.judicialapi.elinks.domain.audit.JudicialRoleType
                            .builder().jurisdictionRoleId(judicialRoleType.getJurisdictionRoleId())
                            .personalCode(judicialRoleType.getPersonalCode())
                            .startDate(judicialRoleType.getStartDate())
                            .jurisdictionRoleNameId(judicialRoleType.getJurisdictionRoleNameId())
                            .roleId(judicialRoleType.getRoleId())
                            .title(judicialRoleType.getTitle())
                            .endDate(judicialRoleType.getEndDate())
                            .build()).collect(Collectors.toList()));
        }

        if (userProfiles != null) {
            profileRepositoryAudit.saveAll(userProfiles.stream()
                    .map(userProfile -> uk.gov.hmcts.reform.judicialapi.elinks.domain.audit.UserProfile.builder()
                            .activeFlag(userProfile.getActiveFlag())
                            .deletedFlag(userProfile.getDeletedFlag())
                            .deletedOn(userProfile.getDeletedOn())
                            .emailId(userProfile.getEmailId())
                            .createdDate(userProfile.getCreatedDate())
                            .lastLoadedDate(userProfile.getLastLoadedDate())
                            .fullName(userProfile.getFullName())
                            .initials(userProfile.getInitials())
                            .knownAs(userProfile.getKnownAs())
                            .lastWorkingDate(userProfile.getLastWorkingDate())
                            .objectId(userProfile.getObjectId())
                            .personalCode(userProfile.getPersonalCode())
                            .postNominals(userProfile.getPostNominals())
                            .retirementDate(userProfile.getRetirementDate())
                            .sidamId(userProfile.getSidamId())
                            .surname(userProfile.getSurname())
                            .title(userProfile.getTitle()).build()).collect(Collectors.toList()));
        }
    }
}
