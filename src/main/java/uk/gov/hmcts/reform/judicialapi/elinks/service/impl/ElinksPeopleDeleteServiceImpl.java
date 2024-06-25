package uk.gov.hmcts.reform.judicialapi.elinks.service.impl;


import com.launchdarkly.shaded.com.google.common.collect.Lists;
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

import java.util.ArrayList;
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
        List<String> personalCodes = Lists.newArrayList(resultsRequest.getPersonalCode());
        auditAndDelete(personalCodes, false);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deletePeople(String personalCode) {
        log.info("Delete by personalCode");
        List<String> personalCodes = Lists.newArrayList(personalCode);
        auditAndDelete(personalCodes, true);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deletePeople(List<String> personalCodes) {
        log.info("Delete people by personal codes");
        // Get and persist into audit table
        auditAndDelete(personalCodes, true);
    }

    private void auditAndDelete(List<String> personalCodes, boolean deleteUserProfile) {
        try {
            List<Authorisation> authorisations = authorisationsRepository.findAllByPersonalCodeIn(personalCodes);
            List<Appointment> appointments = appointmentsRepository.findAllByPersonalCodeIn(personalCodes);
            List<JudicialRoleType> judicialRoleTypes = judicialRoleTypeRepository
                    .findAllByPersonalCodeIn(personalCodes);

            authorisationsRepository.deleteAll(authorisations);
            appointmentsRepository.deleteAll(appointments);
            judicialRoleTypeRepository.deleteAll(judicialRoleTypes);
            List<UserProfile> userProfiles = new ArrayList<>();
            if (deleteUserProfile) {
                userProfiles = profileRepository.findAllById(personalCodes);
                profileRepository.deleteAll(userProfiles);
            }
            elinksPeopleDeleteAuditService
                    .auditPeopleDelete(authorisations, appointments, judicialRoleTypes, userProfiles);
        } catch (Exception e) {
            log.error("Delete User Profile failed for personal codes {} error message: {} ",
                    personalCodes, e.getMessage());
            e.printStackTrace();
        }

    }
}
