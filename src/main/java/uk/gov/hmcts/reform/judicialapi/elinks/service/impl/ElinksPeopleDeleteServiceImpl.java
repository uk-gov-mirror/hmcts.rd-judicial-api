package uk.gov.hmcts.reform.judicialapi.elinks.service.impl;


import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.hmcts.reform.judicialapi.elinks.controller.request.ResultsRequest;
import uk.gov.hmcts.reform.judicialapi.elinks.domain.Appointment;
import uk.gov.hmcts.reform.judicialapi.elinks.domain.Authorisation;
import uk.gov.hmcts.reform.judicialapi.elinks.domain.JudicialRoleType;
import uk.gov.hmcts.reform.judicialapi.elinks.domain.UserProfile;
import uk.gov.hmcts.reform.judicialapi.elinks.repository.AppointmentsRepository;
import uk.gov.hmcts.reform.judicialapi.elinks.repository.AuthorisationsRepository;
import uk.gov.hmcts.reform.judicialapi.elinks.repository.JudicialRoleTypeRepository;
import uk.gov.hmcts.reform.judicialapi.elinks.repository.ProfileRepository;
import uk.gov.hmcts.reform.judicialapi.elinks.service.ElinksPeopleDeleteAuditService;
import uk.gov.hmcts.reform.judicialapi.elinks.service.ElinksPeopleDeleteService;

import java.util.List;

@Slf4j
@Service
@Setter
public class ElinksPeopleDeleteServiceImpl implements ElinksPeopleDeleteService {

    @Autowired
    private JudicialRoleTypeRepository judicialRoleTypeRepository;

    @Autowired
    private AuthorisationsRepository authorisationsRepository;

    @Autowired
    private AppointmentsRepository appointmentsRepository;

    @Autowired
    ProfileRepository profileRepository;

    @Autowired
    private ElinksPeopleDeleteAuditService elinksPeopleDeleteAuditService;


    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteAuth(ResultsRequest resultsRequest) {
        authorisationsRepository.deleteByPersonalCode(resultsRequest.getPersonalCode());
        appointmentsRepository.deleteByPersonalCode(resultsRequest.getPersonalCode());
        judicialRoleTypeRepository.deleteByPersonalCode(resultsRequest.getPersonalCode());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deletePeople(String personalCode) {
        log.info("Delete by personalCode");
        authorisationsRepository.deleteByPersonalCode(personalCode);
        appointmentsRepository.deleteByPersonalCode(personalCode);
        judicialRoleTypeRepository.deleteByPersonalCode(personalCode);
        profileRepository.deleteById(personalCode);

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deletePeople(List<String> personalCodes) {
        log.info("Delete people by personal codes");
        // Get and persist into audit table
        List<Authorisation> authorisations = authorisationsRepository.findAllByPersonalCode(personalCodes);
        List<Appointment> appointments = appointmentsRepository.findAllByPersonalCode(personalCodes);
        List<JudicialRoleType> judicialRoleTypes = judicialRoleTypeRepository.findAllByPersonalCode(personalCodes);
        List<UserProfile> userProfiles = profileRepository.findAllById(personalCodes);
        elinksPeopleDeleteAuditService.auditPeopleDelete(authorisations, appointments, judicialRoleTypes, userProfiles);

        //Delete entries from db
        authorisationsRepository.deleteAll(authorisations);
        appointmentsRepository.deleteAll(appointments);
        judicialRoleTypeRepository.deleteAll(judicialRoleTypes);
        profileRepository.deleteAll(userProfiles);
    }
}
