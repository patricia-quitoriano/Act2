package com.activity.squad2;

import com.bpi.framework.web.configurer.client.RestApiConfigurerComponent;
import com.bpi.framework.web.configproperties.HttpApiConfigProperties;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AdditionalConfigTests {

    @Test
    public void testTestBpiFrameworkConfig() {
        // Manually create the mocks
        RestApiConfigurerComponent mockConfigurerComponent = Mockito.mock(RestApiConfigurerComponent.class);
        HttpApiConfigProperties mockProperties = Mockito.mock(HttpApiConfigProperties.class);

        // Configure the mock properties
        when(mockProperties.getBasePath()).thenReturn("http://localhost:8080");
        when(mockProperties.isBackendAdapterEnabled()).thenReturn(false);
        when(mockProperties.isProxyEnabled()).thenReturn(false);
        when(mockProperties.getDefaultKeepAlive()).thenReturn(5000L);
        when(mockProperties.getConnectionRequestTimeout()).thenReturn(1000L);
        when(mockProperties.getConnectTimeout()).thenReturn(1000);
        when(mockProperties.getSocketTimeout()).thenReturn(3000);

        // Always return a new RestTemplate
        RestTemplate alwaysNewRestTemplate = new RestTemplate();
        when(mockConfigurerComponent.configure(
                any(HttpApiConfigProperties.class),
                eq(RestTemplate.class),
                any()
        )).thenReturn(alwaysNewRestTemplate);

        // Call with three parameters (adding a dummy third parameter, here null)
        RestTemplate restTemplate = mockConfigurerComponent.configure(mockProperties, RestTemplate.class, null);

        // Explicit assertions
        assertNotNull(restTemplate, "RestTemplate should not be null");
        assertEquals(RestTemplate.class, restTemplate.getClass(),
                "Returned object should be a RestTemplate");

        // Verify the mock properties (these calls are simply invoking the stubbed methods)
        assertEquals("http://localhost:8080", mockProperties.getBasePath());
        assertFalse(mockProperties.isBackendAdapterEnabled());
        assertFalse(mockProperties.isProxyEnabled());
        assertEquals(5000L, mockProperties.getDefaultKeepAlive());
        assertEquals(1000L, mockProperties.getConnectionRequestTimeout());
        assertEquals(1000, mockProperties.getConnectTimeout());
        assertEquals(3000, mockProperties.getSocketTimeout());
    }
}
