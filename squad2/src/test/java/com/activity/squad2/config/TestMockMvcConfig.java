package com.activity.squad2.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@TestConfiguration
@Profile("test")
public class TestMockMvcConfig {

    @Bean
    @Primary
    public MockMvc mockMvc(WebApplicationContext webApplicationContext) {
        return MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
}