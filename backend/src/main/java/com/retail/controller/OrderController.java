package com.retail.controller;

import com.retail.dto.ApiResponse;
import com.retail.entity.Order;
import com.retail.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for order operations.
 * Handles order placement, payment confirmation, and order management.
 */
@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Orders", description = "Order management APIs")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/place")
    @Operation(summary = "Place order", description = "Creates a new order from user's cart items")
    public ResponseEntity<ApiResponse> placeOrder(@RequestParam Long userId) {
        try {
            Order order = orderService.placeOrder(userId);
            Map<String, Object> data = new HashMap<>();
            data.put("order", order);
            return ResponseEntity.ok(new ApiResponse("Order placed successfully", true, data));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), false));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), false));
        }
    }

    @PostMapping("/{id}/payment")
    @Operation(summary = "Confirm payment", description = "Confirms payment for an order (dummy payment)")
    public ResponseEntity<ApiResponse> confirmPayment(@PathVariable Long id) {
        try {
            Order order = orderService.confirmPayment(id);
            Map<String, Object> data = new HashMap<>();
            data.put("order", order);
            return ResponseEntity.ok(new ApiResponse("Payment confirmed successfully", true, data));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), false));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), false));
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order by ID", description = "Returns a specific order by ID")
    public ResponseEntity<ApiResponse> getOrderById(@PathVariable Long id) {
        try {
            Order order = orderService.getOrderById(id);
            if (order == null) {
                return ResponseEntity.badRequest().body(new ApiResponse("Order not found", false));
            }
            Map<String, Object> data = new HashMap<>();
            data.put("order", order);
            return ResponseEntity.ok(new ApiResponse("Order retrieved successfully", true, data));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), false));
        }
    }

    @GetMapping("/user")
    @Operation(summary = "Get user orders", description = "Returns all orders for a specific user")
    public ResponseEntity<ApiResponse> getUserOrders(@RequestParam Long userId) {
        try {
            List<Order> orders = orderService.getOrdersByUserId(userId);
            Map<String, Object> data = new HashMap<>();
            data.put("orders", orders);
            return ResponseEntity.ok(new ApiResponse("Orders retrieved successfully", true, data));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), false));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), false));
        }
    }

    @GetMapping
    @Operation(summary = "Get all orders", description = "Admin only: Returns all orders")
    public ResponseEntity<ApiResponse> getAllOrders() {
        try {
            List<Order> orders = orderService.getAllOrders();
            Map<String, Object> data = new HashMap<>();
            data.put("orders", orders);
            return ResponseEntity.ok(new ApiResponse("Orders retrieved successfully", true, data));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), false));
        }
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Update order status", description = "Admin only: Updates the status of an order")
    public ResponseEntity<ApiResponse> updateOrderStatus(@PathVariable Long id, @RequestParam String status) {
        try {
            Order order = orderService.updateOrderStatus(id, status);
            Map<String, Object> data = new HashMap<>();
            data.put("order", order);
            return ResponseEntity.ok(new ApiResponse("Order status updated successfully", true, data));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), false));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), false));
        }
    }
}