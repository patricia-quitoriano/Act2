package com.activity.squad2.service;

import com.activity.squad2.secrets.SecretProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ICMAPServiceImpl implements ICMAPService {

    private final RestOperations restOperations;
    private final SecretProvider secretProvider;

    @Value("${icmp.api.url:http://localhost:3001/api/users}")
    private String icmapApiUrl;

    @Override
    public ResponseEntity<?> getICMAPData(String firstName, String lastName) {
        // Prepare the timestamp in yyyyMMddHHmm format
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));

        // Retrieve the API key from our abstract secret provider
        String apiKey = secretProvider.getICMAPApiKey();

        // Compute SHA-512 hash of the concatenated API key and timestamp, then Base64-encode it.
        String headerKey;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            byte[] hashBytes = digest.digest((apiKey + timestamp).getBytes(StandardCharsets.UTF_8));
            headerKey = Base64.getEncoder().encodeToString(hashBytes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error computing authentication header.");
        }

        // Build the target URL with the query parameter (firstName + " " + lastName)
        String queryParam = firstName + " " + lastName;
        String url = UriComponentsBuilder.fromHttpUrl(icmapApiUrl)
                .queryParam("q", queryParam)
                .toUriString();

        // Set up the HTTP header using the computed DBXAPI header key
        HttpHeaders headers = new HttpHeaders();
        headers.set("DBXAPI", headerKey);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Map> response = restOperations.exchange(url, HttpMethod.GET, entity, Map.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body("ICMAP service unavailable.");
        }
    }
}
