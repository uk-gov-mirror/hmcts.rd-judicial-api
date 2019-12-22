package uk.gov.hmcts.reform.judicialapi.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.stream.Collectors;

import uk.gov.hmcts.reform.judicialapi.domain.JudicialRoleType;

public class JudicialRoleTypeListResponse {

    @JsonProperty
    private List<JudicialRoleTypeResponse> judicialRoleTypeResponseList;

    public JudicialRoleTypeListResponse(List<JudicialRoleType> judicialRoleTypes) {
        this.judicialRoleTypeResponseList = judicialRoleTypes.stream().map(judicialRoleType ->
                new JudicialRoleTypeResponse(judicialRoleType)).collect(Collectors.toList());
    }
}
