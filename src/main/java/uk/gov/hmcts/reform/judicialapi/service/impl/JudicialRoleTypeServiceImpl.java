package uk.gov.hmcts.reform.judicialapi.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.judicialapi.controller.response.JudicialRoleTypeListResponse;
import uk.gov.hmcts.reform.judicialapi.domain.JudicialRoleType;
import uk.gov.hmcts.reform.judicialapi.persistence.JudicialRoleTypeRepository;
import uk.gov.hmcts.reform.judicialapi.service.JudicialRoleTypeService;

@Service
public class JudicialRoleTypeServiceImpl implements JudicialRoleTypeService {

    @Autowired
    JudicialRoleTypeRepository judicialRoleTypeRepository;

    public JudicialRoleTypeListResponse retrieveJudicialRoles() {
        List<JudicialRoleType> judicialRoleTypes = judicialRoleTypeRepository.findAll();
        return new JudicialRoleTypeListResponse(judicialRoleTypes);
    }

}
