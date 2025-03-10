package com.activity.squad2;

import com.activity.squad2.config.SecurityConfig;
import com.activity.squad2.model.User;
import com.activity.squad2.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.transaction.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = {Application.class, SecurityConfig.class})
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class ApplicationIntegrationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserRepository userRepository;

	private Long savedUserId;

	@BeforeEach
	public void setUp() {
		userRepository.deleteAll();
		User user = new User("Juan", "Dela Cruz", "123 Sample St", java.time.LocalDate.of(1990, 5, 15));
		User savedUser = userRepository.saveAndFlush(user);
		savedUserId = savedUser.getId(); // Assuming your User class has an ID field
	}

	@Test
	public void testCreateUser() throws Exception {
		mockMvc.perform(post("/api/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{ \"firstName\": \"John\", \"lastName\": \"Doe\", \"address\": \"456 Main St\", \"birthday\": \"1995-07-20\" }"))
				.andExpect(status().isCreated());
	}

	@Test
	public void testGetUserById() throws Exception {
		// Use the actual saved user ID instead of hardcoding to 1
		mockMvc.perform(get("/api/users/" + savedUserId)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	public void testGetUserById_NotFound() throws Exception {
		// Use a non-existent ID (assuming IDs are positive)
		Long nonExistentId = savedUserId + 999;
		mockMvc.perform(get("/api/users/" + nonExistentId)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}
}