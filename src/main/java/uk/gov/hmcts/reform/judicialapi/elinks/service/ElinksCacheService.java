package uk.gov.hmcts.reform.judicialapi.elinks.service;

import uk.gov.hmcts.reform.judicialapi.elinks.response.Customer;

public interface ElinksCacheService {

    Customer getCustomer(final Long customerID);
}
