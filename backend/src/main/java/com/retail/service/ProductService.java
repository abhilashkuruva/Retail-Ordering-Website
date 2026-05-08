package com.retail.service;

import com.retail.entity.Product;
import com.retail.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for product-related business logic.
 * Handles product management, search, and inventory operations.
 */
@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    /**
     * Get all products.
     */
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Get product by ID.
     */
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    /**
     * Get products by category.
     */
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    /**
     * Search products by name (case-insensitive).
     */
    public List<Product> searchProducts(String keyword) {
        return productRepository.searchByProductName(keyword);
    }

    /**
     * Add a new product (Admin only).
     * Auto-generates product ID if not provided.
     */
    public Product addProduct(Product product) {
        // Check if product ID already exists
        if (product.getProductId() == null || product.getProductId().isEmpty()) {
            // Generate a unique product ID
            String productId = "PROD" + System.currentTimeMillis();
            product.setProductId(productId);
        } else if (productRepository.existsByProductId(product.getProductId())) {
            throw new RuntimeException("Product ID already exists");
        }
        
        // Initialize stock quantity if null
        if (product.getStockQuantity() == null) {
            product.setStockQuantity(0);
        }
        
        return productRepository.save(product);
    }

    /**
     * Update product stock quantity (Admin only).
     */
    public Product updateStock(Long productId, Integer newStockQuantity) {
        Product product = getProductById(productId);
        if (product == null) {
            throw new RuntimeException("Product not found");
        }
        
        product.setStockQuantity(newStockQuantity);
        return productRepository.save(product);
    }

    /**
     * Update product details (Admin only).
     */
    public Product updateProduct(Long productId, Product updatedProduct) {
        Product existingProduct = getProductById(productId);
        if (existingProduct == null) {
            throw new RuntimeException("Product not found");
        }
        
        // Update fields
        existingProduct.setProductName(updatedProduct.getProductName());
        existingProduct.setCategory(updatedProduct.getCategory());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setImageUrl(updatedProduct.getImageUrl());
        
        // Only update stock if explicitly set
        if (updatedProduct.getStockQuantity() != null) {
            existingProduct.setStockQuantity(updatedProduct.getStockQuantity());
        }
        
        return productRepository.save(existingProduct);
    }

    /**
     * Delete a product (Admin only).
     */
    public void deleteProduct(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new RuntimeException("Product not found");
        }
        productRepository.deleteById(productId);
    }

    /**
     * Check if product is in stock.
     */
    public boolean isProductInStock(Long productId) {
        Product product = getProductById(productId);
        return product != null && product.getStockQuantity() > 0;
    }
}