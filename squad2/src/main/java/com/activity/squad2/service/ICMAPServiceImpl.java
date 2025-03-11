package com.activity.squad2.service;

import com.activity.squad2.secrets.SecretProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;

import java.util.HashMap;
import java.util.Map;

@Service
public class ICMAPServiceImpl implements ICMAPService {

    @Value("${icmap.api.url}")
    private String icmapApiUrl;

    private final RestOperations restOperations;
    private final SecretProvider secretProvider;

    @Autowired
    public ICMAPServiceImpl(RestOperations restOperations, SecretProvider secretProvider) {
        this.restOperations = restOperations;
        this.secretProvider = secretProvider;
    }

    @Override
    public ResponseEntity<?> getICMAPData(String firstName, String lastName) {
        // Validate input
        if (firstName == null || lastName == null ||
                firstName.trim().isEmpty() || lastName.trim().isEmpty()) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid input parameters");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("X-API-KEY", secretProvider.getICMAPApiKey());

            HttpEntity<?> entity = new HttpEntity<>(headers);

            // Fixed URL format to use firstName and lastName as separate parameters
            String url = icmapApiUrl + "?q=" + firstName + lastName;

            return restOperations.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    Map.class
            );
        } catch (HttpClientErrorException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Client error: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, e.getStatusCode());
        } catch (HttpServerErrorException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Server error: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, e.getStatusCode());
        } catch (RestClientException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Service error: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}