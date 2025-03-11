package com.activity.squad2.controller;

import com.activity.squad2.model.User;
import com.activity.squad2.service.ICMAPService;
import com.activity.squad2.service.IUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    private MockMvc mockMvc;
    private MockMvc icmapMockMvc; // For ICMAP controller tests

    @Mock
    private IUserService userService;

    @Mock
    private ICMAPService icmapService;

    @InjectMocks
    private UserController userController;

    @InjectMocks
    private ICMAPController icmapController; // Added ICMAP controller

    private ObjectMapper objectMapper;

    private User testUser;
    private final Long userId = 1L;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules(); // For LocalDate serialization

        mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .build();

        // Setup the ICMAP controller for ICMAP endpoint tests
        icmapMockMvc = MockMvcBuilders
                .standaloneSetup(icmapController)
                .build();

        testUser = new User(userId, "John", "Doe", "123 Street", LocalDate.of(1990, 1, 1));
    }

    @Test
    void testCreateUser() throws Exception {
        when(userService.createUser(any(User.class))).thenReturn(testUser);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.lastName", is("Doe")));

        verify(userService).createUser(any(User.class));
    }

    @Test
    void testGetAllUsers() throws Exception {
        when(userService.getAllUsers()).thenReturn(Arrays.asList(testUser));

        mockMvc.perform(get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName", is("John")));

        verify(userService).getAllUsers();
    }

    @Test
    void testGetAllUsers_Empty() throws Exception {
        when(userService.getAllUsers()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(userService).getAllUsers();
    }

    @Test
    void testGetUserById_Found() throws Exception {
        when(userService.getUserById(userId)).thenReturn(Optional.of(testUser));

        mockMvc.perform(get("/api/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.lastName", is("Doe")));

        verify(userService).getUserById(userId);
    }

    @Test
    void testGetUserById_NotFound() throws Exception {
        when(userService.getUserById(userId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("User not found")));

        verify(userService).getUserById(userId);
    }

    @Test
    void testGetUserByName_Found() throws Exception {
        String firstName = "John";
        String lastName = "Doe";

        when(userService.getUserByName(firstName, lastName)).thenReturn(Optional.of(testUser));

        mockMvc.perform(get("/api/users/search")
                        .param("firstName", firstName)
                        .param("lastName", lastName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.lastName", is("Doe")));

        verify(userService).getUserByName(firstName, lastName);
    }

    @Test
    void testGetUserByName_NotFound() throws Exception {
        String firstName = "John";
        String lastName = "Doe";

        when(userService.getUserByName(firstName, lastName)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/search")
                        .param("firstName", firstName)
                        .param("lastName", lastName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("User not found")));

        verify(userService).getUserByName(firstName, lastName);
    }

    @Test
    void testGetICMAPData_UserExists() throws Exception {
        String firstName = "John";
        String lastName = "Doe";
        Map<String, Object> icmapData = Map.of(
                "status", "success",
                "data", Map.of("name", "John Doe", "status", "clear")
        );

        when(userService.getUserByName(firstName, lastName)).thenReturn(Optional.of(testUser));

        // Use doAnswer instead of thenReturn
        doAnswer(invocation -> {
            return new ResponseEntity<>(icmapData, HttpStatus.OK);
        }).when(icmapService).getICMAPData(firstName, lastName);

        // Use icmapMockMvc instead of mockMvc since the endpoint is now in ICMAPController
        icmapMockMvc.perform(get("/api/users/icmap")
                        .param("firstName", firstName)
                        .param("lastName", lastName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("success")))
                .andExpect(jsonPath("$.data.name", is("John Doe")));

        verify(userService).getUserByName(firstName, lastName);
        verify(icmapService).getICMAPData(firstName, lastName);
    }

    @Test
    void testGetICMAPData_UserNotFound() throws Exception {
        String firstName = "John";
        String lastName = "Doe";

        when(userService.getUserByName(firstName, lastName)).thenReturn(Optional.empty());

        // Use icmapMockMvc instead of mockMvc
        icmapMockMvc.perform(get("/api/users/icmap")
                        .param("firstName", firstName)
                        .param("lastName", lastName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("User not found")));

        verify(userService).getUserByName(firstName, lastName);
        verify(icmapService, never()).getICMAPData(anyString(), anyString());
    }

    @Test
    void testUpdateUser_Found() throws Exception {
        User updatedUser = new User(userId, "John", "Updated", "456 New Street", LocalDate.of(1991, 2, 2));

        when(userService.updateUser(eq(userId), any(User.class))).thenReturn(Optional.of(updatedUser));

        mockMvc.perform(put("/api/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName", is("Updated")))
                .andExpect(jsonPath("$.address", is("456 New Street")));

        verify(userService).updateUser(eq(userId), any(User.class));
    }

    @Test
    void testUpdateUser_NotFound() throws Exception {
        User updatedUser = new User(userId, "John", "Updated", "456 New Street", LocalDate.of(1991, 2, 2));

        when(userService.updateUser(eq(userId), any(User.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("User not found")));

        verify(userService).updateUser(eq(userId), any(User.class));
    }

    @Test
    void testDeleteUser_Success() throws Exception {
        when(userService.deleteUser(userId)).thenReturn(true);

        mockMvc.perform(delete("/api/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(userService).deleteUser(userId);
    }

    @Test
    void testDeleteUser_NotFound() throws Exception {
        when(userService.deleteUser(userId)).thenReturn(false);

        mockMvc.perform(delete("/api/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("User not found")));

        verify(userService).deleteUser(userId);
    }
}