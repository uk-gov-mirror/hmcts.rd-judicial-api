package uk.gov.hmcts.reform.judicialapi.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.judicialapi.domain.JudicialRoleType;

@Getter
//@Builder
//@JsonDeserialize(builder = JudicialRoleTypeResponse.JudicialRoleTypeResponseBuilder.class)
//@AllArgsConstructor
@NoArgsConstructor
public class JudicialRoleTypeResponse {

    @JsonProperty
    private String roleId;
    @JsonProperty
    private String roleDescEn;
    @JsonProperty
    private String roleDescCy;

    public JudicialRoleTypeResponse(JudicialRoleType judicialRoleType) {
        this.roleId = judicialRoleType.getRoleId();
        this.roleDescEn = judicialRoleType.getRoleDescEn();
        this.roleDescCy = judicialRoleType.getRoleDescCy();
    }

    @Override
    public String toString() {
        return "{roleId=" + this.getRoleId() + ", roleDescEn=" + this.getRoleDescEn() + ", roleDescCy=" + this.getRoleDescCy() + "}";
    }

}
