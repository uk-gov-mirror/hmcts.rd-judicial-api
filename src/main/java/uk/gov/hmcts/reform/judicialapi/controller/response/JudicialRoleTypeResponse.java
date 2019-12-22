package uk.gov.hmcts.reform.judicialapi.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import uk.gov.hmcts.reform.judicialapi.domain.JudicialRoleType;

@Getter
public class JudicialRoleTypeResponse {

    @JsonProperty
    private final String roleId;
    @JsonProperty
    private final String roleDescEn;
    @JsonProperty
    private final String roleDescCy;

    public JudicialRoleTypeResponse(JudicialRoleType judicialRoleType) {
        this.roleId = judicialRoleType.getRoleId();
        this.roleDescEn = judicialRoleType.getRoleDescEn();
        this.roleDescCy = judicialRoleType.getRoleDescCy();
    }
}
