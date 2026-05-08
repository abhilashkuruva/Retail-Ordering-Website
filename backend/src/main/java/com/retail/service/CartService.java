package com.retail.service;

import com.retail.dto.CartItemDTO;
import com.retail.entity.Cart;
import com.retail.entity.Product;
import com.retail.entity.User;
import com.retail.repository.CartRepository;
import com.retail.repository.ProductRepository;
import com.retail.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for cart-related business logic.
 * Handles shopping cart operations for users.
 */
@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    /**
     * Get cart items for a specific user as DTOs.
     * Returns flattened data to avoid serialization issues.
     */
    public List<CartItemDTO> getCartByUserId(Long userId) {
        // Verify user exists
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        // Use DTO query to get flattened data
        return cartRepository.findCartItemsByUserId(userId);
    }

    /**
     * Add item to cart.
     * If product already exists in cart, updates the quantity.
     */
    public Cart addToCart(Long userId, Long productId, Integer quantity) {
        // Validate user
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        // Validate product
        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            throw new RuntimeException("Product not found");
        }

        // Check stock availability
        if (product.getStockQuantity() < quantity) {
            throw new RuntimeException("Insufficient stock. Available: " + product.getStockQuantity());
        }

        // Check if product already in cart
        Cart existingCart = cartRepository.findByUserAndProduct(user, product).orElse(null);
        
        if (existingCart != null) {
            // Update quantity
            int newQuantity = existingCart.getQuantity() + quantity;
            if (newQuantity > product.getStockQuantity()) {
                throw new RuntimeException("Cannot add more. Available stock: " + product.getStockQuantity());
            }
            existingCart.setQuantity(newQuantity);
            return cartRepository.save(existingCart);
        } else {
            // Create new cart item
            Cart cart = new Cart(user, product, quantity);
            return cartRepository.save(cart);
        }
    }

    /**
     * Update cart item quantity.
     */
    public Cart updateCartQuantity(Long userId, Long productId, Integer quantity) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            throw new RuntimeException("Product not found");
        }

        // Check stock availability
        if (product.getStockQuantity() < quantity) {
            throw new RuntimeException("Insufficient stock. Available: " + product.getStockQuantity());
        }

        Cart cart = cartRepository.findByUserAndProduct(user, product).orElse(null);
        if (cart == null) {
            throw new RuntimeException("Cart item not found");
        }

        cart.setQuantity(quantity);
        return cartRepository.save(cart);
    }

    /**
     * Remove item from cart.
     */
    public void removeFromCart(Long userId, Long productId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            throw new RuntimeException("Product not found");
        }

        cartRepository.deleteByUserAndProduct(user, product);
    }

    /**
     * Clear all items from user's cart.
     * Called after successful order placement.
     */
    public void clearCart(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        List<Cart> cartItems = cartRepository.findByUser(user);
        cartRepository.deleteAll(cartItems);
    }

    /**
     * Get cart item count for a user.
     */
    public int getCartItemCount(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return 0;
        }
        Integer count = cartRepository.countTotalItemsByUser(user);
        return count != null ? count : 0;
    }
}