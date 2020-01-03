package uk.gov.hmcts.reform.judicialapi.service;

import java.util.List;

import uk.gov.hmcts.reform.judicialapi.controller.response.JudicialRoleTypeResponse;

public interface JudicialRoleTypeService {

    List<JudicialRoleTypeResponse> retrieveJudicialRoles();
}
