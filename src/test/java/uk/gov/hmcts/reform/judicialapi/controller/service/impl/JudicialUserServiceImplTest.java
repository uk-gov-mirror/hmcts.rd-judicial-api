package uk.gov.hmcts.reform.judicialapi.controller.service.impl;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Request;
import feign.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.reform.judicialapi.controller.advice.ErrorResponse;
import uk.gov.hmcts.reform.judicialapi.controller.advice.InvalidRequestException;
import uk.gov.hmcts.reform.judicialapi.controller.advice.ResourceNotFoundException;
import uk.gov.hmcts.reform.judicialapi.controller.advice.UserProfileException;
import uk.gov.hmcts.reform.judicialapi.controller.request.RefreshRoleRequest;
import uk.gov.hmcts.reform.judicialapi.controller.request.UserSearchRequest;
import uk.gov.hmcts.reform.judicialapi.controller.response.LrdOrgInfoServiceResponse;
import uk.gov.hmcts.reform.judicialapi.controller.response.UserProfileRefreshResponse;
import uk.gov.hmcts.reform.judicialapi.domain.RegionMapping;
import uk.gov.hmcts.reform.judicialapi.domain.ServiceCodeMapping;
import uk.gov.hmcts.reform.judicialapi.elinks.domain.Appointment;
import uk.gov.hmcts.reform.judicialapi.elinks.domain.Authorisation;
import uk.gov.hmcts.reform.judicialapi.elinks.domain.BaseLocation;
import uk.gov.hmcts.reform.judicialapi.elinks.domain.Location;
import uk.gov.hmcts.reform.judicialapi.elinks.domain.UserProfile;
import uk.gov.hmcts.reform.judicialapi.feign.LocationReferenceDataFeignClient;
import uk.gov.hmcts.reform.judicialapi.repository.RegionMappingRepository;
import uk.gov.hmcts.reform.judicialapi.repository.ServiceCodeMappingRepository;
import uk.gov.hmcts.reform.judicialapi.repository.UserProfileRepository;
import uk.gov.hmcts.reform.judicialapi.service.impl.JudicialUserServiceImpl;
import uk.gov.hmcts.reform.judicialapi.util.RequestUtils;
import uk.gov.hmcts.reform.judicialapi.validator.RefreshUserValidator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.validation.constraints.NotNull;

import static java.nio.charset.Charset.defaultCharset;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.judicialapi.controller.TestSupport.createUserProfile;
import static uk.gov.hmcts.reform.judicialapi.util.RefDataUtil.createPageableObject;

@ExtendWith(MockitoExtension.class)
class JudicialUserServiceImplTest {

    @InjectMocks
    JudicialUserServiceImpl judicialUserService;

    @Mock
    UserProfileRepository userProfileRepository;

    @Mock
    ServiceCodeMappingRepository serviceCodeMappingRepository;

    @Mock
    RegionMappingRepository regionMappingRepository;

    @Mock
    private LocationReferenceDataFeignClient locationReferenceDataFeignClient;

    private RefreshUserValidator refreshUserValidatorMock;
    ObjectMapper mapper = new ObjectMapper();

    private List<String> searchServiceCode;

    @BeforeEach
    void setUp() {
        refreshUserValidatorMock = new RefreshUserValidator();
        judicialUserService.setRefreshUserValidator(refreshUserValidatorMock);
        searchServiceCode = (List.of("bfa1","bba3"));
        judicialUserService.setSearchServiceCode(searchServiceCode);
    }

    @Test
    void shouldFetchJudicialUsers() {
        List<String> sidamIds = new ArrayList<>();
        sidamIds.add("sidamId1");
        sidamIds.add("sidamId2");
        List<UserProfile> userProfiles = new ArrayList<>();
        UserProfile user = createUserProfile();
        userProfiles.add(user);
        Pageable pageable = createPageableObject(0, 10, 10);
        PageImpl<UserProfile> page = new PageImpl<>(userProfiles);

        when(userProfileRepository.findBySidamIdIn(sidamIds,pageable)).thenReturn(page);

        ResponseEntity<Object> responseEntity =
                judicialUserService.fetchJudicialUsers(10,0, sidamIds);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(userProfileRepository, times(1)).findBySidamIdIn(any(),any());
    }

    @Test
    void shouldFetchJudicialUsersFailure() {
        List<String> sidamIds = new ArrayList<>();
        sidamIds.add("sidamId1");
        sidamIds.add("sidamId2");
        List<UserProfile> userProfiles = Collections.emptyList();
        Pageable pageable = createPageableObject(0, 10, 10);
        PageImpl<UserProfile> page = new PageImpl<>(userProfiles);

        when(userProfileRepository.findBySidamIdIn(sidamIds,pageable)).thenReturn(page);
        assertThrows(ResourceNotFoundException.class,
            () -> judicialUserService.fetchJudicialUsers(10,0, sidamIds));
    }

