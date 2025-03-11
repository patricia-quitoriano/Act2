package com.activity.squad2.config;

import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collections;
import java.util.List;

@TestConfiguration
@Profile("test")
public class TestSecurityConfig {

    @Bean
    @Primary
    public SecurityFilterChain testSecurityFilterChain() {
        // Create a minimal implementation of SecurityFilterChain
        return new SecurityFilterChain() {
            @Override
            public boolean matches(HttpServletRequest request) {
                return true; // Match all requests
            }

            @Override
            public List<Filter> getFilters() {
                return Collections.emptyList(); // No filters
            }
        };
    }
}