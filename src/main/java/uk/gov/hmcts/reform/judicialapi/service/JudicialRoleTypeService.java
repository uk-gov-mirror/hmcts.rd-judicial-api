package uk.gov.hmcts.reform.judicialapi.service;

import uk.gov.hmcts.reform.judicialapi.controller.response.JudicialRoleTypeListResponse;
import uk.gov.hmcts.reform.judicialapi.controller.response.JudicialRoleTypeResponse;
import uk.gov.hmcts.reform.judicialapi.domain.JudicialRoleType;

import java.util.List;

public interface JudicialRoleTypeService {

    JudicialRoleTypeListResponse retrieveJudicialRoles();
}
