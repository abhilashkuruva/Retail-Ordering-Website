package com.retail.repository;

import com.retail.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for User entity operations.
 * Provides CRUD operations and custom query methods.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find a user by email address.
     * Used for login and registration validation.
     */
    Optional<User> findByEmail(String email);

    /**
     * Check if a user with the given email exists.
     */
    boolean existsByEmail(String email);
}