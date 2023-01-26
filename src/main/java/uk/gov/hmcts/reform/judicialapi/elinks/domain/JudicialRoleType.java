package uk.gov.hmcts.reform.judicialapi.elinks.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity(name = "judicialRoleType")
@Table(name = "judicial_role_type", schema = "dbjudicialdata")
@Getter
@Setter
@NoArgsConstructor
public class JudicialRoleType implements Serializable {

    @Id
    @Column(name = "role_id")
    private String roleId;

    @Column(name = "personal_code")
    private String personalCode;

    @Column(name = "title")
    private String title;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "personal_code", referencedColumnName = "personal_code",
        insertable = false, updatable = false, nullable = false)
    private UserProfile userProfile;

}

