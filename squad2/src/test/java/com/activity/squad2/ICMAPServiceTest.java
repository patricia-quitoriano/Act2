package com.activity.squad2.service;

import com.activity.squad2.secrets.SecretProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestOperations;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ICMAPServiceTest {

    @Mock
    private RestOperations restOperations;

    @Mock
    private SecretProvider secretProvider;

    @InjectMocks
    private ICMAPServiceImpl icmapService;

    @Test
    void testGetICMAPDataSuccess() {
        // Set the API URL via Reflection
        ReflectionTestUtils.setField(icmapService, "icmapApiUrl", "http://localhost:3001/api/users");
        when(secretProvider.getICMAPApiKey()).thenReturn("test-api-key");

        Map<String, Object> mockResponseMap = Map.of(
                "status", "success",
                "data", Map.of("name", "John Doe", "status", "clear")
        );

        when(restOperations.exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Map.class)
        )).thenReturn(ResponseEntity.ok(mockResponseMap));

        ResponseEntity<?> response = icmapService.getICMAPData("John", "Doe");

        assertEquals(200, response.getStatusCodeValue());
        verify(restOperations, times(1)).exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Map.class)
        );
    }
}
