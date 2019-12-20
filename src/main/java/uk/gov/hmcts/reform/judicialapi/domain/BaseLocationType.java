package uk.gov.hmcts.reform.judicialapi.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "base_location_type")
@NoArgsConstructor
@Getter
@Setter
public class BaseLocationType {

    @Id
    @Size(max = 64)
    private String baseLocationId;

    @Column(name = "COURT_NAME")
    @Size(max = 128)
    private String courtName;

    @Column(name = "BENCH")
    @Size(max = 128)
    private String bench;

    @Column(name = "COURT_TYPE")
    @Size(max = 128)
    private String courtType;

    @Column(name = "CIRCUIT")
    @Size(max = 128)
    private String circuit;

    @Column(name = "AREA_OF_EXPERTISE")
    @Size(max = 128)
    private String areaOfExpertise;

    @Column(name = "NATIONAL_COURT_CODE")
    @Size(max = 128)
    private String nationalCourtCode;

    @OneToMany(mappedBy = "baseLocationType")
    private List<JudicialOfficeAppointment> judicialOfficeAppointments = new ArrayList<>();

    public BaseLocationType(String baseLocationId, String courtName, String bench, String courtType,
                            String circuit, String areaOfExpertise, String nationalCourtCode) {
        this.baseLocationId = baseLocationId;
        this.courtName = courtName;
        this.bench = bench;
        this.courtType = courtType;
        this.circuit = circuit;
        this.areaOfExpertise = areaOfExpertise;
        this.nationalCourtCode = nationalCourtCode;
    }
}
