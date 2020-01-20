package uk.gov.hmcts.reform.judicialapi.domain;

import java.time.LocalDateTime;
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
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity(name = "judicial_user_profile")
@NoArgsConstructor
@Getter
@Setter
public class JudicialUserProfile {

    @Id
    @Size(max = 256)
    private String elinksId;

    @Column(name = "PERSONAL_CODE")
    @Size(max = 32)
    private String personalCode;

    @Column(name = "TITLE")
    @Size(max = 64)
    private String title;

    @Column(name = "KNOWN_AS")
    @Size(max = 64)
    private String knownAs;

    @Column(name = "SURNAME")
    @Size(max = 256)
    private String surname;

    @Column(name = "FULL_NAME")
    @Size(max = 256)
    private String fullName;

    @Column(name = "POST_NOMINALS")
    @Size(max = 32)
    private String postNominals;

    @Column(name = "CONTRACT_TYPE")
    @Size(max = 32)
    private String contractType;

    @Column(name = "WORK_PATTERN")
    @Size(max = 32)
    private String workPattern;

    @Column(name = "EMAIL_ID")
    @Size(max = 256)
    private String emailId;

    @Column(name = "JOINING_DATE")
    private LocalDateTime joiningDate;

    @Column(name = "LAST_WORKING_DATE")
    private LocalDateTime lastWorkingDate;

    @Column(name = "ACTIVE_FLAG")
    private boolean activeFlag;

    @Column(name = "EXTRACTED_DATE")
    private LocalDateTime extractedDate;

    @CreationTimestamp
    @Column(name = "CREATED_DATE")
    private LocalDateTime createdDate;

    @UpdateTimestamp
    @Column(name = "LAST_LOADED_DATE")
    private LocalDateTime lastLoadedDate;

    @OneToMany(mappedBy = "judicialUserProfile")
    private List<JudicialOfficeAuthorisation> judicialOfficeAuthorisations = new ArrayList<>();

    @OneToMany(mappedBy = "judicialUserProfile")
    private List<JudicialOfficeAppointment> judicialOfficeAppointments = new ArrayList<>();

    public JudicialUserProfile(String elinksId, String personalCode, String title, String knownAs,
                               String surname, String fullName, String postNominals, String contractType,
                               String workPattern, String emailId, LocalDateTime joiningDate, LocalDateTime lastWorkingDate,
                               boolean activeFlag, LocalDateTime extractedDate, LocalDateTime createdDate, LocalDateTime lastLoadedDate) {
        this.elinksId = elinksId;
        this.personalCode = personalCode;
        this.title = title;
        this.knownAs = knownAs;
        this.surname = surname;
        this.fullName = fullName;
        this.postNominals = postNominals;
        this.contractType = contractType;
        this.workPattern = workPattern;
        this.emailId = emailId;
        this.joiningDate = joiningDate;
        this.lastWorkingDate = lastWorkingDate;
        this.activeFlag = activeFlag;
        this.extractedDate = extractedDate;
        this.createdDate = createdDate;
        this.lastLoadedDate = lastLoadedDate;
    }

    public void addJudicialOfficeAuthorisation(JudicialOfficeAuthorisation judicialOfficeAuthorisation) {
        judicialOfficeAuthorisations.add(judicialOfficeAuthorisation);
    }

    public List<JudicialOfficeAuthorisation> getJudicialOfficeAuthorisations() {
        return judicialOfficeAuthorisations;
    }

    public void addJudicialOfficeAppointment(JudicialOfficeAppointment judicialOfficeAppointment) {
        judicialOfficeAppointments.add(judicialOfficeAppointment);
    }

    public List<JudicialOfficeAppointment> getJudicialOfficeAppointments() {
        return judicialOfficeAppointments;
    }


}
