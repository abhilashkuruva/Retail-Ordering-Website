package com.retail.service;

import com.retail.entity.*;
import com.retail.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service class for order-related business logic.
 * Handles order placement, payment confirmation, and inventory updates.
 */
@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartRepository cartRepository;

    /**
     * Place a new order from cart items.
     * This method is transactional to ensure data consistency.
     */
    @Transactional
    public Order placeOrder(Long userId) {
        // Validate user
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        // Get cart items
        List<Cart> cartItems = cartRepository.findByUser(user);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        // Calculate total and validate stock
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (Cart cartItem : cartItems) {
            Product product = cartItem.getProduct();
            Integer quantity = cartItem.getQuantity();

            // Check stock availability
            if (product.getStockQuantity() < quantity) {
                throw new RuntimeException("Insufficient stock for: " + product.getProductName());
            }

            // Calculate total
            totalAmount = totalAmount.add(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
        }

        // Create order
        Order order = new Order(user, totalAmount, "PENDING", "PENDING");
        order = orderRepository.save(order);

        // Create order items and update stock
        for (Cart cartItem : cartItems) {
            Product product = cartItem.getProduct();
            Integer quantity = cartItem.getQuantity();

            // Create order item
            OrderItem orderItem = new OrderItem(order, product, quantity, product.getPrice());
            orderItemRepository.save(orderItem);

            // Update product stock
            product.setStockQuantity(product.getStockQuantity() - quantity);
            productRepository.save(product);
        }

        // Clear cart
        cartRepository.deleteAll(cartItems);

        return order;
    }

    /**
     * Confirm payment for an order.
     * Updates order status to CONFIRMED and payment status to SUCCESS.
     */
    @Transactional
    public Order confirmPayment(Long orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            throw new RuntimeException("Order not found");
        }

        order.setPaymentStatus("SUCCESS");
        order.setOrderStatus("CONFIRMED");
        return orderRepository.save(order);
    }

    /**
     * Get order by ID.
     */
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    /**
     * Get all orders for a specific user.
     */
    public List<Order> getOrdersByUserId(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        return orderRepository.findByUserOrderByCreatedAtDesc(user);
    }

    /**
     * Get all orders (Admin only).
     */
    public List<Order> getAllOrders() {
        return orderRepository.findAllByOrderByCreatedAtDesc();
    }

    /**
     * Update order status (Admin only).
     */
    @Transactional
    public Order updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            throw new RuntimeException("Order not found");
        }

        order.setOrderStatus(status);
        return orderRepository.save(order);
    }
}