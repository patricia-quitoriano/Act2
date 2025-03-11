package com.activity.squad2.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import com.activity.squad2.secrets.SecretProvider;

@TestConfiguration
@Profile("test")
@EnableAutoConfiguration(exclude = {
        DataSourceAutoConfiguration.class
})
public class TestConfig {

    @Bean
    @Primary
    public RestOperations restOperations() {
        return new RestTemplate();
    }

    @Bean
    @Primary
    public SecretProvider secretProvider() {
        return () -> "test-api-key";
    }
}