package uk.gov.hmcts.reform.judicialapi.elinks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.judicialapi.elinks.response.ElinkPeopleWrapperResponse;
import uk.gov.hmcts.reform.judicialapi.elinks.util.ElinksEnabledIntegrationTest;

import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.judicialapi.util.FeatureConditionEvaluation.FORBIDDEN_EXCEPTION_LD;

class PeopleIntegrationTest extends ElinksEnabledIntegrationTest {

    @BeforeEach
    void setUp() {

    }


    @DisplayName("Scenario-Retrieve using one UserRequest")
    @Test
    void getPeopleUserProfile() {

        Map<String, Object> response = elinksReferenceDataClient.getPeoples();
        assertThat(response).containsEntry("http_status", "200 OK");
        ElinkPeopleWrapperResponse profiles = (ElinkPeopleWrapperResponse)response.get("body");
        assertEquals("People data loaded successfully", profiles.getMessage());
    }


    @DisplayName("Scenario-FeatureDisabled")
    @Test
    void shouldReturn403WhenLdFeatureDisabled() {
        Map<String, String> launchDarklyMap = new HashMap<>();
        launchDarklyMap.put("JrdUsersController.fetchUsers", "test-jrd-flag");
        when(featureToggleServiceImpl.isFlagEnabled(anyString())).thenReturn(false);
        when(featureToggleServiceImpl.getLaunchDarklyMap()).thenReturn(launchDarklyMap);
        Map<String, Object> errorResponseMap = elinksReferenceDataClient.getPeoples();

        assertThat(errorResponseMap).containsEntry("http_status", "403");
        assertThat((String) errorResponseMap.get("response_body"))
                .contains("test-jrd-flag".concat(SPACE).concat(FORBIDDEN_EXCEPTION_LD));
    }
}
