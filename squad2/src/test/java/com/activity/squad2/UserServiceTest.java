package com.activity.squad2.service;

import com.activity.squad2.model.User;
import com.activity.squad2.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;
    private final Long userId = 1L;

    @BeforeEach
    void setup() {
        testUser = new User(userId, "John", "Doe", "123 Street", LocalDate.of(1990, 1, 1));
    }

    @Test
    void testGetAllUsersEmpty() {
        // Mock empty repository
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        List<User> users = userService.getAllUsers();
        assertTrue(users.isEmpty(), "Expected empty list of users");
        verify(userRepository).findAll();
    }

    @Test
    void testGetAllUsersNonEmpty() {
        // Mock a single user
        when(userRepository.findAll()).thenReturn(Collections.singletonList(testUser));

        List<User> users = userService.getAllUsers();
        assertEquals(1, users.size());
        assertEquals("John", users.get(0).getFirstName());
        verify(userRepository).findAll();
    }

    @Test
    void testCreateUser() {
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User saved = userService.createUser(testUser);
        assertNotNull(saved);
        assertEquals("John", saved.getFirstName());
        verify(userRepository).save(testUser);
    }

    @Test
    void testGetUserById_Found() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));

        Optional<User> foundUser = userService.getUserById(userId);
        assertTrue(foundUser.isPresent());
        assertEquals("John", foundUser.get().getFirstName());
        verify(userRepository).findById(userId);
    }

    @Test
    void testGetUserById_NotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Optional<User> foundUser = userService.getUserById(userId);
        assertFalse(foundUser.isPresent());
        verify(userRepository).findById(userId);
    }

    @Test
    void testGetUserByName_Found() {
        String firstName = "John";
        String lastName = "Doe";
        when(userRepository.findByFirstNameAndLastNameIgnoreCase(firstName, lastName))
                .thenReturn(Optional.of(testUser));

        Optional<User> foundUser = userService.getUserByName(firstName, lastName);
        assertTrue(foundUser.isPresent());
        assertEquals("John", foundUser.get().getFirstName());
        assertEquals("Doe", foundUser.get().getLastName());
        verify(userRepository).findByFirstNameAndLastNameIgnoreCase(firstName, lastName);
    }

    @Test
    void testGetUserByName_NotFound() {
        String firstName = "Jane";
        String lastName = "Smith";
        when(userRepository.findByFirstNameAndLastNameIgnoreCase(firstName, lastName))
                .thenReturn(Optional.empty());

        Optional<User> foundUser = userService.getUserByName(firstName, lastName);
        assertFalse(foundUser.isPresent());
        verify(userRepository).findByFirstNameAndLastNameIgnoreCase(firstName, lastName);
    }

    @Test
    void testUpdateUser_Found() {
        // Original user in DB
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));

        // Updated user
        User updatedUserData = new User(
                userId, "John", "Updated", "456 New Street", LocalDate.of(1991, 2, 2));

        // Mock save behavior with the updated user
        User savedUser = new User(
                userId, "John", "Updated", "456 New Street", LocalDate.of(1991, 2, 2));
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        Optional<User> result = userService.updateUser(userId, updatedUserData);

        assertTrue(result.isPresent());
        assertEquals("Updated", result.get().getLastName());
        assertEquals("456 New Street", result.get().getAddress());
        assertEquals(LocalDate.of(1991, 2, 2), result.get().getBirthday());

        verify(userRepository).findById(userId);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testUpdateUser_NotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        User updatedUserData = new User(
                userId, "John", "Updated", "456 New Street", LocalDate.of(1991, 2, 2));

        Optional<User> result = userService.updateUser(userId, updatedUserData);

        assertFalse(result.isPresent());
        verify(userRepository).findById(userId);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testDeleteUser_Success() {
        when(userRepository.existsById(userId)).thenReturn(true);
        doNothing().when(userRepository).deleteById(userId);

        boolean result = userService.deleteUser(userId);

        assertTrue(result);
        verify(userRepository).existsById(userId);
        verify(userRepository).deleteById(userId);
    }

    @Test
    void testDeleteUser_NotFound() {
        when(userRepository.existsById(userId)).thenReturn(false);

        boolean result = userService.deleteUser(userId);

        assertFalse(result);
        verify(userRepository).existsById(userId);
        verify(userRepository, never()).deleteById(userId);
    }
}