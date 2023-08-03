package uk.gov.hmcts.reform.judicialapi.elinks.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.gov.hmcts.reform.judicialapi.elinks.domain.ServiceCodeMapping;

@Repository
public interface JudicialServiceCodeMappingRepository extends JpaRepository<ServiceCodeMapping, String> {

    List<ServiceCodeMapping> findByServiceCodeIgnoreCase(String serviceCode);
}
