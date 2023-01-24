package uk.gov.hmcts.reform.judicialapi.elinks.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity(name = "dataloadSchedularJob")
@Table(name = "dataload_schedular_job", schema = "dbjudicialdata")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = "elink_job_scheduler_id_sequence",
    sequenceName = "elink_job_scheduler_id_sequence", schema = "dbjudicialdata", allocationSize = 1)
public class DataLoadSchedularJob implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "elink_job_scheduler_id_sequence")
    @Column(name = "id")
    private int id;

    @Column(name = "publishing_status")
    private String publishingStatus;

    @Column(name = "job_start_time")
    private LocalDateTime jobStartTime;

    @Column(name = "job_end_time")
    private LocalDateTime jobEndTime;

}
