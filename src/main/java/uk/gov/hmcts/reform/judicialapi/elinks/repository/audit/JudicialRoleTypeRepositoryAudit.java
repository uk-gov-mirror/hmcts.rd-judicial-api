package uk.gov.hmcts.reform.judicialapi.elinks.repository.audit;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.gov.hmcts.reform.judicialapi.elinks.domain.audit.JudicialRoleType;

@SuppressWarnings("all")
@Repository

public interface JudicialRoleTypeRepositoryAudit extends JpaRepository<JudicialRoleType, String> {

}
