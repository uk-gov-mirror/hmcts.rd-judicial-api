package uk.gov.hmcts.reform.judicialapi.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.expression.method.ExpressionBasedPreInvocationAdvice;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import java.util.ArrayList;
import java.util.List;


@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class MethodSecurityConfiguration {
    @Autowired
    protected AccessDecisionManager accessDecisionManager(
            MethodSecurityExpressionHandler methodSecurityExpressionHandler) {
        List<AccessDecisionVoter<? extends Object>> decisionVoters
                = new ArrayList<AccessDecisionVoter<? extends Object>>();
        ExpressionBasedPreInvocationAdvice expressionAdvice = new ExpressionBasedPreInvocationAdvice();
        expressionAdvice.setExpressionHandler(methodSecurityExpressionHandler);
        RoleVoter voter = new RoleVoter();
        voter.setRolePrefix("");
        decisionVoters.add(voter);
        decisionVoters.add(new AuthenticatedVoter());
        return new AffirmativeBased(decisionVoters);
    }
}
