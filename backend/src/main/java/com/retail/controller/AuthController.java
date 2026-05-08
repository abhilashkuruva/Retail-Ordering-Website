package com.retail.controller;

import com.retail.dto.ApiResponse;
import com.retail.dto.LoginRequest;
import com.retail.dto.RegisterRequest;
import com.retail.entity.User;
import com.retail.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * REST Controller for authentication operations.
 * Handles user and admin registration/login.
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Authentication", description = "User and Admin authentication APIs")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Creates a new user account with USER role")
    public ResponseEntity<ApiResponse> registerUser(@RequestBody RegisterRequest request) {
        try {
            User user = userService.registerUser(request.getName(), request.getEmail(), request.getPassword());
            Map<String, Object> data = new HashMap<>();
            data.put("user", user);
            return ResponseEntity.ok(new ApiResponse("User registered successfully", true, data));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), false));
        }
    }

    @PostMapping("/login")
    @Operation(summary = "Login user", description = "Authenticates a user and returns user details")
    public ResponseEntity<ApiResponse> loginUser(@RequestBody LoginRequest request) {
        try {
            User user = userService.loginUser(request.getEmail(), request.getPassword());
            if (user == null) {
                return ResponseEntity.badRequest().body(new ApiResponse("Invalid credentials", false));
            }
            Map<String, Object> data = new HashMap<>();
            data.put("user", user);
            return ResponseEntity.ok(new ApiResponse("Login successful", true, data));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), false));
        }
    }

    @PostMapping("/register-admin")
    @Operation(summary = "Register a new admin", description = "Creates a new admin account with ADMIN role")
    public ResponseEntity<ApiResponse> registerAdmin(@RequestBody RegisterRequest request) {
        try {
            User user = userService.registerAdmin(request.getName(), request.getEmail(), request.getPassword());
            Map<String, Object> data = new HashMap<>();
            data.put("user", user);
            return ResponseEntity.ok(new ApiResponse("Admin registered successfully", true, data));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), false));
        }
    }

    @PostMapping("/login-admin")
    @Operation(summary = "Login admin", description = "Authenticates an admin and returns admin details")
    public ResponseEntity<ApiResponse> loginAdmin(@RequestBody LoginRequest request) {
        try {
            User user = userService.loginAdmin(request.getEmail(), request.getPassword());
            if (user == null) {
                return ResponseEntity.badRequest().body(new ApiResponse("Invalid admin credentials", false));
            }
            Map<String, Object> data = new HashMap<>();
            data.put("user", user);
            return ResponseEntity.ok(new ApiResponse("Admin login successful", true, data));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), false));
        }
    }

    @GetMapping("/verify/{userId}")
    @Operation(summary = "Verify user", description = "Verifies if a user exists and returns user details")
    public ResponseEntity<ApiResponse> verifyUser(@PathVariable Long userId) {
        try {
            User user = userService.getUserById(userId);
            if (user == null) {
                return ResponseEntity.badRequest().body(new ApiResponse("User not found", false));
            }
            Map<String, Object> data = new HashMap<>();
            data.put("user", user);
            return ResponseEntity.ok(new ApiResponse("User verified", true, data));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), false));
        }
    }
}