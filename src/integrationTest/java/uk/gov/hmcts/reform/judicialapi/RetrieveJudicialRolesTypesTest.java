package uk.gov.hmcts.reform.judicialapi;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import uk.gov.hmcts.reform.judicialapi.domain.JudicialRoleType;
import uk.gov.hmcts.reform.judicialapi.util.AuthorizationEnabledIntegrationTest;

@Slf4j
public class RetrieveJudicialRolesTypesTest extends AuthorizationEnabledIntegrationTest {

    @Test
    public void retrieveJudicialRole() {
        JudicialRoleType judicialRole = new JudicialRoleType("1", "Test Role", "Test Role in Welsh");
        JudicialRoleType judicialRole1 = new JudicialRoleType("2", "Test Role 1", "Test Role 1 in Welsh");

        judicialRoleTypeRepository.save(judicialRole);
        Map<String, Object> response = judicialReferenceDataClient.retrieveAllJudicialRoleTypes("caseworker");

        List<HashMap> rolesResponses = (List<HashMap>) response.get("judicialRoleTypeResponseList");
        rolesResponses.stream().forEach(role -> {
            assertThat(role.get("roleId")).isNotNull();
            assertThat(role.get("roleDescEn")).isNotNull();
        });
    }
}
