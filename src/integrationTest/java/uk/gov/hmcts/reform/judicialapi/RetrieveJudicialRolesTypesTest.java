package uk.gov.hmcts.reform.judicialapi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import uk.gov.hmcts.reform.judicialapi.controller.response.JudicialRoleTypeResponse;
import uk.gov.hmcts.reform.judicialapi.util.AuthorizationEnabledIntegrationTest;
import uk.gov.hmcts.reform.judicialapi.util.SpringBootIntegrationTest;


@Slf4j
public class RetrieveJudicialRolesTypesTest extends AuthorizationEnabledIntegrationTest {

    @Test
    public void testGetAllRoles() {
        List<JudicialRoleTypeResponse> judicialRoleTypeListResponse = restTemplate
                .getForObject("/refdata/v1/judicial/roles", List.class);
        List<JudicialRoleTypeResponse> list = ImmutableList.of(JudicialRoleTypeResponse.builder().roleId("1").roleDescCy("test").roleDescEn("Magistrate").build(),
                JudicialRoleTypeResponse.builder().roleId("2").roleDescCy("test").roleDescEn("Advisory Committee Member - Magistrate").build());

        assertNotNull(judicialRoleTypeListResponse);
        assertEquals(judicialRoleTypeListResponse.toString(), list.toString());
    }
}
