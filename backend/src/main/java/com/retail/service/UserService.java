package com.retail.service;

import com.retail.entity.User;
import com.retail.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service class for user-related business logic.
 * Handles user registration, authentication, and management.
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Register a new user with USER role.
     */
    public User registerUser(String name, String email, String password) {
        // Check if email already exists
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already exists");
        }
        
        User user = new User(name, email, password, "USER");
        return userRepository.save(user);
    }

    /**
     * Register a new admin with ADMIN role.
     */
    public User registerAdmin(String name, String email, String password) {
        // Check if email already exists
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already exists");
        }
        
        User user = new User(name, email, password, "ADMIN");
        return userRepository.save(user);
    }

    /**
     * Authenticate user login.
     * Returns user if credentials are valid, null otherwise.
     */
    public User loginUser(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        
        if (userOptional.isEmpty()) {
            return null;
        }
        
        User user = userOptional.get();
        
        // Check password and role
        if (!user.getPassword().equals(password)) {
            return null;
        }
        
        if (!"USER".equals(user.getRole())) {
            return null;
        }
        
        return user;
    }

    /**
     * Authenticate admin login.
     * Returns user if credentials are valid, null otherwise.
     */
    public User loginAdmin(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        
        if (userOptional.isEmpty()) {
            return null;
        }
        
        User user = userOptional.get();
        
        // Check password and role
        if (!user.getPassword().equals(password)) {
            return null;
        }
        
        if (!"ADMIN".equals(user.getRole())) {
            return null;
        }
        
        return user;
    }

    /**
     * Get user by ID.
     */
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    /**
     * Get user by email.
     */
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
}