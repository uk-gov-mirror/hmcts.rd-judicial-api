package uk.gov.hmcts.reform.judicialapi.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Entity(name = "judicial_office_authorisation")
@NoArgsConstructor
@Getter
@Setter
public class JudicialOfficeAuthorisation {

    @Id
    private long judicialOfficeAuthId;

    @ManyToOne
    @JoinColumn(name = "ELINKS_ID")
    private JudicialUserProfile judicialUserProfile;

    @ManyToOne
    @JoinColumn(name = "AUTHORISATION_ID")
    private AuthorisationType authorisationType;

    @Column(name = "JURISDICTION_ID")
    @Size(max = 256)
    private String jurisdictionId;

    @Column(name = "AUTHORISATION_DATE")
    private LocalDate authorisationDate;

    @Column(name = "EXTRACTED_DATE")
    private LocalDate extractedDate;

    @Column(name = "CREATED_DATE")
    @CreationTimestamp
    private LocalDate createdDate;

    @Column(name = "LAST_LOADED_DATE")
    @UpdateTimestamp
    private LocalDate lastLoadedDate;

    public JudicialOfficeAuthorisation(long judicialOfficeAuthId, JudicialUserProfile judicialUserProfile,
                                       AuthorisationType authorisationType, String jurisdictionId,
                                       LocalDate authorisationDate, LocalDate extractedDate, LocalDate createdDate,
                                       LocalDate lastLoadedDate) {
        this.judicialOfficeAuthId = judicialOfficeAuthId;
        this.judicialUserProfile = judicialUserProfile;
        this.authorisationType = authorisationType;
        this.jurisdictionId = jurisdictionId;
        this.authorisationDate = authorisationDate;
        this.extractedDate = extractedDate;
        this.createdDate = createdDate;
        this.lastLoadedDate = lastLoadedDate;
    }
}
