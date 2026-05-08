# Retail Ordering Website - Java Full Stack Hackathon Project

## Project Overview

A simple yet fully functional e-commerce platform for ordering pizzas, cold drinks, and breads. Built with Spring Boot backend and React frontend, this project demonstrates core full-stack development concepts perfect for hackathon submissions.

## Problem Statement

Customers need a seamless way to browse, order, and receive food products (Pizza, Cold Drinks, and Breads) with:
- Automatic inventory management
- User-friendly search and ordering interface
- Admin panel for product and inventory management
- Simulated payment confirmation
- Real-time stock updates

## Tech Stack

**Backend:**
- Java 21
- Spring Boot 3.x
- Spring Data JPA & Hibernate
- Maven
- MySQL

**Frontend:**
- React 18
- Axios
- Bootstrap 5

**Tools:**
- Swagger for API documentation
- JUnit for testing
- Postman for API testing

## Quick Start

### Prerequisites
- Java 21+
- Node.js 18+
- MySQL 8+
- Maven 3.6+

### Database Setup
```sql
-- MySQL will auto-create database and tables
-- Configuration: localhost:3306, root/password
```

### Backend Setup
```bash
cd backend
mvn clean install
mvn spring-boot:run
```
Backend runs on: http://localhost:8080

### Frontend Setup
```bash
cd frontend
npm install
npm start
```
Frontend runs on: http://localhost:3000

## Features

### User Features
- Browse products by category (Cold Drinks, Veg, Non-Veg)
- Search products
- Add to cart with quantity management
- Place orders with dummy payment
- View order history
- Real-time stock updates

### Admin Features
- Add new products
- Manage inventory (increase/decrease stock)
- View all orders
- Monitor customer activity

## API Endpoints

### Authentication
- POST `/api/auth/register` - Register new user
- POST `/api/auth/login` - User login
- POST `/api/auth/register-admin` - Register admin
- POST `/api/auth/login-admin` - Admin login

### Products
- GET `/api/products` - Get all products
- GET `/api/products/search?keyword={keyword}` - Search products
- GET `/api/products/category/{category}` - Get by category
- POST `/api/products` - Add product (Admin)
- PUT `/api/products/{id}/stock` - Update stock (Admin)

### Cart
- GET `/api/cart` - Get user cart
- POST `/api/cart/add` - Add to cart
- PUT `/api/cart/update` - Update cart quantity
- DELETE `/api/cart/{productId}` - Remove from cart

### Orders
- POST `/api/orders/place` - Place order
- POST `/api/orders/{id}/payment` - Confirm payment
- GET `/api/orders/user` - Get user orders
- GET `/api/orders` - Get all orders (Admin)

## Database Schema

### Tables
1. **users** - User authentication and profiles
2. **products** - Product catalog with inventory
3. **cart** - Shopping cart items
4. **orders** - Order headers
5. **order_items** - Order line items

## Project Structure

```
retail-ordering-website/
├── backend/           # Spring Boot application
│   ├── src/main/java/com/retail/
│   │   ├── controller/    # REST controllers
│   │   ├── service/       # Business logic
│   │   ├── repository/    # Data access layer
│   │   ├── entity/        # JPA entities
│   │   ├── dto/          # Data transfer objects
│   │   └── config/       # Configuration
│   └── src/main/resources/
│       ├── application.properties
│       └── data.sql      # Initial data
├── frontend/          # React application
│   ├── src/
│   │   ├── components/    # React components
│   │   ├── services/      # API services
│   │   └── App.js        # Main app component
│   └── package.json
└── docs/              # Documentation
```

## Documentation

See [docs/](docs/) for detailed documentation including:
- Architecture diagrams
- API flow explanations
- Database design rationale
- Code explanations
- Viva questions and answers
- Troubleshooting guide

## Testing

### Run Backend Tests
```bash
cd backend
mvn test
```

### API Testing
Import `docs/RetailOrderingAPI.postman_collection.json` into Postman

### Swagger Documentation
Access at: http://localhost:8080/swagger-ui.html

## Sample Credentials

### Admin
- Email: admin@retail.com
- Password: admin123

### User
- Email: user@retail.com
- Password: user123

## Common Issues & Solutions

### Database Connection Issues
```properties
# Update application.properties if needed
spring.datasource.url=jdbc:mysql://localhost:3306/retail_db
spring.datasource.username=root
spring.datasource.password=your_password
```

### Port Already in Use
```bash
# Backend
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8081

# Frontend
PORT=3001 npm start
```

## Evaluation Points

This project demonstrates:
- ✅ Clean layered architecture
- ✅ RESTful API design
- ✅ Database normalization
- ✅ Inventory management logic
- ✅ User authentication & authorization
- ✅ Frontend-backend integration
- ✅ Error handling
- ✅ Real-time updates
- ✅ Responsive UI design

## License
Educational use for hackathon submission.