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

@Entity(name = "region_type")
@NoArgsConstructor
@Getter
@Setter
public class RegionType {

    @Id
    @Size(max = 64)
    private String regionId;

    @Column(name = "REGION_DESC_EN")
    @Size(max = 256)
    private String regionDescEn;

    @Column(name = "REGION_DESC_CY")
    @Size(max = 256)
    private String regionDescCy;

    @OneToMany(mappedBy = "regionType")
    private List<JudicialOfficeAppointment> judicialOfficeAppointments = new ArrayList<>();

    public RegionType(String regionId, String regionDescEn, String regionDescCy) {
        this.regionId = regionId;
        this.regionDescEn = regionDescEn;
        this.regionDescCy = regionDescCy;
    }
}
