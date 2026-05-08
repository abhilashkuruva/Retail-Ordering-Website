package com.retail.repository;

import com.retail.entity.Order;
import com.retail.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Repository interface for Order entity operations.
 * Manages order records for users and admins.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Find all orders for a specific user.
     * Returns orders sorted by creation date (newest first).
     */
    List<Order> findByUserOrderByCreatedAtDesc(User user);

    /**
     * Find all orders sorted by creation date.
     * Used by admin to view all orders.
     */
    List<Order> findAllByOrderByCreatedAtDesc();
    
    // Dashboard Stats Queries
    
    @Query("SELECT COUNT(o) FROM Order o")
    long countTotalOrders();
    
    @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.paymentStatus = 'SUCCESS'")
    BigDecimal sumTotalRevenue();
    
    @Query("SELECT COUNT(DISTINCT o.user.id) FROM Order o WHERE o.paymentStatus = 'SUCCESS'")
    long countTotalCustomers();
    
    @Query("SELECT SUM(oi.quantity) FROM OrderItem oi WHERE oi.order.paymentStatus = 'SUCCESS'")
    Long sumTotalItemsSold();
    
    @Query("SELECT SUM(p.stockQuantity) FROM Product p")
    Long sumTotalInventory();
    
    // Order Summary Query for Admin Dashboard
    @Query("SELECT new com.retail.dto.OrderSummaryDTO(" +
           "o.id, o.user.name, o.user.email, " +
           "SUM(oi.quantity), " +
           "CASE WHEN COUNT(oi) = 1 THEN CONCAT(oi.product.productName, ' x', oi.quantity) " +
           "ELSE CONCAT(COUNT(oi), ' items') END, " +
           "o.totalAmount, o.paymentStatus, o.orderStatus, o.createdAt) " +
           "FROM Order o JOIN o.orderItems oi " +
           "GROUP BY o.id, o.user.name, o.user.email, o.totalAmount, o.paymentStatus, o.orderStatus, o.createdAt " +
           "ORDER BY o.createdAt DESC")
    List<com.retail.dto.OrderSummaryDTO> findAllOrderSummaries();
}