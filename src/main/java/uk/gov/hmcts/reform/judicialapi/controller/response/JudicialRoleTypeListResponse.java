package uk.gov.hmcts.reform.judicialapi.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import uk.gov.hmcts.reform.judicialapi.domain.JudicialRoleType;

import java.util.List;
import java.util.stream.Collectors;

public class JudicialRoleTypeListResponse {

    @JsonProperty
    private List<JudicialRoleTypeResponse> judicialRoleTypeResponseList;

    public JudicialRoleTypeListResponse(List<JudicialRoleType> judicialRoleTypes) {
        this.judicialRoleTypeResponseList = judicialRoleTypes.stream().map(judicialRoleType ->
                new JudicialRoleTypeResponse(judicialRoleType)).collect(Collectors.toList());
    }
}
