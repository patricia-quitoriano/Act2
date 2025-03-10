package com.activity.squad2.config;

import com.bpi.framework.security.policies.PolicyConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig {

    @Bean
    public PolicyConfig policyConfig() {
        return new DummyPolicyConfig();
    }

    private static class DummyPolicyConfig extends PolicyConfig {

        public String getPolicyName() {
            return "dummyPolicy";
        }
    }
}
