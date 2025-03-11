package com.activity.squad2.service;

import com.activity.squad2.secrets.SecretProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
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

    private final String apiUrl = "http://localhost:3001/api/users";
    private final String apiKey = "test-api-key";
    private final String firstName = "John";
    private final String lastName = "Doe";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(icmapService, "icmapApiUrl", apiUrl);
        when(secretProvider.getICMAPApiKey()).thenReturn(apiKey);
    }

    @Test
    void testGetICMAPDataSuccess() {
        // Mock successful response
        Map<String, Object> mockResponseMap = new HashMap<>();
        mockResponseMap.put("status", "success");
        mockResponseMap.put("data", Map.of(
                "name", "John Doe",
                "status", "clear"
        ));

        when(restOperations.exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Map.class)
        )).thenReturn(ResponseEntity.ok(mockResponseMap));

        ResponseEntity<?> response = icmapService.getICMAPData(firstName, lastName);

        // Verify results
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());

        // Verify the correct URL and headers were used
        verify(restOperations).exchange(
                eq(apiUrl + "?q=" + firstName + lastName),
                eq(HttpMethod.GET),
                argThat(entity ->
                        entity.getHeaders().getFirst("X-API-KEY").equals(apiKey) &&
                                entity.getHeaders().getContentType().equals(MediaType.APPLICATION_JSON)
                ),
                eq(Map.class)
        );
    }

    @Test
    void testGetICMAPData_ClientError() {
        // Mock 4xx client error
        when(restOperations.exchange(
                anyString(),
                any(HttpMethod.class),
                any(HttpEntity.class),
                eq(Map.class)
        )).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        ResponseEntity<?> response = icmapService.getICMAPData(firstName, lastName);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());

        Map<String, String> errorBody = (Map<String, String>) response.getBody();
        assertTrue(errorBody.containsKey("error"));
    }

    @Test
    void testGetICMAPData_ServerError() {
        // Mock 5xx server error
        when(restOperations.exchange(
                anyString(),
                any(HttpMethod.class),
                any(HttpEntity.class),
                eq(Map.class)
        )).thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        ResponseEntity<?> response = icmapService.getICMAPData(firstName, lastName);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());

        Map<String, String> errorBody = (Map<String, String>) response.getBody();
        assertTrue(errorBody.containsKey("error"));
    }

    @Test
    void testGetICMAPData_GeneralException() {
        // Mock general exception
        when(restOperations.exchange(
                anyString(),
                any(HttpMethod.class),
                any(HttpEntity.class),
                eq(Map.class)
        )).thenThrow(new RestClientException("Connection refused"));

        ResponseEntity<?> response = icmapService.getICMAPData(firstName, lastName);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());

        Map<String, String> errorBody = (Map<String, String>) response.getBody();
        assertTrue(errorBody.containsKey("error"));
    }
}