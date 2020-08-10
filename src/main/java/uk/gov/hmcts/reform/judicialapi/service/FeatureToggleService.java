package uk.gov.hmcts.reform.judicialapi.service;

import com.launchdarkly.sdk.LDUser;
import com.launchdarkly.sdk.LDValue;
import com.launchdarkly.sdk.server.LDClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FeatureToggleService {

    @Autowired
    private final LDClient ldClient;
    private final String ldUserKey;

    @Autowired
    public FeatureToggleService(LDClient ldClient, @Value("${ld.user_key}") String ldUserKey) {
        this.ldClient = ldClient;
        this.ldUserKey = ldUserKey;
    }

    public boolean isFlagEnabled(String flag) {
        LDUser user = new LDUser.Builder("UNIQUE IDENTIFIER")
            .firstName("Bob")
            .lastName("Loblaw")
            .custom("groups", LDValue.buildArray().add("beta_testers").build())
            .build();
        boolean isFlagDisabled = ldClient.isOffline();
        boolean showFeature = true;
        if(!isFlagDisabled) {
             showFeature = ldClient.boolVariation(flag, user, false);
        }



        if (showFeature) {
            System.out.println("Showing your feature");
        } else {
            System.out.println("Not showing your feature");
        }
        return showFeature;
    }

}
