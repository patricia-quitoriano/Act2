package com.activity.squad2.service;

import com.activity.squad2.model.User;
import com.activity.squad2.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllUsersEmpty() {
        // Mock empty repository
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        List<User> users = userService.getAllUsers();
        assertTrue(users.isEmpty(), "Expected empty list of users");
    }

    @Test
    void testGetAllUsersNonEmpty() {
        // Mock a single user
        User user = new User("John", "Doe", "123 Street", LocalDate.of(1990, 1, 1));
        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));

        List<User> users = userService.getAllUsers();
        assertEquals(1, users.size());
        assertEquals("John", users.get(0).getFirstName());
    }

    @Test
    void testCreateUser() {
        User user = new User("Jane", "Doe", "456 Avenue", LocalDate.of(1985, 5, 15));
        when(userRepository.save(user)).thenReturn(user);

        User saved = userService.createUser(user);
        assertNotNull(saved);
        assertEquals("Jane", saved.getFirstName());
        verify(userRepository, times(1)).save(user);
    }
}
