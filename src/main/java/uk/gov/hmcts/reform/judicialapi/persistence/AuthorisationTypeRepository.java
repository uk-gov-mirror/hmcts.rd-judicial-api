package uk.gov.hmcts.reform.judicialapi.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.gov.hmcts.reform.judicialapi.domain.AuthorisationType;

@Repository
public interface AuthorisationTypeRepository extends JpaRepository<AuthorisationType, String> {
}
