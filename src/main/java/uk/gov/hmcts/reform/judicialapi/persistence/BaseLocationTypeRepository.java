package uk.gov.hmcts.reform.judicialapi.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.gov.hmcts.reform.judicialapi.domain.BaseLocationType;

@Repository
public interface BaseLocationTypeRepository extends JpaRepository<BaseLocationType, String> {
}
