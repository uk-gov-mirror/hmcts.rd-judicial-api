package uk.gov.hmcts.reform.judicialapi.elinks.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.judicialapi.elinks.response.Customer;
import uk.gov.hmcts.reform.judicialapi.elinks.service.ElinksCacheService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
@Service
//@CacheConfig(cacheNames = {"customer"})
@CacheConfig(cacheNames = {"userProfileCacheManager"})

@Slf4j
public class ElinksCacheServiceImpl implements ElinksCacheService {

    @Cacheable
    @Override
    public Customer getCustomer(Long customerID) {
        log.info("Trying to get customer information for id {} ",customerID);
        return getCustomerData(customerID);
    }

    private Customer getCustomerData(final Long id){
        Customer customer = new Customer(id, "testemail@test.com", "Test Customer");
        return  customer;
    }
}
