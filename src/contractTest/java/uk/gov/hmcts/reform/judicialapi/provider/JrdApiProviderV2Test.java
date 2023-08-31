package uk.gov.hmcts.reform.judicialapi.provider;

import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import au.com.dius.pact.provider.junitsupport.IgnoreNoPactsToVerify;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.loader.PactBroker;
import au.com.dius.pact.provider.junitsupport.loader.VersionSelector;
import au.com.dius.pact.provider.spring.junit5.MockMvcTestTarget;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.reform.judicialapi.elinks.controller.JrdElinkController;
import uk.gov.hmcts.reform.judicialapi.elinks.domain.ServiceCodeMapping;
import uk.gov.hmcts.reform.judicialapi.elinks.repository.AppointmentsRepository;
import uk.gov.hmcts.reform.judicialapi.elinks.repository.AuthorisationsRepository;
import uk.gov.hmcts.reform.judicialapi.elinks.repository.JudicialRoleTypeRepository;
import uk.gov.hmcts.reform.judicialapi.elinks.repository.ProfileRepository;
import uk.gov.hmcts.reform.judicialapi.elinks.repository.ServiceCodeMappingRepository;
import uk.gov.hmcts.reform.judicialapi.elinks.response.UserSearchResponseWrapper;
import uk.gov.hmcts.reform.judicialapi.elinks.service.impl.ElinkUserServiceImpl;
import uk.gov.hmcts.reform.judicialapi.elinks.validator.ElinksRefreshUserValidator;
import uk.gov.hmcts.reform.judicialapi.feign.LocationReferenceDataFeignClient;
import uk.gov.hmcts.reform.judicialapi.repository.RegionMappingRepository;
import uk.gov.hmcts.reform.judicialapi.validator.RefreshUserValidator;

import java.util.List;

import static java.util.Objects.nonNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@Provider("referenceData_judicialv2")
@PactBroker(scheme = "${PACT_BROKER_SCHEME:http}",
        host = "${PACT_BROKER_URL:localhost}",
        port = "${PACT_BROKER_PORT:80}", consumerVersionSelectors = {
        @VersionSelector(tag = "master")})
@ContextConfiguration(classes = {JrdElinkController.class, ElinkUserServiceImpl.class})
@TestPropertySource(properties = {"defaultPageSize=10", "refresh.pageSize=10", "refresh.sortColumn=objectId"})
@IgnoreNoPactsToVerify
public class JrdApiProviderV2Test {

    @Autowired
    JrdElinkController jrdElinkController;

    @MockBean
    ProfileRepository userProfileRepository;

    @MockBean
    JudicialRoleTypeRepository judicialRoleTypeRepository;

    @MockBean
    AuthorisationsRepository authorisationsRepository;

    @MockBean
    AppointmentsRepository appointmentsRepository;

    @MockBean
    ElinksRefreshUserValidator elinksRefreshUserValidator;

    @MockBean
    @Qualifier("elinksServiceCodeMappingRepository")
    ServiceCodeMappingRepository serviceCodeMappingRepository;

    @MockBean
    RegionMappingRepository regionMappingRepository;

    @MockBean
    RefreshUserValidator refreshUserValidator;

    @MockBean
    LocationReferenceDataFeignClient locationReferenceDataFeignClient;

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider.class)
    void pactVerificationTestTemplate(PactVerificationContext context) {
        if (context != null) {
            context.verifyInteraction();
        }
    }

    @BeforeEach
    void before(PactVerificationContext context) {
        MockMvcTestTarget testTarget = new MockMvcTestTarget();
        System.getProperties().setProperty("pact.verifier.publishResults", "true");
        testTarget.setControllers(jrdElinkController);
        if (nonNull(context)) {
            context.setTarget(testTarget);
        }

    }


    @State({"return judicial user profiles"})
    public void toReturnUserProfilesDetailsForTheGivenSearchRequestTypeAhead() {
        var userSearchResponseWrapper = new UserSearchResponseWrapper();
        userSearchResponseWrapper.setIdamId("44362987-4b00-f2e7-4ff8-761b87f16bf9");
        userSearchResponseWrapper.setFullName("testFullName");
        userSearchResponseWrapper.setKnownAs("testKnownAs");
        userSearchResponseWrapper.setSurname("surname");
        userSearchResponseWrapper.setEmailId("test@test.com");
        userSearchResponseWrapper.setTitle("Family Judge");
        userSearchResponseWrapper.setPersonalCode("1234");
        userSearchResponseWrapper.setPostNominals("Mr");
        userSearchResponseWrapper.setInitials("I N");

        var serviceCodeMapping = ServiceCodeMapping
                .builder()
                .ticketCode("testTicketCode")
                .build();

        var userProfiles = List.of(userSearchResponseWrapper);

        when(serviceCodeMappingRepository.findByServiceCodeIgnoreCase(any())).thenReturn(List.of(serviceCodeMapping));
        when(userProfileRepository.findBySearchForString(any(),any(),any(), anyList(), anyList()))
                .thenReturn(userProfiles);
    }

}