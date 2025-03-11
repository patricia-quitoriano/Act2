package com.activity.squad2.service;

import com.activity.squad2.model.User;
import com.activity.squad2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> getUserByName(String firstName, String lastName) {
        return userRepository.findByFirstNameAndLastNameIgnoreCase(firstName, lastName);
    }

    @Override
    public Optional<User> updateUser(Long id, User updatedUser) {
        return userRepository.findById(id).map(existingUser -> {
            existingUser.setFirstName(updatedUser.getFirstName());
            existingUser.setLastName(updatedUser.getLastName());
            existingUser.setAddress(updatedUser.getAddress());
            existingUser.setBirthday(updatedUser.getBirthday());
            return userRepository.save(existingUser);
        });
    }

    @Override
    public boolean deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            return false;
        }
        userRepository.deleteById(id);
        return true;
    }
}
