package com.activity.squad2.controller;

import com.activity.squad2.model.User;
import com.activity.squad2.service.ICMAPService;
import com.activity.squad2.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final IUserService userService;
    private final ICMAPService icmapService;

    @Autowired
    public UserController(IUserService userService, ICMAPService icmapService) {
        this.userService = userService;
        this.icmapService = icmapService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User savedUser = userService.createUser(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        Optional<User> userOptional = userService.getUserById(id);
        if (userOptional.isPresent()) {
            return new ResponseEntity<>(userOptional.get(), HttpStatus.OK);
        } else {
            Map<String, String> error = new HashMap<>();
            error.put("error", "User not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> getUserByName(
            @RequestParam String firstName,
            @RequestParam String lastName) {
        Optional<User> userOptional = userService.getUserByName(firstName, lastName);
        if (userOptional.isPresent()) {
            return new ResponseEntity<>(userOptional.get(), HttpStatus.OK);
        } else {
            Map<String, String> error = new HashMap<>();
            error.put("error", "User not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }

    // Remove this method since it's duplicated in ICMAPController
    /*
    @GetMapping("/icmap")
    public ResponseEntity<?> getICMAPData(
            @RequestParam String firstName,
            @RequestParam String lastName) {

        // First check if the user exists
        Optional<User> userOptional = userService.getUserByName(firstName, lastName);

        if (userOptional.isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "User not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        // If user exists, get ICMAP data
        ResponseEntity<?> icmapResponse = icmapService.getICMAPData(firstName, lastName);
        return icmapResponse;
    }
    */

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        Optional<User> updatedUserOptional = userService.updateUser(id, userDetails);
        if (updatedUserOptional.isPresent()) {
            return new ResponseEntity<>(updatedUserOptional.get(), HttpStatus.OK);
        } else {
            Map<String, String> error = new HashMap<>();
            error.put("error", "User not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        boolean deleted = userService.deleteUser(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            Map<String, String> error = new HashMap<>();
            error.put("error", "User not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }
}