package com.retail.repository;

import com.retail.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Product entity operations.
 * Provides CRUD operations and custom query methods for product search and filtering.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Find products by category.
     */
    List<Product> findByCategory(String category);

    /**
     * Search products by name (case-insensitive).
     */
    @Query("SELECT p FROM Product p WHERE LOWER(p.productName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Product> searchByProductName(@Param("keyword") String keyword);

    /**
     * Check if a product with the given product ID exists.
     */
    boolean existsByProductId(String productId);

    /**
     * Find product by product ID.
     */
    Product findByProductId(String productId);
}