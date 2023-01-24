package uk.gov.hmcts.reform.judicialapi.controller;


import uk.gov.hmcts.reform.judicialapi.elinks.domain.Appointment;
import uk.gov.hmcts.reform.judicialapi.elinks.domain.Authorisation;
import uk.gov.hmcts.reform.judicialapi.elinks.domain.BaseLocation;
import uk.gov.hmcts.reform.judicialapi.elinks.domain.Location;
import uk.gov.hmcts.reform.judicialapi.elinks.domain.UserProfile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;

public class TestSupport {

    private static final LocalDate date = LocalDate.now();
    private static final LocalDateTime dateTime = LocalDateTime.now();

    private TestSupport() {

    }

    public static Appointment createAppointment() {
        BaseLocation baseLocation = createBaseLocation();
        Location location = createLocation();

        Appointment appointment = new Appointment();
        appointment.setOfficeAppointmentId(1234567L);
        appointment.setBaseLocation(baseLocation);
        appointment.setLocation(location);
        appointment.setIsPrincipleAppointment(Boolean.TRUE);
        appointment.setStartDate(date);
        appointment.setStartDate(date);
        appointment.setEndDate(date);
        appointment.setCreatedDate(dateTime);
        appointment.setLastLoadedDate(dateTime);
        appointment.setUserProfile(new UserProfile());

        return appointment;
    }

    public static Authorisation createAuthorisation() {
        Authorisation authorisation = new Authorisation();
        authorisation.setOfficeAuthId(2L);
        authorisation.setJurisdiction("Languages");
        authorisation.setStartDate(dateTime);
        authorisation.setEndDate(dateTime);
        authorisation.setCreatedDate(dateTime);
        authorisation.setLastUpdated(dateTime);
        authorisation.setLowerLevel("Welsh");
        authorisation.setUserProfile(new UserProfile());

        return authorisation;
    }

    public static UserProfile createUserProfile() {
        UserProfile userProfile = new UserProfile();
        userProfile.setPersonalCode("personalCode");
        userProfile.setKnownAs("knownAs");
        userProfile.setSurname("surname");
        userProfile.setFullName("name");
        userProfile.setPostNominals("postNominals");
        userProfile.setEjudiciaryEmailId("emailId");
        userProfile.setActiveFlag(Boolean.TRUE);
        userProfile.setLastWorkingDate(date);
        userProfile.setLastLoadedDate(dateTime);
        userProfile.setCreatedDate(dateTime);
        userProfile.setObjectId("objectId");
        userProfile.setSidamId("sidamId");
        userProfile.setAppointments(Collections.singletonList(createAppointment()));
        userProfile.setAuthorisations(Collections.singletonList(createAuthorisation()));

        return userProfile;
    }

    public static BaseLocation createBaseLocation() {
        BaseLocation baseLocation = new BaseLocation();
        baseLocation.setBaseLocationId("0");
        baseLocation.setCourtName("Court Name");
        baseLocation.setCourtType("Court Type");
        baseLocation.setAppointments(Collections.singletonList(new Appointment()));
        baseLocation.setCircuit("Circuit");
        baseLocation.setAreaOfExpertise("Area of Expertise");
        return baseLocation;
    }


    public static Location createLocation() {
        Location location = new Location();
        location.setRegionId("0");
        location.setRegionDescEn("default");
        location.setRegionDescCy("default");

        return location;
    }

}
