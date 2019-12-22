package uk.gov.hmcts.reform.judicialapi.service;

import uk.gov.hmcts.reform.judicialapi.controller.response.JudicialRoleTypeListResponse;

public interface JudicialRoleTypeService {

    JudicialRoleTypeListResponse retrieveJudicialRoles();
}
