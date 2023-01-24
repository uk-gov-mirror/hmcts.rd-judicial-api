package uk.gov.hmcts.reform.judicialapi.controller.domain;

import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.judicialapi.domain.Appointment;
import uk.gov.hmcts.reform.judicialapi.domain.RegionType;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RegionTypeTest {

    @Test
    void testRegionType() {
        RegionType regionType = createRegionType();

        assertNotNull(regionType);
        assertNotNull(regionType.getAppointments());
        assertEquals("0", regionType.getRegionId());
        assertEquals("default", regionType.getRegionDescEn());
        assertEquals("default", regionType.getRegionDescCy());
    }

    public static RegionType createRegionType() {
        RegionType regionType = new RegionType();
        regionType.setRegionId("0");
        regionType.setRegionDescEn("default");
        regionType.setRegionDescCy("default");
        regionType.setAppointments(Collections.singletonList(new Appointment()));

        return regionType;
    }
}
