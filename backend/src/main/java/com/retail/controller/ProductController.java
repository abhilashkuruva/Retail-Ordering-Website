package com.retail.controller;

import com.retail.dto.ApiResponse;
import com.retail.dto.ProductRequest;
import com.retail.dto.StockUpdateRequest;
import com.retail.entity.Product;
import com.retail.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for product operations.
 * Handles product browsing, searching, and admin management.
 */
@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Products", description = "Product management APIs")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    @Operation(summary = "Get all products", description = "Returns list of all available products")
    public ResponseEntity<ApiResponse> getAllProducts() {
        try {
            List<Product> products = productService.getAllProducts();
            Map<String, Object> data = new HashMap<>();
            data.put("products", products);
            return ResponseEntity.ok(new ApiResponse("Products retrieved successfully", true, data));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), false));
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID", description = "Returns a specific product by ID")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long id) {
        try {
            Product product = productService.getProductById(id);
            if (product == null) {
                return ResponseEntity.badRequest().body(new ApiResponse("Product not found", false));
            }
            Map<String, Object> data = new HashMap<>();
            data.put("product", product);
            return ResponseEntity.ok(new ApiResponse("Product retrieved successfully", true, data));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), false));
        }
    }

    @GetMapping("/search")
    @Operation(summary = "Search products", description = "Search products by name (case-insensitive)")
    public ResponseEntity<ApiResponse> searchProducts(@RequestParam String keyword) {
        try {
            List<Product> products = productService.searchProducts(keyword);
            Map<String, Object> data = new HashMap<>();
            data.put("products", products);
            return ResponseEntity.ok(new ApiResponse("Search completed", true, data));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), false));
        }
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "Get products by category", description = "Returns products filtered by category")
    public ResponseEntity<ApiResponse> getProductsByCategory(@PathVariable String category) {
        try {
            List<Product> products = productService.getProductsByCategory(category);
            Map<String, Object> data = new HashMap<>();
            data.put("products", products);
            return ResponseEntity.ok(new ApiResponse("Products retrieved by category", true, data));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), false));
        }
    }

    @PostMapping
    @Operation(summary = "Add new product", description = "Admin only: Creates a new product")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody ProductRequest request) {
        try {
            Product product = new Product();
            product.setProductId(request.getProductId());
            product.setProductName(request.getProductName());
            product.setCategory(request.getCategory());
            product.setPrice(request.getPrice());
            product.setImageUrl(request.getImageUrl());
            product.setStockQuantity(request.getStockQuantity());

            Product savedProduct = productService.addProduct(product);
            Map<String, Object> data = new HashMap<>();
            data.put("product", savedProduct);
            return ResponseEntity.ok(new ApiResponse("Product added successfully", true, data));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), false));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), false));
        }
    }

    @PutMapping("/{id}/stock")
    @Operation(summary = "Update product stock", description = "Admin only: Updates the stock quantity of a product")
    public ResponseEntity<ApiResponse> updateStock(@PathVariable Long id, @RequestBody StockUpdateRequest request) {
        try {
            Product product = productService.updateStock(id, request.getStockQuantity());
            Map<String, Object> data = new HashMap<>();
            data.put("product", product);
            return ResponseEntity.ok(new ApiResponse("Stock updated successfully", true, data));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), false));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), false));
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update product", description = "Admin only: Updates product details")
    public ResponseEntity<ApiResponse> updateProduct(@PathVariable Long id, @RequestBody ProductRequest request) {
        try {
            Product updatedProduct = new Product();
            updatedProduct.setProductName(request.getProductName());
            updatedProduct.setCategory(request.getCategory());
            updatedProduct.setPrice(request.getPrice());
            updatedProduct.setImageUrl(request.getImageUrl());
            updatedProduct.setStockQuantity(request.getStockQuantity());

            Product product = productService.updateProduct(id, updatedProduct);
            Map<String, Object> data = new HashMap<>();
            data.put("product", product);
            return ResponseEntity.ok(new ApiResponse("Product updated successfully", true, data));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), false));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), false));
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete product", description = "Admin only: Deletes a product")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok(new ApiResponse("Product deleted successfully", true, null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), false));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), false));
        }
    }
}