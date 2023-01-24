package uk.gov.hmcts.reform.judicialapi.elinks.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.hmcts.reform.judicialapi.elinks.domain.DataLoadSchedularJob;
import uk.gov.hmcts.reform.judicialapi.elinks.repository.DataloadSchedularJobRepository;

import java.time.LocalDateTime;

import static java.util.Objects.nonNull;

@Component
@Slf4j
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class ElinkDataLoadSchedularJob {
    @Value("${loggingComponentName}")
    private String loggingComponentName;

    @Autowired
    private DataloadSchedularJobRepository dataloadSchedularJobRepository;


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void schedulerJobStatus(String publishingStatus, LocalDateTime jobStartTime, LocalDateTime jobEndTime) {

        DataLoadSchedularJob schedularJob = new DataLoadSchedularJob();
        try {
            if (nonNull(jobEndTime)) {
                schedularJob = dataloadSchedularJobRepository.findByJobStartTime(jobStartTime);
                schedularJob.setJobEndTime(jobEndTime);
            }
            schedularJob.setPublishingStatus(publishingStatus);
            schedularJob.setJobStartTime(jobStartTime);
            schedularJob.setJobEndTime(jobEndTime);


            dataloadSchedularJobRepository.save(schedularJob);
        } catch (Exception e) {
            log.error("{}:: Failure error Message {} in auditSchedulerStatus {}  ",
                loggingComponentName, e.getMessage(), publishingStatus);
        }

    }

}
