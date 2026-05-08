# Retail Ordering Website - Comprehensive Documentation

## Table of Contents
1. [Project Overview](#project-overview)
2. [Architecture](#architecture)
3. [Database Design](#database-design)
4. [API Documentation](#api-documentation)
5. [Code Explanation](#code-explanation)
6. [Workflow Diagrams](#workflow-diagrams)
7. [Concurrency Handling](#concurrency-handling)
8. [Viva Questions & Answers](#viva-questions--answers)
9. [Troubleshooting Guide](#troubleshooting-guide)

---

## Project Overview

### Problem Statement
A retail ordering system where customers can browse, search, and order food products (Pizzas, Cold Drinks, Breads) with automatic inventory management and admin controls.

### Solution Approach
- **Frontend**: React SPA with Bootstrap for responsive UI
- **Backend**: Spring Boot REST API with Spring Data JPA
- **Database**: MySQL with auto-schema generation
- **Authentication**: Role-based (USER/ADMIN)

### Why This Project is Useful
1. Demonstrates full-stack development skills
2. Implements real-world e-commerce features
3. Shows database design and optimization
4. Includes inventory management logic
5. Demonstrates role-based access control

---

## Architecture

### Layered Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                     React Frontend                          │
│  (Components, Pages, Services, Bootstrap UI)               │
└─────────────────────┬───────────────────────────────────────┘
                      │ HTTP/JSON (Axios)
┌─────────────────────▼───────────────────────────────────────┐
│                   Spring Boot Backend                       │
│  ┌───────────────────────────────────────────────────────┐  │
│  │              Controller Layer                         │  │
│  │  (AuthController, ProductController, etc.)           │  │
│  └───────────────────┬───────────────────────────────────┘  │
│  ┌───────────────────▼───────────────────────────────────┐  │
│  │               Service Layer                           │  │
│  │  (Business Logic, Validation, Transactions)          │  │
│  └───────────────────┬───────────────────────────────────┘  │
│  ┌───────────────────▼───────────────────────────────────┐  │
│  │             Repository Layer                          │  │
│  │  (JPA/Hibernate - Data Access)                       │  │
│  └───────────────────┬───────────────────────────────────┘  │
└─────────────────────┼───────────────────────────────────────┘
                      │ JDBC
┌─────────────────────▼───────────────────────────────────────┐
│                     MySQL Database                          │
│  (users, products, cart, orders, order_items)              │
└─────────────────────────────────────────────────────────────┘
```

### Request Flow

```
User Action (React)
    ↓
Axios HTTP Request
    ↓
Spring CORS Filter
    ↓
Controller (Request Mapping)
    ↓
Service (Business Logic)
    ↓
Repository (JPA Query)
    ↓
MySQL Database
    ↓
Entity/DTO Response
    ↓
Controller Response
    ↓
Axios Response Handler
    ↓
React State Update
    ↓
UI Re-render
```

---

## Database Design

### Entity Relationship Diagram

```
┌─────────────────────┐
│       users         │
├─────────────────────┤
│ id (PK)             │
│ name                │
│ email (UNIQUE)      │
│ password            │
│ role (ADMIN/USER)   │
└─────────────────────┘
        │ 1
        │
        │
        │ ∞
┌───────▼─────────────┐    ┌─────────────────────┐
│       cart          │    │      products       │
├─────────────────────┤    ├─────────────────────┤
│ id (PK)             │◄───┤ id (PK)             │
│ user_id (FK)        │    │ productId (UNIQUE)  │
│ product_id (FK)     │    │ productName         │
│ quantity            │    │ category            │
└─────────────────────┘    │ price               │
                           │ imageUrl            │
┌─────────────────────┐    │ stockQuantity       │
│       orders        │    └─────────────────────┘
├─────────────────────┤             │ 1
│ id (PK)             │             │
│ user_id (FK)        │             │ ∞
│ totalAmount         │    ┌────────▼─────────────┐
│ orderStatus         │    │     order_items      │
│ paymentStatus       │    ├─────────────────────┤
│ createdAt           │    │ id (PK)              │
└─────────────────────┘    │ order_id (FK)        │
        │ 1                 │ product_id (FK)      │
        │                   │ quantity             │
        │ ∞                 │ price                │
└─────────────────────┘
```

### Table Relationships

| Relationship | Type | Description |
|-------------|------|-------------|
| User → Cart | One-to-Many | One user can have many cart items |
| User → Orders | One-to-Many | One user can place many orders |
| Product → Cart | One-to-Many | One product can be in many carts |
| Product → OrderItems | One-to-Many | One product can be in many orders |
| Order → OrderItems | One-to-Many | One order contains many items |

### Why Each Table Exists

1. **users**: Stores user credentials and role information
2. **products**: Product catalog with inventory tracking
3. **cart**: Temporary storage for items user wants to purchase
4. **orders**: Order header with total amount and status
5. **order_items**: Line items for each order (normalized design)

### Normalization

- **1NF**: All columns contain atomic values
- **2NF**: No partial dependencies (all non-key columns depend on entire primary key)
- **3NF**: No transitive dependencies (non-key columns don't depend on other non-key columns)

---

## API Documentation

### Authentication APIs

| Method | Endpoint | Description | Request Body |
|--------|----------|-------------|--------------|
| POST | /api/auth/register | Register new user | {name, email, password} |
| POST | /api/auth/login | User login | {email, password} |
| POST | /api/auth/register-admin | Register admin | {name, email, password} |
| POST | /api/auth/login-admin | Admin login | {email, password} |
| GET | /api/auth/verify/{userId} | Verify user | - |

### Product APIs

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/products | Get all products |
| GET | /api/products/{id} | Get product by ID |
| GET | /api/products/search?keyword= | Search products |
| GET | /api/products/category/{category} | Filter by category |
| POST | /api/products | Add product (Admin) |
| PUT | /api/products/{id}/stock | Update stock (Admin) |
| PUT | /api/products/{id} | Update product (Admin) |
| DELETE | /api/products/{id} | Delete product (Admin) |

### Cart APIs

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/cart?userId= | Get user's cart |
| POST | /api/cart/add | Add item to cart |
| PUT | /api/cart/update | Update cart quantity |
| DELETE | /api/cart/{productId}?userId= | Remove from cart |
| GET | /api/cart/count?userId= | Get cart item count |

### Order APIs

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/orders/place?userId= | Place order from cart |
| POST | /api/orders/{id}/payment | Confirm payment |
| GET | /api/orders/{id} | Get order details |
| GET | /api/orders/user?userId= | Get user's orders |
| GET | /api/orders | Get all orders (Admin) |
| PUT | /api/orders/{id}/status?status= | Update order status (Admin) |

---

## Code Explanation

### Entity Classes

#### User.java
```java
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    // Relationships
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Cart> cartItems;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Order> orders;
}
```

**Why this design?**
- `@GeneratedValue.IDENTITY`: Auto-increment primary key
- `unique = true` on email: Prevents duplicate registrations
- `cascade = CascadeType.ALL`: When user is deleted, their cart and orders are also deleted
- `mappedBy = "user"`: Inverse side of the relationship (User doesn't own the foreign key)

#### Product.java
```java
@Entity
@Table(name = "products")
public class Product {
    @Column(nullable = false, unique = true)
    private String productId;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
    
    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity;
}
```

**Why BigDecimal for price?**
- Floating-point numbers (float/double) have precision issues
- BigDecimal provides exact decimal representation
- Essential for financial calculations

#### Order.java
```java
@Entity
@Table(name = "orders")
public class Order {
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    public Order() {
        this.createdAt = LocalDateTime.now();
    }
}
```

**Why LocalDateTime?**
- Stores both date and time
- No timezone issues (unlike Date)
- Immutable and thread-safe

### Service Layer

#### OrderService.java - Transactional Operations
```java
@Transactional
public Order placeOrder(Long userId) {
    // 1. Validate user
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("User not found"));
    
    // 2. Get cart items
    List<Cart> cartItems = cartRepository.findByUser(user);
    
    // 3. Validate stock and calculate total
    BigDecimal total = BigDecimal.ZERO;
    for (Cart item : cartItems) {
        if (item.getProduct().getStockQuantity() < item.getQuantity()) {
            throw new RuntimeException("Insufficient stock");
        }
        total = total.add(item.getProduct().getPrice()
            .multiply(BigDecimal.valueOf(item.getQuantity())));
    }
    
    // 4. Create order
    Order order = new Order(user, total, "PENDING", "PENDING");
    orderRepository.save(order);
    
    // 5. Create order items and update stock
    for (Cart item : cartItems) {
        OrderItem orderItem = new OrderItem(order, item.getProduct(), 
            item.getQuantity(), item.getProduct().getPrice());
        orderItemRepository.save(orderItem);
        
        // Update stock
        item.getProduct().setStockQuantity(
            item.getProduct().getStockQuantity() - item.getQuantity());
        productRepository.save(item.getProduct());
    }
    
    // 6. Clear cart
    cartRepository.deleteAll(cartItems);
    
    return order;
}
```

**Why @Transactional?**
- Ensures all operations succeed or all fail (ACID properties)
- If stock update fails, order is not created
- Prevents data inconsistency

### Repository Layer

#### ProductRepository.java - Custom Queries
```java
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    @Query("SELECT p FROM Product p WHERE LOWER(p.productName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Product> searchByProductName(@Param("keyword") String keyword);
    
    List<Product> findByCategory(String category);
}
```

**Why JPQL?**
- Database-independent
- Works with entity objects, not tables
- Type-safe with compile-time checking

---

## Workflow Diagrams

### Order Placement Workflow

```
User adds items to cart
        ↓
User clicks "Place Order"
        ↓
Frontend calls POST /api/orders/place?userId=X
        ↓
OrderService.placeOrder() starts
        ↓
┌─────────────────────────────────────┐
│ @Transactional Block                │
│ 1. Validate user exists             │
│ 2. Get cart items                   │
│ 3. Check stock for each item        │
│ 4. Calculate total amount           │
│ 5. Create Order entity              │
│ 6. Create OrderItem for each cart   │
│ 7. Update product stock             │
│ 8. Clear cart                       │
└─────────────────────────────────────┘
        ↓
Order created with status PENDING
        ↓
Frontend calls POST /api/orders/{id}/payment
        ↓
Order status → CONFIRMED
Payment status → SUCCESS
        ↓
Order appears in user's order history
```

### Inventory Update Workflow

```
Admin updates stock quantity
        ↓
Frontend calls PUT /api/products/{id}/stock
        ↓
ProductService.updateStock()
        ↓
Find product by ID
        ↓
Update stockQuantity field
        ↓
Save to database
        ↓
Return updated product
        ↓
Frontend refreshes product list
        ↓
UI shows new stock quantity
```

### Cart Management Workflow

```
User clicks "Add to Cart"
        ↓
Check if user is logged in
        ↓
Frontend calls POST /api/cart/add
        ↓
CartService.addToCart()
        ↓
┌─────────────────────────────────────┐
│ 1. Find user and product            │
│ 2. Check if already in cart         │
│    - If yes: Update quantity        │
│    - If no: Create new cart item    │
│ 3. Validate stock availability      │
│ 4. Save cart item                   │
└─────────────────────────────────────┘
        ↓
Return updated cart
        ↓
Frontend updates cart badge count
```

---

## Concurrency Handling

### Scenario: Two Users Ordering Same Product

```
Time    User A                          User B
────────────────────────────────────────────────────────────
T1      Reads stock: 5                  
────────────────────────────────────────────────────────────
T2                                      Reads stock: 5
────────────────────────────────────────────────────────────
T3      Orders 3 items                  Orders 3 items
        Calculates: 5 - 3 = 2           Calculates: 5 - 3 = 2
────────────────────────────────────────────────────────────
T4      Saves stock: 2                  Saves stock: 2
────────────────────────────────────────────────────────────
Result: Stock shows 2, but should be -1 (impossible!)
```

### Solution: Database Locking

#### Optimistic Locking (Recommended)
```java
@Entity
public class Product {
    @Version
    private Long version;
}
```

#### Pessimistic Locking
```java
@Query("SELECT p FROM Product p WHERE p.id = :id")
@Lock(LockModeType.PESSIMISTIC_WRITE)
Product findByIdForUpdate(@Param("id") Long id);
```

### Our Implementation

We use **@Transactional** with database-level constraints:

```java
@Transactional
public Order placeOrder(Long userId) {
    // Database ensures atomicity
    // If two transactions try to update same product,
    // one will fail and rollback
}
```

### How It Works

1. **Transaction Isolation**: Each order placement runs in its own transaction
2. **Row-level Locks**: Database locks the product row during update
3. **Atomic Operations**: Stock update happens atomically
4. **Rollback on Failure**: If any step fails, entire transaction rolls back

### Example with Database Constraints

```sql
-- Database ensures stock never goes negative
ALTER TABLE products ADD CONSTRAINT chk_stock 
CHECK (stock_quantity >= 0);
```

---

## Viva Questions & Answers

### Database Questions

**Q1: Why did you use MySQL over other databases?**
A: MySQL is widely used, well-documented, and perfect for relational data like e-commerce. It supports ACID transactions which are crucial for order processing.

**Q2: What is the difference between INNER JOIN and LEFT JOIN?**
A: 
- INNER JOIN returns only matching rows from both tables
- LEFT JOIN returns all rows from left table and matching rows from right (NULL if no match)

**Q3: What is normalization? Did you normalize your database?**
A: Normalization is organizing data to reduce redundancy. Yes, our database is in 3NF:
- 1NF: Atomic values (no arrays in columns)
- 2NF: No partial dependencies
- 3NF: No transitive dependencies

**Q4: Why separate orders and order_items tables?**
A: To support multiple items per order (one-to-many relationship). This is normalized design - order header contains total/status, order_items contain individual products.

**Q5: What indexes would you add for performance?**
A:
- `users(email)` - for login queries
- `products(category)` - for category filtering
- `products(productName)` - for search
- `orders(user_id)` - for user order history
- `cart(user_id, product_id)` - for cart operations

### Spring Boot Questions

**Q6: What is the difference between @Controller and @RestController?**
A: 
- @Controller returns view names (for server-side rendering)
- @RestController returns JSON/XML (for REST APIs) - it's @Controller + @ResponseBody

**Q7: What is Dependency Injection?**
A: Design pattern where objects receive their dependencies from external sources rather than creating them. In Spring, we use @Autowired.

**Q8: What is the difference between @Component, @Service, and @Repository?**
A:
- @Component: Generic Spring bean
- @Service: Business logic layer (specialization of @Component)
- @Repository: Data access layer (translates DB exceptions to Spring exceptions)

**Q9: What is @Transactional annotation?**
A: Ensures a method executes within a database transaction. If any operation fails, all changes are rolled back (ACID properties).

**Q10: What is Spring Data JPA?**
A: Abstraction layer over JPA that reduces boilerplate code. JpaRepository provides CRUD operations automatically.

### React Questions

**Q11: What is the difference between state and props?**
A:
- Props: Passed from parent to child (read-only)
- State: Managed within component (can change)

**Q12: What is useEffect hook?**
A: Handles side effects in functional components (API calls, subscriptions, DOM manipulation). Replaces componentDidMount/Update/Unmount.

**Q13: What is Axios and why use it over fetch?**
A: Axios is HTTP client with advantages:
- Automatic JSON transformation
- Request/response interceptors
- Better error handling
- Browser compatibility

**Q14: What is React Router?**
A: Library for client-side routing in React SPAs. Allows navigation without page reload.

**Q15: What are React hooks?**
A: Functions that let you use state and lifecycle features in functional components (useState, useEffect, useContext, etc.)

### Business Logic Questions

**Q16: How do you prevent overselling (selling more than stock)?**
A: 
1. Check stock before adding to cart
2. Re-check stock before placing order
3. Use @Transactional for atomic operations
4. Database constraints as final safeguard

**Q17: What happens if payment fails after order is placed?**
A: In our dummy payment, we create order with PENDING status, then update to SUCCESS on payment confirmation. In real systems, we'd use payment gateway webhooks.

**Q18: How would you handle concurrent cart updates?**
A: 
1. Use optimistic locking on cart items
2. Implement cart version checking
3. Merge conflicts on concurrent updates

**Q19: How do you calculate order total?**
A: Sum of (product.price × quantity) for all order items. Calculated at order placement time and stored in order table (price may change later).

**Q20: Why store price in order_items when it's already in products?**
A: Product price may change over time. Order should reflect price at time of purchase for historical accuracy.

### Architecture Questions

**Q21: Why separate frontend and backend?**
A:
- Independent development and deployment
- Different teams can work in parallel
- Frontend can be mobile app later
- Better scalability

**Q22: What is REST API?**
A: Architectural style for web services using HTTP methods (GET, POST, PUT, DELETE) with stateless communication and resource-based URLs.

**Q23: What is CORS and why did you configure it?**
A: Cross-Origin Resource Sharing - security feature that allows frontend (localhost:3000) to call backend (localhost:8080) despite different origins.

**Q24: How would you scale this application?**
A:
- Frontend: CDN, caching, code splitting
- Backend: Load balancer, multiple instances
- Database: Read replicas, sharding
- Cache: Redis for frequently accessed data

**Q25: How would you secure this application?**
A:
- Use JWT tokens instead of storing user in localStorage
- HTTPS in production
- Input validation and sanitization
- SQL injection prevention (using JPA)
- Rate limiting on APIs

---

## Troubleshooting Guide

### Backend Issues

#### Problem: "Connection refused to localhost:3306"
**Solution:**
1. Check if MySQL is running: `sudo systemctl status mysql`
2. Start MySQL: `sudo systemctl start mysql`
3. Verify credentials in application.properties
4. Check if database exists: `mysql -u root -p -e "SHOW DATABASES;"`

#### Problem: "Table doesn't exist"
**Solution:**
1. Set `spring.jpa.hibernate.ddl-auto=update` in application.properties
2. Restart application to trigger auto-creation
3. Or manually run: `CREATE DATABASE retail_db;`

#### Problem: "Port 8080 already in use"
**Solution:**
```bash
# Find process using port 8080
lsof -i :8080

# Kill the process
kill -9 <PID>

# Or change port in application.properties
server.port=8081
```

#### Problem: "Data.sql not executing"
**Solution:**
1. Check `spring.sql.init.mode=always` is set
2. Check `spring.jpa.defer-datasource-initialization=true`
3. Ensure data.sql is in src/main/resources
4. Check for SQL syntax errors

### Frontend Issues

#### Problem: "npm start fails"
**Solution:**
```bash
# Clear npm cache
npm cache clean --force

# Remove node_modules and reinstall
rm -rf node_modules package-lock.json
npm install

# Start with different port if 3000 is busy
PORT=3001 npm start
```

#### Problem: "Cannot connect to backend"
**Solution:**
1. Check backend is running: `curl http://localhost:8080/api/products`
2. Check CORS configuration in CorsConfig.java
3. Check API_BASE_URL in api.js matches backend port
4. Check browser console for CORS errors

#### Problem: "React app shows blank page"
**Solution:**
1. Check browser console for errors
2. Verify all imports are correct
3. Check if index.js imports App.js
4. Verify public/index.html has `<div id="root"></div>`

### Database Issues

#### Problem: "Duplicate entry for key 'PRIMARY'"
**Solution:**
1. Check if trying to insert duplicate ID
2. Ensure auto-increment is set: `AUTO_INCREMENT=1`
3. Truncate table if needed: `TRUNCATE TABLE products;`

#### Problem: "Foreign key constraint fails"
**Solution:**
1. Check if referenced record exists
2. Ensure proper insert order (parent before child)
3. Temporarily disable checks: `SET FOREIGN_KEY_CHECKS=0;`

#### Problem: "Data not persisting after restart"
**Solution:**
1. Check MySQL service is running
2. Verify database name in connection string
3. Check user has proper permissions
4. Ensure `ddl-auto=update` not `create`

### Common Errors

#### "Cannot add or update a child row: a foreign key constraint fails"
**Cause:** Trying to insert cart item with non-existent user or product
**Fix:** Ensure user and product exist before adding to cart

#### "Data too long for column 'email'"
**Cause:** Email exceeds column length (default 255)
**Fix:** Increase column size: `ALTER TABLE users MODIFY email VARCHAR(500);`

#### "Incorrect decimal value for column 'price'"
**Cause:** Trying to insert non-numeric value
**Fix:** Validate price is numeric before API call

### Testing Checklist

Before submission, verify:

- [ ] MySQL is running
- [ ] Database `retail_db` exists
- [ ] Backend starts without errors
- [ ] Swagger UI loads at http://localhost:8080/swagger-ui.html
- [ ] Frontend starts without errors
- [ ] Can register new user
- [ ] Can login as user
- [ ] Can browse products
- [ ] Can add items to cart
- [ ] Can place order
- [ ] Can view orders
- [ ] Can login as admin
- [ ] Can add new product
- [ ] Can update stock
- [ ] Can view all orders
- [ ] All APIs work in Postman

### Debug Mode

Enable detailed logging in application.properties:
```properties
logging.level.com.retail=DEBUG
logging.level.org.springframework.web=DEBUG
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
```

This will show all SQL queries and parameter values in console.

---

## Conclusion

This project demonstrates a complete full-stack application with:
- Clean layered architecture
- Proper database design
- RESTful API implementation
- Responsive frontend
- Business logic implementation
- Error handling
- Transaction management

The code is beginner-friendly yet follows best practices suitable for production-like systems.