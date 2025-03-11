package com.activity.squad2.controller;

import com.activity.squad2.model.User;
import com.activity.squad2.service.ICMAPService;
import com.activity.squad2.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class ICMAPController {

    private final IUserService userService;
    private final ICMAPService icmapService;

    @Autowired
    public ICMAPController(IUserService userService, ICMAPService icmapService) {
        this.userService = userService;
        this.icmapService = icmapService;
    }

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
}