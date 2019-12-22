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

@Entity(name = "authorisation_type")
@NoArgsConstructor
@Getter
@Setter
public class AuthorisationType {

    @Id
    @Size(max = 64)
    private String authorisationType;

    @Column(name = "AUTHORISATION_DESC_EN")
    @Size(max = 256)
    private String authorisationDescEn;

    @Column(name = "AUTHORISATION_DESC_CY")
    @Size(max = 256)
    private String authorisationDescCy;

    @Column(name = "JURISDICTION_ID")
    @Size(max = 64)
    private String jurisdictionId;

    @Column(name = "JURISDICTION_DESC_EN")
    @Size(max = 256)
    private String jurisdictionDescEn;

    @Column(name = "JURISDICTION_DESC_CY")
    @Size(max = 256)
    private String jurisdictionDescCy;

    @OneToMany(mappedBy = "authorisationType")
    private List<JudicialOfficeAuthorisation> judicialOfficeAuthorisations = new ArrayList<>();

    public AuthorisationType(String authorisationType, String authorisationDescEn, String authorisationDescCy,
                             String jurisdictionId, String jurisdictionDescEn, String jurisdictionDescCy) {
        this.authorisationType = authorisationType;
        this.authorisationDescEn = authorisationDescEn;
        this.authorisationDescCy = authorisationDescCy;
        this.jurisdictionId = jurisdictionId;
        this.authorisationDescEn = jurisdictionDescEn;
        this.jurisdictionDescCy = jurisdictionDescCy;
    }
}
