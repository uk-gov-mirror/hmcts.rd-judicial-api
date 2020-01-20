package uk.gov.hmcts.reform.judicialapi.controller.response;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class JudicialRoleTypeResponseUnitTest {

    @Test
    public void testJudicialRoleTypeResponse() {
        final String roleId = "1";
        final String roleDescEn = "Role Description";
        final String roleDescCy = "Role Description Welsh";

        JudicialRoleTypeResponse sut = new JudicialRoleTypeResponse(roleId, roleDescEn, roleDescCy);

        assertThat(sut.getRoleId()).isEqualTo(roleId);
        assertThat(sut.getRoleDescEn()).isEqualTo(roleDescEn);
        assertThat(sut.getRoleDescCy()).isEqualTo(roleDescCy);

    }
}
