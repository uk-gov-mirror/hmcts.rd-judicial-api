package uk.gov.hmcts.reform.judicialapi.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@JsonDeserialize(builder = JudicialRoleTypeListResponse.JudicialRoleTypeListResponseBuilder.class)
@AllArgsConstructor
@NoArgsConstructor
public class JudicialRoleTypeListResponse {

    @JsonProperty
    private List<JudicialRoleTypeResponse> judicialRoleTypeResponseList;

}
