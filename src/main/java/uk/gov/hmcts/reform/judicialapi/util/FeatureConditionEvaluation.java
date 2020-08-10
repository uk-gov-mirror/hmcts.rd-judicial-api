package uk.gov.hmcts.reform.judicialapi.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import uk.gov.hmcts.reform.judicialapi.service.FeatureToggleService;

@Component
public class FeatureConditionEvaluation implements HandlerInterceptor {

    @Autowired
    FeatureToggleService featureToggleService;

    @Override
    public boolean preHandle(HttpServletRequest request
        , HttpServletResponse response, Object arg2) throws Exception {
        String feature = request.getHeader("feature");
        boolean flagStatus = featureToggleService.isFlagEnabled(feature);
        if(!flagStatus) {
            response.sendError(403, "Forbidden");
        }
        return flagStatus;
    }
}
