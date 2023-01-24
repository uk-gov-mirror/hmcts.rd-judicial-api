package uk.gov.hmcts.reform.judicialapi.controller.domain;

import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.judicialapi.domain.Appointment;
import uk.gov.hmcts.reform.judicialapi.domain.BaseLocationType;
import uk.gov.hmcts.reform.judicialapi.domain.RegionType;
import uk.gov.hmcts.reform.judicialapi.domain.UserProfile;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.hmcts.reform.judicialapi.controller.domain.BaseLocationTypeTest.createBaseLocationType;
import static uk.gov.hmcts.reform.judicialapi.controller.domain.RegionTypeTest.createRegionType;

class AppointmentTest {

    private static final LocalDate date = LocalDate.now();
    private static final LocalDateTime dateTime = LocalDateTime.now();

    @Test
    void testAppointment() {
        Appointment appointment = createAppointment();

        assertNotNull(appointment);
        assertEquals(1234567L, appointment.getOfficeAppointmentId());
        assertTrue(appointment.getIsPrincipleAppointment());
        assertTrue(appointment.getActiveFlag());
        assertNotNull(appointment.getStartDate());
        assertNotNull(appointment.getEndDate());
        assertNotNull(appointment.getExtractedDate());
        assertNotNull(appointment.getCreatedDate());
        assertNotNull(appointment.getLastLoadedDate());
        assertNotNull(appointment.getUserProfile());
        assertNotNull(appointment.getRegionType());
        assertNotNull(appointment.getBaseLocationType());
    }

    public static Appointment createAppointment() {
        BaseLocationType baseLocationType = createBaseLocationType();
        RegionType regionType = createRegionType();

        Appointment appointment = new Appointment();
        appointment.setOfficeAppointmentId(1234567L);
        appointment.setBaseLocationType(baseLocationType);
        appointment.setRegionType(regionType);
        appointment.setIsPrincipleAppointment(Boolean.TRUE);
        appointment.setStartDate(date);
        appointment.setActiveFlag(Boolean.TRUE);
        appointment.setStartDate(date);
        appointment.setEndDate(date);
        appointment.setExtractedDate(dateTime);
        appointment.setCreatedDate(dateTime);
        appointment.setLastLoadedDate(dateTime);
        appointment.setUserProfile(new UserProfile());

        return appointment;
    }

}
