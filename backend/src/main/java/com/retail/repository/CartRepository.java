package com.retail.repository;

import com.retail.entity.Cart;
import com.retail.entity.Product;
import com.retail.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Cart entity operations.
 * Manages shopping cart items for users.
 */
@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    /**
     * Find all cart items for a specific user.
     * Uses JOIN FETCH to eagerly load product data.
     */
    @Query("SELECT c FROM Cart c JOIN FETCH c.product WHERE c.user = :user ORDER BY c.id DESC")
    List<Cart> findByUser(@Param("user") User user);

    /**
     * Find a specific cart item by user and product.
     */
    Optional<Cart> findByUserAndProduct(User user, Product product);

    /**
     * Count total items in a user's cart.
     */
    @Query("SELECT SUM(c.quantity) FROM Cart c WHERE c.user = :user")
    Integer countTotalItemsByUser(@Param("user") User user);

    /**
     * Find all cart items for a user with product details (DTO projection).
     * This avoids lazy loading and serialization issues.
     */
    @Query("SELECT new com.retail.dto.CartItemDTO(" +
           "c.id, p.id, p.productName, p.imageUrl, p.price, c.quantity, p.category, p.stockQuantity) " +
           "FROM Cart c JOIN c.product p WHERE c.user.id = :userId ORDER BY c.id DESC")
    List<com.retail.dto.CartItemDTO> findCartItemsByUserId(@Param("userId") Long userId);

    /**
     * Delete cart item by user and product.
     */
    void deleteByUserAndProduct(User user, Product product);

    /**
     * Delete all cart items for a user.
     */
    void deleteAllByUser(User user);
}