package uk.gov.hmcts.reform.judicialapi.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class JudicialRoleTypeUnitTest {

    @Test
    public void judicialRoleTypeTest() {

        JudicialRoleType judicialRoleType = new JudicialRoleType("1", "Role description", "Role description in Welsh");

        assertThat(judicialRoleType.getRoleId()).isEqualTo("1");
        assertThat(judicialRoleType.getRoleDescEn()).isEqualTo("Role description");
        assertThat(judicialRoleType.getRoleDescCy()).isEqualTo("Role description in Welsh");
    }
}
