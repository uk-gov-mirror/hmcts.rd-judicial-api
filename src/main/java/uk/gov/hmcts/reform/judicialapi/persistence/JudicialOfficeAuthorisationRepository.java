package uk.gov.hmcts.reform.judicialapi.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.gov.hmcts.reform.judicialapi.domain.JudicialOfficeAuthorisation;

@Repository
public interface JudicialOfficeAuthorisationRepository extends JpaRepository<JudicialOfficeAuthorisation, Long> {
}
