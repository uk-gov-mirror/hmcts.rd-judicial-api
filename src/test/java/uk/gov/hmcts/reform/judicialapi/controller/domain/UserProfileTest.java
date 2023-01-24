package uk.gov.hmcts.reform.judicialapi.controller.domain;

import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.judicialapi.domain.Appointment;
import uk.gov.hmcts.reform.judicialapi.domain.Authorisation;
import uk.gov.hmcts.reform.judicialapi.domain.UserProfile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserProfileTest {

    private static final LocalDate date = LocalDate.now();
    private static final LocalDateTime dateTime = LocalDateTime.now();

    @Test
    void testUserProfile() {
        UserProfile userProfile = createUserProfile();

        assertNotNull(userProfile);
        assertNotNull(userProfile.getAppointments());
        assertNotNull(userProfile.getAuthorisations());
        assertNotNull(userProfile.getJoiningDate());
        assertNotNull(userProfile.getLastWorkingDate());
        assertNotNull(userProfile.getLastLoadedDate());
        assertNotNull(userProfile.getExtractedDate());
        assertNotNull(userProfile.getCreatedDate());
        assertEquals("1", userProfile.getPerId());
        assertEquals("personalCode", userProfile.getPersonalCode());
        assertEquals("knownAs", userProfile.getKnownAs());
        assertEquals("surname", userProfile.getSurname());
        assertEquals("name", userProfile.getFullName());
        assertEquals("postNominals", userProfile.getPostNominals());
        assertEquals("workPattern", userProfile.getWorkPattern());
        assertEquals("emailId", userProfile.getEjudiciaryEmailId());
        assertEquals(Boolean.TRUE, userProfile.getActiveFlag());
        assertEquals("objectId", userProfile.getObjectId());
        assertEquals("sidamId", userProfile.getSidamId());
    }

    public static UserProfile createUserProfile() {
        UserProfile userProfile = new UserProfile();
        userProfile.setPerId("1");
        userProfile.setPersonalCode("personalCode");
        userProfile.setKnownAs("knownAs");
        userProfile.setSurname("surname");
        userProfile.setFullName("name");
        userProfile.setPostNominals("postNominals");
        userProfile.setWorkPattern("workPattern");
        userProfile.setEjudiciaryEmailId("emailId");
        userProfile.setActiveFlag(Boolean.TRUE);
        userProfile.setJoiningDate(date);
        userProfile.setLastWorkingDate(date);
        userProfile.setLastLoadedDate(dateTime);
        userProfile.setExtractedDate(dateTime);
        userProfile.setCreatedDate(dateTime);
        userProfile.setObjectId("objectId");
        userProfile.setSidamId("sidamId");
        userProfile.setAppointments(Collections.singletonList(new Appointment()));
        userProfile.setAuthorisations(Collections.singletonList(new Authorisation()));
        return userProfile;
    }

}
