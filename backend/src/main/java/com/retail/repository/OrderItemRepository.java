package com.retail.repository;

import com.retail.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for OrderItem entity operations.
 * Manages individual items within orders.
 */
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    // Basic CRUD operations are provided by JpaRepository
    // Custom queries can be added here if needed
}