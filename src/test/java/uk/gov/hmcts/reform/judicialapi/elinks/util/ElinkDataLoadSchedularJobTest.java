package uk.gov.hmcts.reform.judicialapi.elinks.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import uk.gov.hmcts.reform.judicialapi.elinks.domain.DataLoadSchedularJob;
import uk.gov.hmcts.reform.judicialapi.elinks.repository.DataloadSchedularJobRepository;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;


 class ElinkDataLoadSchedularJobTest {

    @InjectMocks
    ElinkDataLoadSchedularJob dataLoadSchedularJob;

    @Spy
    private DataloadSchedularJobRepository dataloadSchedularJobRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @ParameterizedTest
    @CsvSource(value = "SUCCESS")
    void testSaveScheduleJobSuccess(String publishingStatus) {
        DataLoadSchedularJob schedularJob = new DataLoadSchedularJob();
        schedularJob.setId(1);
        schedularJob.setPublishingStatus(publishingStatus);
        schedularJob.setJobStartTime(LocalDateTime.now());
        schedularJob.setJobEndTime(LocalDateTime.now());

        when(dataloadSchedularJobRepository.findByJobStartTime(any())).thenReturn(schedularJob);
        when(dataloadSchedularJobRepository.save(any())).thenReturn(schedularJob);
        dataLoadSchedularJob.schedulerJobStatus(schedularJob.getPublishingStatus(),
            LocalDateTime.now(), LocalDateTime.now());

        verify(dataloadSchedularJobRepository, times(1)).save(any());

    }

    @ParameterizedTest
    @CsvSource(value = "FAILURE")
    void testSaveScheduleJobFailure(String publishingStatus) {
        DataLoadSchedularJob schedularJob = new DataLoadSchedularJob();
        schedularJob.setId(1);
        schedularJob.setPublishingStatus(publishingStatus);
        schedularJob.setJobStartTime(LocalDateTime.now());
        schedularJob.setJobEndTime(LocalDateTime.now());

        when(dataloadSchedularJobRepository.findByJobStartTime(any())).thenReturn(schedularJob);
        when(dataloadSchedularJobRepository.save(any())).thenReturn(schedularJob);
        dataLoadSchedularJob.schedulerJobStatus(schedularJob.getPublishingStatus(),
            LocalDateTime.now(), LocalDateTime.now());

        verify(dataloadSchedularJobRepository, times(1)).save(any());

    }


}
