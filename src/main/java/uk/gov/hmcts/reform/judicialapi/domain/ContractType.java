package uk.gov.hmcts.reform.judicialapi.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "contract_type")
@NoArgsConstructor
@Getter
@Setter
public class ContractType {

    @Id
    @Size(max = 64)
    private String contractTypeId;

    @Column(name = "CONTRACT_TYPE_DESC_EN")
    @Size(max = 256)
    private String contractTypeDescEn;

    @Column(name = "CONTRACT_TYPE_DESC_CY")
    @Size(max = 256)
    private String contractTypeDescCy;

    @OneToMany(mappedBy = "contractType")
    private List<JudicialOfficeAppointment> judicialOfficeAppointments = new ArrayList<>();

    public ContractType(String contractTypeId, String contractTypeDescEn, String contractTypeDescCy) {
        this.contractTypeId = contractTypeId;
        this.contractTypeDescEn = contractTypeDescEn;
        this.contractTypeDescCy = contractTypeDescCy;
    }
}
