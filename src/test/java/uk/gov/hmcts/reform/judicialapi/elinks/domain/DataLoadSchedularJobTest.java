package uk.gov.hmcts.reform.judicialapi.elinks.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DataLoadSchedularJobTest {

    @Test
    void testSchedularJob() {
        DataLoadSchedularJob dataLoadSchedularJob = new DataLoadSchedularJob();
        dataLoadSchedularJob.setId(1);
        dataLoadSchedularJob.setJobStartTime(LocalDateTime.now());
        dataLoadSchedularJob.setJobEndTime(LocalDateTime.now());
        dataLoadSchedularJob.setPublishingStatus("SUCCESS");

        assertNotNull(dataLoadSchedularJob);
        assertThat(dataLoadSchedularJob.getId(), is(1));
        assertNotNull(dataLoadSchedularJob.getJobStartTime());
        assertNotNull(dataLoadSchedularJob.getJobEndTime());
        assertThat(dataLoadSchedularJob.getPublishingStatus(), is("SUCCESS"));
    }


}
