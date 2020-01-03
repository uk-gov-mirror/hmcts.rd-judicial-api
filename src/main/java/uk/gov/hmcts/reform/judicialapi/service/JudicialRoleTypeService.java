package uk.gov.hmcts.reform.judicialapi.service;

import uk.gov.hmcts.reform.judicialapi.controller.response.JudicialRoleTypeResponse;

import java.util.List;

public interface JudicialRoleTypeService {

    List<JudicialRoleTypeResponse> retrieveJudicialRoles();
}
