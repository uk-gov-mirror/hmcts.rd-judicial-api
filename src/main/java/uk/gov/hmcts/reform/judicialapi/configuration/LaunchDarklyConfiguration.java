package uk.gov.hmcts.reform.judicialapi.configuration;

import com.launchdarkly.sdk.server.LDClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import uk.gov.hmcts.reform.judicialapi.util.FeatureConditionEvaluation;

@Configuration
public class LaunchDarklyConfiguration  implements WebMvcConfigurer {

    @Autowired
    FeatureConditionEvaluation featureConditionEvaluation;

    @Bean
    public LDClient ldClient(@Value("${ld.sdk_key}") String sdkKey) {
        return new LDClient(sdkKey);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(featureConditionEvaluation).addPathPatterns("/testLD");
        registry.addInterceptor(featureConditionEvaluation).addPathPatterns("/testLD1233");
    }
}
