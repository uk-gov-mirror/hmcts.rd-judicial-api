package uk.gov.hmcts.reform.judicialapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.google.common.collect.ImmutableList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import uk.gov.hmcts.reform.judicialapi.controller.response.JudicialRoleTypeListResponse;
import uk.gov.hmcts.reform.judicialapi.controller.response.JudicialRoleTypeResponse;
import uk.gov.hmcts.reform.judicialapi.util.SpringBootIntegrationTest;


@Slf4j
public class RetrieveJudicialRolesTypesTest extends SpringBootIntegrationTest {

    @Test
    public void testGetAllRoles() {
        JudicialRoleTypeListResponse judicialRoleTypeListResponse = restTemplate
                .getForObject("/refdata/v1/judicial/roles", JudicialRoleTypeListResponse.class);
        List<JudicialRoleTypeResponse> list = ImmutableList.of(JudicialRoleTypeResponse.builder().roleId("1").roleDescCy("test").roleDescEn("Magistrate").build(),
                JudicialRoleTypeResponse.builder().roleId("2").roleDescCy("test").roleDescEn("Advisory Committee Member - Magistrate").build());

        assertNotNull(judicialRoleTypeListResponse);
        assertEquals(judicialRoleTypeListResponse, JudicialRoleTypeListResponse.builder().judicialRoleTypeResponseList(list).build());
    }
}
