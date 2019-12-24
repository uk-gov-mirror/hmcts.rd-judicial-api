package uk.gov.hmcts.reform.judicialapi.controller.response;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import uk.gov.hmcts.reform.judicialapi.domain.JudicialRoleType;

public class JudicialRoleTypeResponseUnitTest {

    @Test
    public void testJudicialRoleTypeResponse() {
        final String expectRoleId = "1";
        final String expectRoleDescEn = "Role Description";
        final String expectRoleDescCy = "Role Description Welsh";
        final JudicialRoleType judicialRoleType = new JudicialRoleType();
        judicialRoleType.setRoleId(expectRoleId);
        judicialRoleType.setRoleDescEn(expectRoleDescEn);
        judicialRoleType.setRoleDescCy(expectRoleDescCy);

        JudicialRoleTypeResponse sut = new JudicialRoleTypeResponse(judicialRoleType);

        assertThat(sut.getRoleId()).isEqualTo(expectRoleId);
        assertThat(sut.getRoleDescEn()).isEqualTo(expectRoleDescEn);
        assertThat(sut.getRoleDescCy()).isEqualTo(expectRoleDescCy);

    }
}
