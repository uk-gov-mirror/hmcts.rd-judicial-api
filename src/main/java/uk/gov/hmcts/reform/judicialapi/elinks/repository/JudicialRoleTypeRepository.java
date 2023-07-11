package uk.gov.hmcts.reform.judicialapi.elinks.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.gov.hmcts.reform.judicialapi.elinks.domain.JudicialRoleType;

@SuppressWarnings("all")
@Repository

public interface JudicialRoleTypeRepository extends JpaRepository<JudicialRoleType, String> {

    void deleteByPersonalCodeIn(List<String> personalCode);

}
