package uk.gov.hmcts.reform.judicialapi.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.gov.hmcts.reform.judicialapi.elinks.domain.Appointment;
import uk.gov.hmcts.reform.judicialapi.elinks.domain.Location;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang.StringUtils.EMPTY;

@Getter
@Setter
@NoArgsConstructor
public class AppointmentResponse {

    @JsonProperty
    private String appointmentId;
    @JsonProperty
    private String roleId;
    @JsonProperty
    private String roleDescEn;
    @JsonProperty
    private String contractTypeId;
    @JsonProperty
    private String contractTypeDescEn;
    @JsonProperty
    private String baseLocationId;
    @JsonProperty
    private String regionId;
    @JsonProperty
    private String regionDescEn;
    @JsonProperty
    private String isPrincipalAppointment;
    @JsonProperty
    private String startDate;
    @JsonProperty
    private String endDate;

    public AppointmentResponse(Appointment appointment) {
        
        if (nonNull(appointment)) {
            this.appointmentId = String.valueOf(appointment.getOfficeAppointmentId());
            this.baseLocationId = isNull(appointment.getBaseLocation()) ? EMPTY :
                appointment.getBaseLocation().getBaseLocationId();
            this.isPrincipalAppointment = isNull(appointment.getIsPrincipleAppointment()) ? EMPTY :
                appointment.getIsPrincipleAppointment().toString();
            this.startDate = isNull(appointment.getStartDate()) ? EMPTY : appointment.getStartDate().toString();
            this.endDate = isNull(appointment.getEndDate()) ? EMPTY : appointment.getEndDate().toString();

            validateNonNull(appointment.getLocation());
        }
    }

    public void validateNonNull(Location location) {

        if (nonNull(location)) {
            this.regionId = location.getRegionId();
            this.regionDescEn = location.getRegionDescEn();
        }
    }
}
