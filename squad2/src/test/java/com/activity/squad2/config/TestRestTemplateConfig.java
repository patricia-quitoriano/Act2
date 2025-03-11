package com.activity.squad2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

@Configuration
@Profile("test")
public class TestRestTemplateConfig {

    @Bean
    @Primary
    public RestOperations squadRestOperations() {
        return new RestTemplate();
    }
}