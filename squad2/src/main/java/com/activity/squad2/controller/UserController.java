package com.activity.squad2.controller;

import com.activity.squad2.model.User;
import com.activity.squad2.service.ICMAPService;
import com.activity.squad2.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;
    private final ICMAPService icmapService;

    // Create User
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(user));
    }

    // Get All Users
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // Get User by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User not found"));
        }
    }

    // Get User by First & Last Name (Case-Insensitive)
    @GetMapping("/search")
    public ResponseEntity<?> getUserByName(@RequestParam String firstName, @RequestParam String lastName) {
        Optional<User> user = userService.getUserByName(firstName, lastName);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User not found"));
        }
    }

    // Get ICMAP Data (Ensuring User Exists)
    @GetMapping("/icmap")
    public ResponseEntity<?> getICMAPData(@RequestParam String firstName, @RequestParam String lastName) {
        Optional<User> user = userService.getUserByName(firstName, lastName);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User not found"));
        }
        return icmapService.getICMAPData(firstName, lastName);
    }

    // Update User by ID
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        Optional<User> user = userService.updateUser(id, updatedUser);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User not found"));
        }
    }

    // Delete User
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        boolean deleted = userService.deleteUser(id);
        if (!deleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User not found"));
        }
        return ResponseEntity.noContent().build();
    }
}
