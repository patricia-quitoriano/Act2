package com.activity.squad2;

import com.activity.squad2.config.SecurityConfig;
import com.activity.squad2.model.User;
import com.activity.squad2.repository.UserRepository;
import com.activity.squad2.service.ICMAPService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.transaction.Transactional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {Application.class, SecurityConfig.class})
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class ICMAPIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private ICMAPService icmapService;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        // Create test user with the same name we'll use in the test
        User testUser = new User();
        testUser.setFirstName("Juan");
        testUser.setLastName("Dela Cruz");
        userRepository.save(testUser);

        // Create a ResponseEntity with a wildcard type to match the service method return type
        @SuppressWarnings("unchecked")
        ResponseEntity<Object> mockResponse = new ResponseEntity<>(
                "{ \"status\": \"success\", \"data\": { \"name\": \"Juan Dela Cruz\", \"status\": \"clear\" } }",
                HttpStatus.OK
        );

        when(icmapService.getICMAPData(eq("Juan"), eq("Dela Cruz")))
                .thenReturn((ResponseEntity) mockResponse);
    }

    @Test
    public void testUserRetrieval() throws Exception {
        mockMvc.perform(get("/api/users/icmap")
                        .param("firstName", "Juan")
                        .param("lastName", "Dela Cruz")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()); // Expect 200 OK
    }
}