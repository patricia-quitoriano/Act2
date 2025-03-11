package com.activity.squad2.config;

import com.bpi.framework.web.configurer.client.RestApiConfigurerComponent;
import com.bpi.framework.web.configproperties.HttpApiConfigProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    private final RestApiConfigurerComponent restApiConfigurerComponent;
    private final HttpApiConfigProperties httpApiConfigProperties;

    public RestTemplateConfig(RestTemplateBuilder restTemplateBuilder, HttpApiConfigProperties httpApiConfigProperties, HttpApiConfigProperties httpApiConfigProperties1) {
        this.httpApiConfigProperties = httpApiConfigProperties;
        this.restApiConfigurerComponent = new RestApiConfigurerComponent();
    }

    @Bean
    public RestOperations restOperations() {
        // Use the configure() method to create a RestTemplate
        return restApiConfigurerComponent.configure(httpApiConfigProperties, RestTemplate.class);
    }
}
