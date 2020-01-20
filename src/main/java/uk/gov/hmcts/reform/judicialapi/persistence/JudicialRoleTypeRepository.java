package uk.gov.hmcts.reform.judicialapi.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.gov.hmcts.reform.judicialapi.domain.JudicialRoleType;

@Repository
public interface JudicialRoleTypeRepository extends JpaRepository<JudicialRoleType, String> {
}