    @Test
    void shouldReturn200WhenUserFoundForTheSearchRequestProvided() {
        var userSearchRequest = UserSearchRequest
                .builder()
                .serviceCode("BFA1")
                .location("12456")
                .searchString("Test")
                .build();
        var userProfile = createUserProfile();
        var userProfile1 = createUserProfile();
        userProfile1.setActiveFlag(false);
        var serviceCodeMapping = ServiceCodeMapping
                .builder()
                .ticketCode("testTicketCode")
                .build();

        when(serviceCodeMappingRepository.findByServiceCodeIgnoreCase(userSearchRequest.getServiceCode()))
                .thenReturn(List.of(serviceCodeMapping));
        when(userProfileRepository.findBySearchString(userSearchRequest.getSearchString().toLowerCase(),
                userSearchRequest.getServiceCode(),userSearchRequest.getLocation(),List.of("testTicketCode"),
                searchServiceCode))
                .thenReturn(List.of(userProfile,userProfile1));

        var responseEntity =
                judicialUserService.retrieveUserProfile(userSearchRequest);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(userProfileRepository, times(1)).findBySearchString(any(),any(),
                any(), anyList(),anyList());
    }

    @Test
    void shouldReturn200WhenUserFoundForSscsSearchRequestProvided() {
        var userSearchRequest = UserSearchRequest
                .builder()
                .serviceCode("BBA3")
                .location("12456")
                .searchString("Test")
                .build();
        var userProfile = createUserProfile();
        var userProfile1 = createUserProfile();
        userProfile1.setActiveFlag(false);
        var serviceCodeMapping = ServiceCodeMapping
                .builder()
                .ticketCode("testTicketCode")
                .build();

        when(serviceCodeMappingRepository.findByServiceCodeIgnoreCase(userSearchRequest.getServiceCode()))
                .thenReturn(List.of(serviceCodeMapping));
        when(userProfileRepository.findBySearchString(userSearchRequest.getSearchString().toLowerCase(),
                userSearchRequest.getServiceCode(),userSearchRequest.getLocation(),List.of("testTicketCode"),
                searchServiceCode))
                .thenReturn(List.of(userProfile,userProfile1));

        var responseEntity =
                judicialUserService.retrieveUserProfile(userSearchRequest);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(userProfileRepository, times(1)).findBySearchString(any(),any(),
                any(), anyList(),anyList());
    }


