package uk.gov.hmcts.reform.judicialapi.elinks.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.gov.hmcts.reform.judicialapi.elinks.domain.DataLoadSchedularJob;

import java.time.LocalDateTime;


@Repository
public interface DataloadSchedularJobRepository extends JpaRepository<DataLoadSchedularJob, Long> {

    DataLoadSchedularJob findByJobStartTime(LocalDateTime jobStartTime);
}
