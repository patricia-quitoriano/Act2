package com.activity.squad2.config;

import com.bpi.framework.security.policies.PolicyConfig;
import com.bpi.framework.web.configproperties.HttpApiConfigProperties;
import com.bpi.framework.web.configurer.client.RestApiConfigurerComponent;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

/**
 * Test configuration that uses Spring's MockBean to create mocks for external dependencies
 */
@TestConfiguration
@Profile("test")
public class TestBpiFrameworkConfig {

    // Use MockBean to create mocks of the external classes rather than
    // trying to implement them directly
    @MockBean
    private RestApiConfigurerComponent restApiConfigurerComponent;

    @MockBean
    private HttpApiConfigProperties httpApiConfigProperties;

    @MockBean
    private PolicyConfig policyConfig;

    @Bean
    @Primary
    public RestApiConfigurerComponent mockRestApiConfigurerComponent() {
        // Configure the mock to return a RestTemplate when configure is called with any parameters
        Mockito.when(restApiConfigurerComponent.configure(
                Mockito.any(HttpApiConfigProperties.class),
                Mockito.any(Class.class),
                Mockito.any())
        ).thenReturn(new RestTemplate());

        return restApiConfigurerComponent;
    }

    @Bean
    @Primary
    public HttpApiConfigProperties mockHttpApiConfigProperties() {
        // Configure common properties
        Mockito.when(httpApiConfigProperties.getBasePath()).thenReturn("http://localhost:8080");
        Mockito.when(httpApiConfigProperties.isBackendAdapterEnabled()).thenReturn(false);
        Mockito.when(httpApiConfigProperties.isProxyEnabled()).thenReturn(false);
        Mockito.when(httpApiConfigProperties.getDefaultKeepAlive()).thenReturn(5000L);
        Mockito.when(httpApiConfigProperties.getConnectionRequestTimeout()).thenReturn(1000L);
        Mockito.when(httpApiConfigProperties.getConnectTimeout()).thenReturn(1000);
        Mockito.when(httpApiConfigProperties.getSocketTimeout()).thenReturn(3000);

        return httpApiConfigProperties;
    }

    @Bean
    @Primary
    public PolicyConfig mockPolicyConfig() {
        return policyConfig;
    }
}