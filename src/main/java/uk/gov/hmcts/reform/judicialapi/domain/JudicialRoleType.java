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

@Entity(name = "judicial_role_type")
@NoArgsConstructor
@Getter
@Setter
public class JudicialRoleType {

    @Id
    private String roleId;

    @Column(name = "ROLE_DESC_EN")
    @Size(max = 256)
    private String roleDescEn;

    @Column(name = "ROLE_DESC_CY")
    @Size(max = 256)
    private String roleDescCy;

    //@OneToMany(mappedBy = "judicialRoletype")
    //private List<JudicialOfficeAppointment> judicialOfficeAppointments = new ArrayList<>();

    public JudicialRoleType(String roleId, String roleDescEn, String roleDescCy) {
        this.roleId = roleId;
        this.roleDescEn = roleDescEn;
        this.roleDescCy = roleDescCy;
    }

    //public void addJudicialOfficeAppointment(JudicialOfficeAppointment judicialOfficeAppointment) { judicialOfficeAppointments.add(judicialOfficeAppointment); }

    //public List<JudicialOfficeAppointment> getJudicialOfficeAppointments() { return judicialOfficeAppointments; }
}