    @Test
    void shouldReturn200WithEmptyResponseWhenUserNotFoundForTheSearchRequestProvided() {

        var userSearchRequest = UserSearchRequest
                .builder()
                .location("12456")
                .searchString("Test")
                .build();

        when(userProfileRepository.findBySearchString(any(), any(), any(), any(),any()))
                .thenReturn(Collections.emptyList());

        var responseEntity =
                judicialUserService.retrieveUserProfile(userSearchRequest);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Collections.EMPTY_LIST, responseEntity.getBody());
        verify(userProfileRepository, times(1)).findBySearchString(any(),any(),
                any(), anyList(),anyList());
    }

    @Test
    void test_refreshUserProfile_Two_Input_01() throws JsonProcessingException {

        var refreshRoleRequest = new RefreshRoleRequest("cmc",
                null, Arrays.asList("test", "test"),null);
        Assertions.assertThrows(InvalidRequestException.class, () -> {
            judicialUserService.refreshUserProfile(refreshRoleRequest, 1,
                    0, "ASC", "objectId");
        });

    }

    @Test
    void test_refreshUserProfile_Two_Input_02() throws JsonProcessingException {

        var refreshRoleRequest = new RefreshRoleRequest("cmc",
                Arrays.asList("test", "test"), null,null);
        Assertions.assertThrows(InvalidRequestException.class, () -> {
            judicialUserService.refreshUserProfile(refreshRoleRequest, 1,
                    0, "ASC", "objectId");
        });
    }

    @Test
    void test_refreshUserProfile_Two_Input_03() throws JsonProcessingException {

        var refreshRoleRequest = new RefreshRoleRequest("",
                Arrays.asList("test", "test"), Arrays.asList("test", "test"),null);
        Assertions.assertThrows(InvalidRequestException.class, () -> {
            judicialUserService.refreshUserProfile(refreshRoleRequest, 1,
                    0, "ASC", "objectId");
        });
    }

    @Test
    void test_refreshUserProfile_Two_Input_04() throws JsonProcessingException {

        var refreshRoleRequest = new RefreshRoleRequest("",
                Arrays.asList("test", "test"), null,Arrays.asList("test", "test"));
        Assertions.assertThrows(InvalidRequestException.class, () -> {
            judicialUserService.refreshUserProfile(refreshRoleRequest, 1,
                    0, "ASC", "objectId");
        });
    }

    @Test
    void test_refreshUserProfile_Multiple_Input() throws JsonProcessingException {

        var refreshRoleRequest = new RefreshRoleRequest("cmc",
                Arrays.asList("test", "test"), Arrays.asList("test", "test"),null);
        Assertions.assertThrows(InvalidRequestException.class, () -> {
            judicialUserService.refreshUserProfile(refreshRoleRequest, 1,
                    0, "ASC", "objectId");
        });
    }

    @Test
    void test_refreshUserProfile_No_Input() throws JsonProcessingException {
        checkAssertion("");
    }

    @Test
    void test_refreshUserProfile_WhenCcdServiceNameContainComma() throws JsonProcessingException {
        checkAssertion("abc,def");
    }

    @Test
    void test_refreshUserProfile_WhenCcdServiceNameContainAll() throws JsonProcessingException {
        checkAssertion(" all ");
    }

    private void checkAssertion(String ccdServiceNames) {
        var refreshRoleRequest = new RefreshRoleRequest(ccdServiceNames,
                null, null,null);
        Assertions.assertThrows(InvalidRequestException.class, () -> {
            judicialUserService.refreshUserProfile(refreshRoleRequest, 1,
                    0, "ASC", "objectId");
        });
    }

    @Test
    void test_refreshUserProfile_BasedOnSidamIds_200() throws JsonProcessingException {
        var userProfile = buildUserProfile();

        var pageRequest = getPageRequest();
        var page = new PageImpl<>(Collections.singletonList(userProfile));
        var serviceCodeMappingOne = ServiceCodeMapping
                .builder()
                .ticketCode("300")
                .serviceCode("BBA3")
                .build();
        var serviceCodeMappingTwo = ServiceCodeMapping
                .builder()
                .ticketCode("373")
                .serviceCode("BFA1")
                .build();

        var regionMapping = RegionMapping
                .builder()
                .regionId("1")
                .region("National")
                .jrdRegionId("1")
                .jrdRegion("National")
                .build();
        when(serviceCodeMappingRepository.findAllServiceCodeMapping())
                .thenReturn(List.of(serviceCodeMappingOne,serviceCodeMappingTwo));
        when(regionMappingRepository.findAllRegionMappingData()).thenReturn(List.of(regionMapping));
        when(userProfileRepository.fetchUserProfileBySidamIds(List.of("test", "test"), pageRequest))
                .thenReturn(page);
        var refreshRoleRequest = new RefreshRoleRequest("",
                null, Arrays.asList("test", "test"),null);
        var responseEntity = judicialUserService.refreshUserProfile(refreshRoleRequest, 1,
                0, "ASC", "objectId");

        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @DisplayName("Refresh Userprofile based on IAC objectId")
    @Test
    void test_refreshUserProfile_BasedOnObjectIds_200() {
        var userProfile = buildUserProfileIac();
        var pageRequest = getPageRequest();
        var page = new PageImpl<>(List.of(userProfile));

        var serviceCodeMappingOne = ServiceCodeMapping
                .builder()
                .ticketCode("300")
                .serviceCode("BBA3")
                .build();
        var serviceCodeMappingTwo = ServiceCodeMapping
                .builder()
                .ticketCode("373")
                .serviceCode("BFA1")
                .build();

        var regionMapping = RegionMapping
                .builder()
                .regionId("1")
                .region("National")
                .jrdRegionId("1")
                .jrdRegion("National")
                .build();
        when(serviceCodeMappingRepository.findAllServiceCodeMapping()).thenReturn(List.of(serviceCodeMappingOne,
                serviceCodeMappingTwo));
        when(regionMappingRepository.findAllRegionMappingData()).thenReturn(List.of(regionMapping));
        when(userProfileRepository.fetchUserProfileByObjectIds(List.of("test", "test"), pageRequest))
                .thenReturn(page);
        var refreshRoleRequest = new RefreshRoleRequest("",
                Arrays.asList("test", "test"), null,null);
        var responseEntity = judicialUserService.refreshUserProfile(refreshRoleRequest, 1,
                0, "ASC", "objectId");

        assertEquals(200, responseEntity.getStatusCodeValue());
        ArrayList profiles =  (ArrayList)responseEntity.getBody();
        assertEquals(1, profiles.size());

    }

    @DisplayName("Refresh Userprofile based on NonIAC objectId")
    @Test
    void test_refreshUserProfile_BasedOnObjectIds_NonIac200() {
        var userProfile = buildUserProfileNonIac();
        var pageRequest = getPageRequest();
        var page = new PageImpl<>(List.of(userProfile));

        var serviceCodeMappingOne = ServiceCodeMapping
                .builder()
                .ticketCode("366")
                .serviceCode("BBA3")
                .build();

        var regionMapping = RegionMapping
                .builder()
                .regionId("1")
                .region("National")
                .jrdRegionId("1")
                .jrdRegion("National")
                .build();
        when(serviceCodeMappingRepository.findAllServiceCodeMapping()).thenReturn(List.of(serviceCodeMappingOne));
        when(regionMappingRepository.findAllRegionMappingData()).thenReturn(List.of(regionMapping));
        when(userProfileRepository.fetchUserProfileByObjectIds(List.of("test", "test"), pageRequest))
                .thenReturn(page);
        var refreshRoleRequest = new RefreshRoleRequest("",
                Arrays.asList("test", "test"), null,null);

        var responseEntity = judicialUserService.refreshUserProfile(refreshRoleRequest, 1,
                0, "ASC", "objectId");
        assertEquals(200, responseEntity.getStatusCodeValue());
        ArrayList profiles =  (ArrayList)responseEntity.getBody();
        assertEquals(1, profiles.size());
        UserProfileRefreshResponse profile = (UserProfileRefreshResponse)profiles.get(0);
        assertEquals(2, profile.getAppointments().size());
        assertEquals(2, profile.getAuthorisations().size());
    }

    @DisplayName("Refresh Userprofile based on IAC and Non IAC objectId")
    @Test
    void test_refreshUserProfile_BasedOnObjectIds_Iac_NonIac200() {
        var userProfileIac = buildUserProfileIac();
        var userProfileNonIac = buildUserProfileNonIac();
        var pageRequest = getPageRequest();
        var page = new PageImpl<>(List.of(userProfileIac,userProfileNonIac));

        var serviceCodeMappingOne = ServiceCodeMapping
                .builder()
                .ticketCode("366")
                .serviceCode("BBA3")
                .build();

        var regionMapping = RegionMapping
                .builder()
                .regionId("1")
                .region("National")
                .jrdRegionId("1")
                .jrdRegion("National")
                .build();
        when(serviceCodeMappingRepository.findAllServiceCodeMapping()).thenReturn(List.of(serviceCodeMappingOne));
        when(regionMappingRepository.findAllRegionMappingData()).thenReturn(List.of(regionMapping));
        when(userProfileRepository.fetchUserProfileByObjectIds(List.of("test", "test"), pageRequest))
                .thenReturn(page);
        var refreshRoleRequest = new RefreshRoleRequest("",
                Arrays.asList("test", "test"), null,null);

        var responseEntity = judicialUserService.refreshUserProfile(refreshRoleRequest, 1,
                0, "ASC", "objectId");
        assertEquals(200, responseEntity.getStatusCodeValue());
        ArrayList profiles =  (ArrayList)responseEntity.getBody();
        assertEquals(2, profiles.size());
        UserProfileRefreshResponse profileOne = (UserProfileRefreshResponse)profiles.get(0);
        assertEquals(2, profileOne.getAppointments().size());
        assertEquals(2, profileOne.getAuthorisations().size());

        UserProfileRefreshResponse profileTwo = (UserProfileRefreshResponse)profiles.get(1);
        assertEquals(1, profileTwo.getAppointments().size());
        assertEquals(1, profileTwo.getAuthorisations().size());

    }


    @Test
    void test_refreshUserProfile_BasedOnPersonalCodes_Error() throws JsonProcessingException {
        var userProfile = buildUserProfile();

        var pageRequest = getPageRequest();
        var page = new PageImpl<>(Collections.singletonList(userProfile));

        var refreshRoleRequest = new RefreshRoleRequest("",
                Arrays.asList("test", "test"), null,null);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            var responseEntity = judicialUserService.refreshUserProfile(refreshRoleRequest, 1,
                    0, "ASC", "objectId");
        });
    }


    @Test
    void test_refreshUserProfile_BasedOnPersonalCodes_200() {
        var userProfile = buildUserProfileIac();

        var pageRequest = getPageRequest();
        var page = new PageImpl<>(Collections.singletonList(userProfile));
        var serviceCodeMapping = ServiceCodeMapping
                .builder()
                .ticketCode("373")
                .serviceCode("BFA1")
                .build();

        var regionMapping = RegionMapping
                .builder()
                .regionId("2")
                .region("National")
                .jrdRegionId("2")
                .build();
        when(serviceCodeMappingRepository.findAllServiceCodeMapping()).thenReturn(List.of(serviceCodeMapping));
        when(regionMappingRepository.findAllRegionMappingData()).thenReturn(List.of(regionMapping));

        when(userProfileRepository.fetchUserProfileByPersonalCodes(List.of("Emp", "Emp"), pageRequest))
                .thenReturn(page);
        var refreshRoleRequest = new RefreshRoleRequest("",
                null, null, Arrays.asList("Emp", "Emp", null));
        var responseEntity = judicialUserService.refreshUserProfile(refreshRoleRequest, 1,
                0, "ASC", "objectId");

        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void test_refreshUserProfile_BasedOnPersonalCodes_400() throws JsonProcessingException {

        var refreshRoleRequest = new RefreshRoleRequest("",
                null, null, Arrays.asList("Emp", "Emp", null));

        Assertions.assertThrows(InvalidRequestException.class, () -> {
            var responseEntity = judicialUserService.refreshUserProfile(refreshRoleRequest, -1,
                    0, "ASC", "objectId");

        });
    }

    @Test
    void test_refreshUserProfile_BasedOnPersonalCodes_404() throws JsonProcessingException {
        var userProfile = buildUserProfile();

        var pageRequest = getPageRequest();
        var page = new PageImpl<>(Collections.singletonList(userProfile));
        when(userProfileRepository.fetchUserProfileByPersonalCodes(List.of("Emp", "Emp"), pageRequest))
                .thenReturn(null);
        var refreshRoleRequest = new RefreshRoleRequest("",
                null, null, Arrays.asList("Emp", "Emp"));
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            var responseEntity = judicialUserService.refreshUserProfile(refreshRoleRequest, 1,
                    0, "ASC", "objectId");
        });
    }

    @Test
    void test_refreshUserProfile_BasedOnCcdServiceNames_200() throws JsonProcessingException {
        var lrdOrgInfoServiceResponse = new LrdOrgInfoServiceResponse();
        lrdOrgInfoServiceResponse.setServiceCode("BFA1");
        lrdOrgInfoServiceResponse.setCcdServiceName("cmc");
        var body = mapper.writeValueAsString(List.of(lrdOrgInfoServiceResponse));

        when(locationReferenceDataFeignClient.getLocationRefServiceMapping("cmc"))
                .thenReturn(Response.builder()
                        .request(mock(Request.class)).body(body, defaultCharset()).status(201).build());

        var userProfile = buildUserProfile();

        var pageRequest = getPageRequest();

        var page = new PageImpl<>(Collections.singletonList(userProfile));

        when(serviceCodeMappingRepository.fetchTicketCodeFromServiceCode(Set.of("BFA1"))).thenReturn(List.of("386"));
        when(userProfileRepository.fetchUserProfileByServiceNames(Set.of("BFA1"), List.of("386"), pageRequest))
                .thenReturn(page);
        var refreshRoleRequest = new RefreshRoleRequest("cmc",
                null, null,null);
        var responseEntity = judicialUserService.refreshUserProfile(refreshRoleRequest, 1,
                0, "ASC", "objectId");

        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void test_refreshUserProfile_BasedOnCcdServiceNames_when_LrdResponse_IsNon_200() {

        var pageRequest = getPageRequest();
        when(locationReferenceDataFeignClient.getLocationRefServiceMapping("cmc"))
                .thenReturn(Response.builder()
                        .request(mock(Request.class)).body("body", defaultCharset()).status(400).build());

        var refreshRoleRequest = new RefreshRoleRequest("cmc",
                null, null,null);
        Assertions.assertThrows(UserProfileException.class, () -> {
            var responseEntity = judicialUserService.refreshUserProfile(refreshRoleRequest, 1,
                    0, "ASC", "objectId");
        });

    }

    @Test
    void test_refreshUserProfile_BasedOnCcdServiceNames_when_Response_Empty() throws JsonProcessingException {

        var lrdOrgInfoServiceResponse = new LrdOrgInfoServiceResponse();
        lrdOrgInfoServiceResponse.setServiceCode("BFA1");
        lrdOrgInfoServiceResponse.setCcdServiceName("cmc");
        var body = mapper.writeValueAsString(List.of(lrdOrgInfoServiceResponse));

        when(locationReferenceDataFeignClient.getLocationRefServiceMapping("cmc"))
                .thenReturn(Response.builder()
                        .request(mock(Request.class)).body(body, defaultCharset()).status(201).build());

        var pageRequest = getPageRequest();

        var page = new PageImpl<UserProfile>(Collections.emptyList());
        when(userProfileRepository.fetchUserProfileByServiceNames(Set.of("BFA1"), List.of("386"), pageRequest))
                .thenReturn(page);
        when(serviceCodeMappingRepository.fetchTicketCodeFromServiceCode(Set.of("BFA1"))).thenReturn(List.of("386"));
        var refreshRoleRequest = new RefreshRoleRequest("cmc",
                null, null,null);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            var responseEntity = judicialUserService.refreshUserProfile(refreshRoleRequest, 1,
                    0, "ASC", "objectId");
        });
    }

    @Test
    void test_refreshUserProfile_BasedOnCcdServiceNames_when_LrdResponseIsEmpty()
            throws JsonProcessingException {

        var body = mapper.writeValueAsString(Collections.emptyList());

        when(locationReferenceDataFeignClient.getLocationRefServiceMapping("cmc"))
                .thenReturn(Response.builder()
                        .request(mock(Request.class)).body(body, defaultCharset()).status(201).build());
        var refreshRoleRequest = new RefreshRoleRequest("cmc",
                null, null,null);

        Assertions.assertThrows(UserProfileException.class, () -> {
            var responseEntity = judicialUserService.refreshUserProfile(refreshRoleRequest, 1,
                    0, "ASC", "objectId");
        });
    }

    @Test
    void test_refreshUserProfile_BasedOnCcdServiceNames_when_LrdResponseReturns400()
            throws JsonProcessingException {
        var errorResponse = ErrorResponse
                .builder()
                .errorCode(400)
                .errorDescription("testErrorDesc")
                .errorMessage("testErrorMsg")
                .build();
        var body = mapper.writeValueAsString(errorResponse);

        when(locationReferenceDataFeignClient.getLocationRefServiceMapping("cmc"))
                .thenReturn(Response.builder()
                        .request(mock(Request.class)).body(body, defaultCharset()).status(400).build());
        var pageRequest = getPageRequest();
        var refreshRoleRequest = new RefreshRoleRequest("cmc",
                null, null,null);

        Assertions.assertThrows(UserProfileException.class, () -> {
            var responseEntity = judicialUserService.refreshUserProfile(refreshRoleRequest, 1,
                    0, "ASC", "objectId");
        });
    }

    @Test
    void test_refreshUserProfile_BasedOn_All_400() throws JsonProcessingException {
        var refreshRoleRequest = new RefreshRoleRequest("", null, null,null);
        Assertions.assertThrows(InvalidRequestException.class, () -> {
            var responseEntity = judicialUserService.refreshUserProfile(refreshRoleRequest, 1,
                    0, "ASC", "objectId");
        });
    }

    @NotNull
    private PageRequest getPageRequest() {
        return RequestUtils.validateAndBuildPaginationObject(1, 0,
                "ASC", "objectId",
                20, "id", UserProfile.class);
    }

    UserProfile buildUserProfile() {

        var baseLocation = new BaseLocation();
        baseLocation.setBaseLocationId("1");
        baseLocation.setCircuit("National");

        var location = new Location();
        location.setRegionId("1");
        location.setRegionDescCy("National");
        location.setRegionDescEn("National");

        var appointment = new Appointment();
        appointment.setEpimmsId("1234");
        appointment.setOfficeAppointmentId(1L);
        appointment.setIsPrincipleAppointment(true);
        appointment.setStartDate(LocalDate.now());
        appointment.setEndDate(LocalDate.now());
        appointment.setCreatedDate(LocalDateTime.now());
        appointment.setLastLoadedDate(LocalDateTime.now());
        appointment.setBaseLocation(baseLocation);
        appointment.setLocation(location);
        appointment.setServiceCode(null);
        appointment.setRegionId("1");

        var appointmentTwo = new Appointment();
        appointmentTwo.setEpimmsId(null);
        appointmentTwo.setOfficeAppointmentId(1L);
        appointmentTwo.setIsPrincipleAppointment(true);
        appointmentTwo.setStartDate(LocalDate.now());
        appointmentTwo.setEndDate(LocalDate.now().minusDays(10));
        appointmentTwo.setCreatedDate(LocalDateTime.now());
        appointmentTwo.setLastLoadedDate(LocalDateTime.now());
        appointmentTwo.setBaseLocation(baseLocation);
        appointmentTwo.setLocation(location);
        appointmentTwo.setServiceCode("BFA1");
        appointment.setRegionId("2");

        var appointmentThree = new Appointment();
        appointmentThree.setEpimmsId(null);
        appointmentThree.setOfficeAppointmentId(1L);
        appointmentThree.setIsPrincipleAppointment(true);
        appointmentThree.setStartDate(LocalDate.now());
        appointmentThree.setEndDate(LocalDate.now().plusDays(10));
        appointmentThree.setCreatedDate(LocalDateTime.now());
        appointmentThree.setLastLoadedDate(LocalDateTime.now());
        appointmentThree.setBaseLocation(baseLocation);
        appointmentThree.setLocation(location);
        appointmentThree.setServiceCode("BBA3");
        appointment.setRegionId("2");

        var appointmentFour = new Appointment();
        appointmentFour.setEpimmsId("10");
        appointmentFour.setOfficeAppointmentId(1L);
        appointmentFour.setIsPrincipleAppointment(true);
        appointmentFour.setStartDate(LocalDate.now());
        appointmentFour.setEndDate(LocalDate.now());
        appointmentFour.setCreatedDate(LocalDateTime.now());
        appointmentFour.setLastLoadedDate(LocalDateTime.now());
        appointmentFour.setBaseLocation(baseLocation);
        appointmentFour.setLocation(location);
        appointmentFour.setServiceCode("BBA3");
        appointment.setRegionId("3");

        var authorisation = new Authorisation();
        authorisation.setOfficeAuthId(1L);
        authorisation.setJurisdiction("Languages");
        authorisation.setStartDate(null);
        authorisation.setEndDate(null);
        authorisation.setCreatedDate(LocalDateTime.now());
        authorisation.setLastUpdated(LocalDateTime.now());
        authorisation.setLowerLevel("Welsh");
        authorisation.setPersonalCode("");
        authorisation.setTicketCode(null);

        var authorisationOne = new Authorisation();
        authorisationOne.setOfficeAuthId(1L);
        authorisationOne.setJurisdiction("Languages");
        authorisationOne.setStartDate(LocalDateTime.now());
        authorisationOne.setEndDate(LocalDateTime.now().plusDays(15));
        authorisationOne.setCreatedDate(LocalDateTime.now());
        authorisationOne.setLastUpdated(LocalDateTime.now());
        authorisationOne.setLowerLevel("Welsh");
        authorisationOne.setPersonalCode("");
        authorisationOne.setTicketCode("373");

        var authorisationTwo = new Authorisation();
        authorisationTwo.setOfficeAuthId(1L);
        authorisationTwo.setJurisdiction("Languages");
        authorisationTwo.setStartDate(LocalDateTime.now());
        authorisationTwo.setEndDate(LocalDateTime.now().minusDays(5));
        authorisationTwo.setCreatedDate(LocalDateTime.now());
        authorisationTwo.setLastUpdated(LocalDateTime.now());
        authorisationTwo.setLowerLevel("Welsh");
        authorisationTwo.setPersonalCode("");
        authorisationTwo.setTicketCode("100");

        var userProfile = new UserProfile();
        userProfile.setPersonalCode("Emp");
        userProfile.setKnownAs("TestEmp");
        userProfile.setSurname("Test");
        userProfile.setFullName("Test1");
        userProfile.setPostNominals("Test Test1");
        userProfile.setEjudiciaryEmailId("abc@gmail.com");
        userProfile.setLastWorkingDate(LocalDate.now());
        userProfile.setActiveFlag(true);
        userProfile.setCreatedDate(LocalDateTime.now());
        userProfile.setLastLoadedDate(LocalDateTime.now());
        userProfile.setObjectId("");
        userProfile.setSidamId("4c0ff6a3-8fd6-803b-301a-29d9dacccca8");

        baseLocation.setAppointments(List.of(appointment,appointmentTwo,appointmentThree,appointmentFour));
        userProfile.setAppointments(List.of(appointment,appointmentTwo,appointmentThree,appointmentFour));
        userProfile.setAuthorisations(List.of(authorisation,authorisationOne,authorisationTwo));

        return userProfile;
    }

    //Valid IAC record
    UserProfile buildUserProfileIac() {

        var baseLocation = new BaseLocation();
        baseLocation.setBaseLocationId("2");
        baseLocation.setCircuit("Nationals");

        var location = new Location();
        location.setRegionId("1");
        location.setRegionDescCy("Nationals");
        location.setRegionDescEn("Nationals");

        var appointment = new Appointment();
        appointment.setEpimmsId(" ");
        appointment.setOfficeAppointmentId(1L);
        appointment.setIsPrincipleAppointment(true);
        appointment.setStartDate(LocalDate.now());
        appointment.setEndDate(null);
        appointment.setCreatedDate(LocalDateTime.now());
        appointment.setLastLoadedDate(LocalDateTime.now());
        appointment.setServiceCode("BFA1");
        appointment.setRegionId("1");



        var authorisation = new Authorisation();
        authorisation.setOfficeAuthId(1L);
        authorisation.setJurisdiction("Languages");
        authorisation.setStartDate(LocalDateTime.now());
        authorisation.setEndDate(null);
        authorisation.setCreatedDate(LocalDateTime.now());
        authorisation.setLastUpdated(LocalDateTime.now());
        authorisation.setLowerLevel("Welsh");
        authorisation.setPersonalCode("100");
        authorisation.setTicketCode("373");

        var userProfile = new UserProfile();
        userProfile.setPersonalCode("Emp");
        userProfile.setKnownAs("TestEmp");
        userProfile.setSurname("Test");
        userProfile.setFullName("Test1");
        userProfile.setPostNominals("Test Test1");
        userProfile.setEjudiciaryEmailId("abc@gmail.com");
        userProfile.setLastWorkingDate(LocalDate.now());
        userProfile.setActiveFlag(false);
        userProfile.setCreatedDate(LocalDateTime.now());
        userProfile.setLastLoadedDate(LocalDateTime.now());
        userProfile.setObjectId("asd12345-0987asdas-asdas8asdas");
        userProfile.setSidamId("4c0ff6a3-8fd6-803b-301a-29d9dacccca8");

        authorisation.setUserProfile(userProfile);
        baseLocation.setAppointments(List.of(appointment));
        userProfile.setAppointments(List.of(appointment));
        userProfile.setAuthorisations(List.of(authorisation));

        return userProfile;

    }

    //user Profile IAC records
    UserProfile buildUserProfileNonIac() {

        var baseLocation = new BaseLocation();
        baseLocation.setBaseLocationId("2");
        baseLocation.setCircuit("Nationals");

        var location = new Location();
        location.setRegionId("1");
        location.setRegionDescCy("Nationals");
        location.setRegionDescEn("Nationals");

        var appointmentOne = new Appointment();
        appointmentOne.setEpimmsId(" ");
        appointmentOne.setOfficeAppointmentId(1L);
        appointmentOne.setIsPrincipleAppointment(true);
        appointmentOne.setStartDate(LocalDate.now());
        appointmentOne.setEndDate(null);
        appointmentOne.setCreatedDate(LocalDateTime.now());
        appointmentOne.setLastLoadedDate(LocalDateTime.now());
        appointmentOne.setBaseLocation(baseLocation);
        appointmentOne.setLocation(location);
        appointmentOne.setServiceCode("BBA3");
        appointmentOne.setRegionId("2");

        var appointmentTwo = new Appointment();
        appointmentTwo.setEpimmsId(" ");
        appointmentTwo.setOfficeAppointmentId(1L);
        appointmentTwo.setIsPrincipleAppointment(true);
        appointmentTwo.setStartDate(LocalDate.now());
        appointmentTwo.setEndDate(LocalDate.now().minusDays(1));
        appointmentTwo.setCreatedDate(LocalDateTime.now());
        appointmentTwo.setLastLoadedDate(LocalDateTime.now());
        appointmentTwo.setBaseLocation(baseLocation);
        appointmentTwo.setLocation(location);
        appointmentTwo.setServiceCode(" ");
        appointmentTwo.setRegionId("2");


        var authorisationOne = new Authorisation();
        authorisationOne.setOfficeAuthId(1L);
        authorisationOne.setJurisdiction("Languages");
        authorisationOne.setStartDate(LocalDateTime.now());
        authorisationOne.setEndDate(null);
        authorisationOne.setCreatedDate(LocalDateTime.now());
        authorisationOne.setLastUpdated(LocalDateTime.now());
        authorisationOne.setLowerLevel("Welsh");
        authorisationOne.setPersonalCode("100");
        authorisationOne.setTicketCode("366");

        var authorisationTwo = new Authorisation();
        authorisationTwo.setOfficeAuthId(1L);
        authorisationTwo.setJurisdiction("Languages");
        authorisationTwo.setStartDate(LocalDateTime.now());
        authorisationTwo.setEndDate(LocalDateTime.now().minusDays(1));
        authorisationTwo.setCreatedDate(LocalDateTime.now());
        authorisationTwo.setLastUpdated(LocalDateTime.now());
        authorisationTwo.setLowerLevel("Welsh");
        authorisationTwo.setPersonalCode("100");
        authorisationTwo.setTicketCode(" ");

        var userProfile = new UserProfile();
        userProfile.setPersonalCode("Emp");
        userProfile.setKnownAs("TestEmp");
        userProfile.setSurname("Test");
        userProfile.setFullName("Test1");
        userProfile.setPostNominals("Test Test1");
        userProfile.setEjudiciaryEmailId("abcd@gmail.com");
        userProfile.setLastWorkingDate(LocalDate.now());
        userProfile.setActiveFlag(false);
        userProfile.setCreatedDate(LocalDateTime.now());
        userProfile.setLastLoadedDate(LocalDateTime.now());
        userProfile.setObjectId("asd12345-0987asdas-asdas8asdas");
        userProfile.setSidamId("4c0ff6a3-8fd6-803b-301a-29d9dacccca8");

        baseLocation.setAppointments(List.of(appointmentOne,appointmentTwo));
        userProfile.setAppointments(List.of(appointmentOne,appointmentTwo));
        userProfile.setAuthorisations(List.of(authorisationOne,authorisationTwo));

        return userProfile;

    }

}
