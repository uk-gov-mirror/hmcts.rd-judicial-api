package uk.gov.hmcts.reform.judicialapi.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Getter
@Component
@Data
@Builder
@JsonDeserialize(builder = JudicialRoleTypeResponse.JudicialRoleTypeResponseBuilder.class)
@AllArgsConstructor
@NoArgsConstructor
public class JudicialRoleTypeResponse {

    @JsonProperty
    private String roleId;
    @JsonProperty
    private String roleDescEn;
    @JsonProperty
    private String roleDescCy;

}
