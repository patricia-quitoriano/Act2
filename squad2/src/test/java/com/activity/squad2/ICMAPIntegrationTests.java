package com.activity.squad2;

import com.activity.squad2.controller.ICMAPController;
import com.activity.squad2.model.User;
import com.activity.squad2.repository.UserRepository;
import com.activity.squad2.service.ICMAPService;
import com.activity.squad2.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ICMAPIntegrationTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ICMAPService icmapService;

    @InjectMocks
    private UserServiceImpl userService;

    private MockMvc mockMvc;
    private User testUser;

    @BeforeEach
    void setUp() {
        ICMAPController icmapController = new ICMAPController(userService, icmapService);
        mockMvc = MockMvcBuilders.standaloneSetup(icmapController).build();

        testUser = new User("Juan", "Dela Cruz", "Some Address", LocalDate.of(1990, 1, 1));
        testUser.setId(1L);

        // Create response data
        Map<String, Object> responseData = new HashMap<>();
        Map<String, String> innerData = new HashMap<>();
        innerData.put("name", "Juan Dela Cruz");
        innerData.put("status", "clear");
        responseData.put("status", "success");
        responseData.put("data", innerData);

        // Set up mocks
        when(userRepository.findByFirstNameAndLastNameIgnoreCase("Juan", "Dela Cruz"))
                .thenReturn(Optional.of(testUser));

        // Using doAnswer instead of thenReturn for the ICMAPService
        doAnswer(invocation -> {
            return new ResponseEntity<>(responseData, HttpStatus.OK);
        }).when(icmapService).getICMAPData(anyString(), anyString());
    }

    @Test
    public void testUserRetrieval() throws Exception {
        mockMvc.perform(get("/api/users/icmap")
                        .param("firstName", "Juan")
                        .param("lastName", "Dela Cruz")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}