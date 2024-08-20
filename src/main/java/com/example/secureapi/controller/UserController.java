package com.example.secureapi.controller;

import com.example.secureapi.model.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;
import java.util.concurrent.atomic.AtomicLong;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    // A list to store User objects (in-memory storage)
    private List<User> users = new ArrayList<>();

    // A counter to generate unique IDs for each user
    private AtomicLong counter = new AtomicLong();

    // Endpoint to retrieve all users
    @GetMapping
    public List<User> getAllUsers() {
        return users;
    }

    // Endpoint to create a new user
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)  // Returns HTTP 201 status on successful creation
    public User createUser(@Valid @RequestBody User user) {
        // Set a unique ID for the new user and add them to the list
        user.setId(counter.incrementAndGet());
        users.add(user);
        return user;
    }

    // Endpoint to welcome a user with their name
    @GetMapping("/welcome")
    public String welcomeUser(@RequestParam String name) {
        // Output encoding using Spring's HtmlUtils to prevent XSS attacks
        String safeName = HtmlUtils.htmlEscape(name);
        return "<h1>Welcome, " + safeName + "!</h1>";
    }
}
