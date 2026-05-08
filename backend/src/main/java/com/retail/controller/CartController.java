package com.retail.controller;

import com.retail.dto.ApiResponse;
import com.retail.dto.CartItemDTO;
import com.retail.dto.CartRequest;
import com.retail.entity.Cart;
import com.retail.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for cart operations.
 * Handles shopping cart management for users.
 */
@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Cart", description = "Shopping cart management APIs")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping
    @Operation(summary = "Get user cart", description = "Returns all items in the user's cart")
    public ResponseEntity<ApiResponse> getCart(@RequestParam Long userId) {
        try {
            List<CartItemDTO> cartItems = cartService.getCartByUserId(userId);
            Map<String, Object> data = new HashMap<>();
            data.put("cartItems", cartItems);
            data.put("itemCount", cartItems.size());
            return ResponseEntity.ok(new ApiResponse("Cart retrieved successfully", true, data));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), false));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), false));
        }
    }

    @PostMapping("/add")
    @Operation(summary = "Add item to cart", description = "Adds a product to the user's cart")
    public ResponseEntity<ApiResponse> addToCart(@RequestBody CartRequest request) {
        try {
            Cart cartItem = cartService.addToCart(request.getUserId(), request.getProductId(), request.getQuantity());
            Map<String, Object> data = new HashMap<>();
            data.put("cartItem", cartItem);
            return ResponseEntity.ok(new ApiResponse("Item added to cart successfully", true, data));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), false));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), false));
        }
    }

    @PutMapping("/update")
    @Operation(summary = "Update cart item quantity", description = "Updates the quantity of a product in the cart")
    public ResponseEntity<ApiResponse> updateCartQuantity(@RequestBody CartRequest request) {
        try {
            Cart cartItem = cartService.updateCartQuantity(request.getUserId(), request.getProductId(), request.getQuantity());
            Map<String, Object> data = new HashMap<>();
            data.put("cartItem", cartItem);
            return ResponseEntity.ok(new ApiResponse("Cart updated successfully", true, data));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), false));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), false));
        }
    }

    @DeleteMapping("/{productId}")
    @Operation(summary = "Remove item from cart", description = "Removes a product from the user's cart")
    public ResponseEntity<ApiResponse> removeFromCart(@RequestParam Long userId, @PathVariable Long productId) {
        try {
            cartService.removeFromCart(userId, productId);
            return ResponseEntity.ok(new ApiResponse("Item removed from cart successfully", true, null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), false));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), false));
        }
    }

    @GetMapping("/count")
    @Operation(summary = "Get cart item count", description = "Returns the number of items in the user's cart")
    public ResponseEntity<ApiResponse> getCartItemCount(@RequestParam Long userId) {
        try {
            int count = cartService.getCartItemCount(userId);
            Map<String, Object> data = new HashMap<>();
            data.put("itemCount", count);
            return ResponseEntity.ok(new ApiResponse("Cart count retrieved", true, data));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), false));
        }
    }
}