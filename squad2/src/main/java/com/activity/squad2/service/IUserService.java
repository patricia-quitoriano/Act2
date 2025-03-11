package com.activity.squad2.service;

import com.activity.squad2.model.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    User createUser(User user);

    List<User> getAllUsers();

    Optional<User> getUserById(Long id);

    Optional<User> getUserByName(String firstName, String lastName);

    Optional<User> updateUser(Long id, User updatedUser);

    boolean deleteUser(Long id);
}