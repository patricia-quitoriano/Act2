package com.activity.squad2.config;

import com.bpi.framework.web.configurer.client.RestApiConfigurerComponent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    private final RestApiConfigurerComponent apiConfigurer;
    private final CustomHttpApiConfigProperties apiConfigProperties;

    public RestTemplateConfig(RestApiConfigurerComponent apiConfigurer,
                              CustomHttpApiConfigProperties apiConfigProperties) {
        this.apiConfigurer = apiConfigurer;
        this.apiConfigProperties = apiConfigProperties;
    }

    /**
     * Exposes a fully configured RestTemplate (as RestOperations) for use in the application.
     * Here we use the apiConfigurer to apply our settings and interceptors from the properties.
     */
    @Bean
    public RestOperations squadRestOperations() {
        // In this example, we are not passing extra interceptors.
        // You could add interceptors as additional arguments if needed.
        return apiConfigurer.configure(apiConfigProperties, RestTemplate.class);
    }
}