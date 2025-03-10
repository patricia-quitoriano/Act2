package com.activity.squad2.config;

import com.bpi.framework.security.policies.PolicyConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PolicyConfigConfiguration {

    @Bean
    public PolicyConfig policyConfig() {
        return new PolicyConfig();
    }
}
