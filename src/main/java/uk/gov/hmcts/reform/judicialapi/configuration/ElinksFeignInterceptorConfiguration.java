package uk.gov.hmcts.reform.judicialapi.configuration;

import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import uk.gov.hmcts.reform.judicialapi.util.RefDataConstants;

@Slf4j
public class ElinksFeignInterceptorConfiguration {

    @Value("${loggingComponentName}")
    private String loggingComponentName;

    @Value("${elinksApiKey}")
    private String elinksApiKey;

    @Value("${LD_SDK_KEY}")
    private String LD_SDK_KEY;




    @Bean
    public RequestInterceptor requestInterceptor(FeignHeaderConfig config) {

        return requestTemplate -> {
            log.error("elinksApiKey  :" + elinksApiKey);
            log.error("LD_SDK_KEY  :" + LD_SDK_KEY);
            requestTemplate.header(RefDataConstants.AUTHORIZATION, "Token " + elinksApiKey);
            requestTemplate.header("Content-Type", "application/json");
        };
    }
}
