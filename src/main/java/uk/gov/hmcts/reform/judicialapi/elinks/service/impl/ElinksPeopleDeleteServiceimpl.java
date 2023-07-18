package uk.gov.hmcts.reform.judicialapi.elinks.service.impl;


import java.util.ArrayList;
import java.util.List;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.hmcts.reform.judicialapi.controller.request.UserSearchRequest;
import uk.gov.hmcts.reform.judicialapi.elinks.controller.request.ResultsRequest;
import uk.gov.hmcts.reform.judicialapi.elinks.repository.AppointmentsRepository;
import uk.gov.hmcts.reform.judicialapi.elinks.repository.AuthorisationsRepository;
import uk.gov.hmcts.reform.judicialapi.elinks.repository.JudicialRoleTypeRepository;
import uk.gov.hmcts.reform.judicialapi.elinks.repository.ProfileRepository;
import uk.gov.hmcts.reform.judicialapi.elinks.service.ELinksPeopleDeleteService;
import uk.gov.hmcts.reform.judicialapi.elinks.service.ElinkUserService;
import uk.gov.hmcts.reform.judicialapi.repository.ServiceCodeMappingRepository;

@Slf4j
@Service
@Setter
public class ElinksPeopleDeleteServiceimpl implements ELinksPeopleDeleteService {

    @Autowired
    private JudicialRoleTypeRepository judicialRoleTypeRepository;

    @Autowired
    private AuthorisationsRepository authorisationsRepository;

    @Autowired
    private AppointmentsRepository appointmentsRepository;


    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteAuth(ResultsRequest resultsRequest) {
        authorisationsRepository.deleteByPersonalCode(resultsRequest.getPersonalCode());
        appointmentsRepository.deleteByPersonalCode(resultsRequest.getPersonalCode());
        judicialRoleTypeRepository.deleteByPersonalCode(resultsRequest.getPersonalCode());
    }
}
