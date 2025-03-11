package com.activity.squad2;

import com.activity.squad2.model.User;
import com.activity.squad2.repository.UserRepository;
import com.activity.squad2.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ApplicationIntegrationTests {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserServiceImpl userService;

	private MockMvc mockMvc;

	@Mock
	private WebApplicationContext context;

	private User testUser;
	private final Long userId = 1L;

	@BeforeEach
	public void setUp() {
		mockMvc = MockMvcBuilders
				.standaloneSetup(new com.activity.squad2.controller.UserController(userService, null))
				.build();

		testUser = new User(userId, "Juan", "Dela Cruz", "123 Sample St", LocalDate.of(1990, 5, 15));
	}

	@Test
	public void testCreateUser() throws Exception {
		when(userRepository.save(any(User.class))).thenReturn(testUser);

		mockMvc.perform(post("/api/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"firstName\":\"Juan\",\"lastName\":\"Dela Cruz\",\"address\":\"123 Sample St\",\"birthday\":\"1990-05-15\"}"))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.firstName", is("Juan")))
				.andExpect(jsonPath("$.lastName", is("Dela Cruz")));

		verify(userRepository).save(any(User.class));
	}

	@Test
	public void testGetAllUsers() throws Exception {
		List<User> users = new ArrayList<>();
		users.add(testUser);

		when(userRepository.findAll()).thenReturn(users);

		mockMvc.perform(get("/api/users")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].firstName", is("Juan")));

		verify(userRepository).findAll();
	}

	@Test
	public void testGetUserById() throws Exception {
		when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));

		mockMvc.perform(get("/api/users/" + userId)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.firstName", is("Juan")))
				.andExpect(jsonPath("$.lastName", is("Dela Cruz")));

		verify(userRepository).findById(userId);
	}

	@Test
	public void testGetUserById_NotFound() throws Exception {
		when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

		mockMvc.perform(get("/api/users/999")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.error", is("User not found")));

		verify(userRepository).findById(anyLong());
	}

	@Test
	public void testGetUserByName() throws Exception {
		when(userRepository.findByFirstNameAndLastNameIgnoreCase("Juan", "Dela Cruz"))
				.thenReturn(Optional.of(testUser));

		mockMvc.perform(get("/api/users/search")
						.param("firstName", "Juan")
						.param("lastName", "Dela Cruz")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.firstName", is("Juan")))
				.andExpect(jsonPath("$.lastName", is("Dela Cruz")));

		verify(userRepository).findByFirstNameAndLastNameIgnoreCase("Juan", "Dela Cruz");
	}

	@Test
	public void testGetUserByName_NotFound() throws Exception {
		when(userRepository.findByFirstNameAndLastNameIgnoreCase(anyString(), anyString()))
				.thenReturn(Optional.empty());

		mockMvc.perform(get("/api/users/search")
						.param("firstName", "Nonexistent")
						.param("lastName", "User")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.error", is("User not found")));

		verify(userRepository).findByFirstNameAndLastNameIgnoreCase(anyString(), anyString());
	}

	@Test
	public void testUpdateUser() throws Exception {
		User updatedUser = new User(userId, "Juan", "Updated", "999 New St", LocalDate.of(1990, 5, 15));

		when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
		when(userRepository.save(any(User.class))).thenReturn(updatedUser);

		mockMvc.perform(put("/api/users/" + userId)
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"firstName\":\"Juan\",\"lastName\":\"Updated\",\"address\":\"999 New St\",\"birthday\":\"1990-05-15\"}"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.lastName", is("Updated")))
				.andExpect(jsonPath("$.address", is("999 New St")));

		verify(userRepository).findById(userId);
		verify(userRepository).save(any(User.class));
	}

	@Test
	public void testUpdateUser_NotFound() throws Exception {
		when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

		mockMvc.perform(put("/api/users/999")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"firstName\":\"Juan\",\"lastName\":\"Updated\",\"address\":\"999 New St\",\"birthday\":\"1990-05-15\"}"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.error", is("User not found")));

		verify(userRepository).findById(anyLong());
		verify(userRepository, never()).save(any(User.class));
	}

	@Test
	public void testDeleteUser() throws Exception {
		when(userRepository.existsById(userId)).thenReturn(true);
		doNothing().when(userRepository).deleteById(userId);

		mockMvc.perform(delete("/api/users/" + userId)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());

		verify(userRepository).existsById(userId);
		verify(userRepository).deleteById(userId);
	}

	@Test
	public void testDeleteUser_NotFound() throws Exception {
		when(userRepository.existsById(anyLong())).thenReturn(false);

		mockMvc.perform(delete("/api/users/999")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.error", is("User not found")));

		verify(userRepository).existsById(anyLong());
		verify(userRepository, never()).deleteById(anyLong());
	}
}