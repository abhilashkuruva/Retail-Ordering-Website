package com.retail;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application entry point for the Retail Ordering Website.
 * This Spring Boot application provides REST APIs for product management,
 * shopping cart, and order processing.
 */
@SpringBootApplication
public class RetailOrderingApplication {

    public static void main(String[] args) {
        SpringApplication.run(RetailOrderingApplication.class, args);
        System.out.println("========================================");
        System.out.println("Retail Ordering Website Started!");
        System.out.println("Swagger UI: http://localhost:8080/swagger-ui.html");
        System.out.println("API Docs: http://localhost:8080/api-docs");
        System.out.println("========================================");
    }
}