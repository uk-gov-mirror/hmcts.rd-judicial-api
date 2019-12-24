package uk.gov.hmcts.reform.judicialapi;

import lombok.extern.slf4j.Slf4j;
import uk.gov.hmcts.reform.judicialapi.domain.JudicialRoleType;
import uk.gov.hmcts.reform.judicialapi.util.AuthorizationEnabledIntegrationTest;

@Slf4j
public class RetrieveJudicialRolesTypesTest extends AuthorizationEnabledIntegrationTest {

    JudicialRoleType judicialRole = new JudicialRoleType("1", "Test Role", "Test Role in Welsh");

}
