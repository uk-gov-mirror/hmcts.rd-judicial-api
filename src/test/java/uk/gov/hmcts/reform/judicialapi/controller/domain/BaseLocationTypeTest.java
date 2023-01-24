package uk.gov.hmcts.reform.judicialapi.controller.domain;

import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.judicialapi.domain.Appointment;
import uk.gov.hmcts.reform.judicialapi.domain.BaseLocationType;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class BaseLocationTypeTest {

    @Test
    void testBaseLocationType() {
        BaseLocationType baseLocationType = createBaseLocationType();

        assertNotNull(baseLocationType);
        assertNotNull(baseLocationType.getAppointments());
        assertEquals("0", baseLocationType.getBaseLocationId());
        assertEquals("Court Type", baseLocationType.getCourtType());
        assertEquals("Court Name", baseLocationType.getCourtName());
        assertEquals("Circuit", baseLocationType.getCircuit());
        assertEquals("Area of Expertise", baseLocationType.getAreaOfExpertise());
    }

    public static BaseLocationType createBaseLocationType() {
        BaseLocationType baseLocationType = new BaseLocationType();
        baseLocationType.setBaseLocationId("0");
        baseLocationType.setCourtName("Court Name");
        baseLocationType.setCourtType("Court Type");
        baseLocationType.setAppointments(Collections.singletonList(new Appointment()));
        baseLocationType.setCircuit("Circuit");
        baseLocationType.setAreaOfExpertise("Area of Expertise");
        return baseLocationType;
    }
}
