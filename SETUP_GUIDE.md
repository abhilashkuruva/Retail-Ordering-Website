# Retail Ordering Website - Complete Setup Guide

## Quick Start (5 Minutes)

### Prerequisites
- Java 21+ installed
- Node.js 18+ installed
- MySQL 8+ running
- Maven 3.6+ installed

### Step 1: Configure MySQL
```sql
-- Open MySQL command line or Workbench
-- The application will auto-create database, but ensure MySQL is running

-- If you want to manually create database:
CREATE DATABASE IF NOT EXISTS retail_db;

-- Verify connection works:
-- Host: localhost
-- Port: 3306
-- Username: root
-- Password: password
```

### Step 2: Start Backend
```bash
# Navigate to backend folder
cd backend

# Clean and build
mvn clean install

# Run application
mvn spring-boot:run

# Wait for this message:
# "Retail Ordering Website Started!"
# "Swagger UI: http://localhost:8080/swagger-ui.html"
```

### Step 3: Start Frontend
```bash
# Open new terminal, navigate to frontend folder
cd frontend

# Install dependencies (first time only)
npm install

# Start development server
npm start

# Browser will open automatically to http://localhost:3000
```

### Step 4: Test the Application

#### Test as User:
1. Go to http://localhost:3000
2. Click "Login" → Login with:
   - Email: `user@retail.com`
   - Password: `user123`
3. Browse products
4. Add items to cart
5. Place order
6. View orders

#### Test as Admin:
1. Go to http://localhost:3000
2. Click "Admin" → Login with:
   - Email: `admin@retail.com`
   - Password: `admin123`
3. View dashboard
4. Add/update products
5. View all orders

### Step 5: Verify APIs with Swagger
1. Open http://localhost:8080/swagger-ui.html
2. Test any API endpoint
3. View request/response schemas

---

## Detailed Setup Instructions

### For Windows Users

#### Install Java 21
1. Download from [Oracle](https://www.oracle.com/java/technologies/downloads/)
2. Install and set JAVA_HOME
3. Verify: `java -version`

#### Install Node.js
1. Download from [Node.js](https://nodejs.org/)
2. Install (includes npm)
3. Verify: `node --version` and `npm --version`

#### Install MySQL
1. Download [MySQL Community Server](https://dev.mysql.com/downloads/mysql/)
2. During installation, set root password as `password`
3. Or update password in `backend/src/main/resources/application.properties`

#### Install Maven
1. Download from [Apache Maven](https://maven.apache.org/download.cgi)
2. Extract to C:\apache-maven
3. Add to PATH: `C:\apache-maven\bin`
4. Verify: `mvn --version`

### For Mac/Linux Users

```bash
# Install Java 21 (Mac)
brew install openjdk@21

# Install Java 21 (Ubuntu/Debian)
sudo apt update
sudo apt install openjdk-21-jdk

# Install Node.js (Mac)
brew install node

# Install Node.js (Ubuntu/Debian)
curl -fsSL https://deb.nodesource.com/setup_18.x | sudo -E bash -
sudo apt-get install -y nodejs

# Install MySQL (Mac)
brew install mysql

# Install MySQL (Ubuntu/Debian)
sudo apt install mysql-server

# Install Maven (Mac)
brew install maven

# Install Maven (Ubuntu/Debian)
sudo apt install maven
```

---

## Troubleshooting Common Issues

### Issue: "Cannot connect to MySQL"
**Solution:**
```bash
# Check if MySQL is running
# Windows:
services.msc → Look for MySQL service

# Mac:
brew services list

# Linux:
sudo systemctl status mysql

# Start MySQL if not running
# Mac:
brew services start mysql

# Linux:
sudo systemctl start mysql
```

### Issue: "Port 8080 already in use"
**Solution:**
```bash
# Find process using port 8080
# Windows:
netstat -ano | findstr :8080

# Mac/Linux:
lsof -i :8080

# Kill the process
# Windows:
taskkill /PID <PID> /F

# Mac/Linux:
kill -9 <PID>

# Or change port in backend/src/main/resources/application.properties
server.port=8081
```

### Issue: "Port 3000 already in use"
**Solution:**
```bash
# Start frontend on different port
# Mac/Linux:
PORT=3001 npm start

# Windows (Command Prompt):
set PORT=3001 && npm start

# Windows (PowerShell):
$env:PORT="3001"; npm start
```

### Issue: "npm install fails"
**Solution:**
```bash
# Clear npm cache
npm cache clean --force

# Remove node_modules
rm -rf node_modules package-lock.json

# Reinstall
npm install

# If still failing, try:
npm install --legacy-peer-deps
```

### Issue: "mvn clean install fails"
**Solution:**
```bash
# Check Java version
java -version

# Should be Java 21+
# If wrong version, update JAVA_HOME

# Clear Maven cache
rm -rf ~/.m2/repository/com/retail

# Try again
mvn clean install -U
```

### Issue: "Database not created"
**Solution:**
```sql
-- Manually create database
mysql -u root -p
CREATE DATABASE retail_db;
exit;

-- Verify in application.properties:
-- spring.datasource.url=jdbc:mysql://localhost:3306/retail_db
-- spring.jpa.hibernate.ddl-auto=update
```

### Issue: "Sample data not loaded"
**Solution:**
1. Check `backend/src/main/resources/data.sql` exists
2. Check these lines in `application.properties`:
   ```properties
   spring.sql.init.mode=always
   spring.jpa.defer-datasource-initialization=true
   ```
3. Restart backend

---

## Database Credentials

Default configuration in `backend/src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/retail_db
spring.datasource.username=root
spring.datasource.password=password
```

If your MySQL password is different, update the `password` field.

---

## Sample Data

After first run, database will contain:

### Users
| Email | Password | Role |
|-------|----------|------|
| admin@retail.com | admin123 | ADMIN |
| user@retail.com | user123 | USER |

### Products (15 total)
- **Cold Drinks (5)**: Coca Cola, Pepsi, Sprite, Fanta, Mountain Dew
- **Veg (6)**: Margherita Pizza, Farmhouse Pizza, Paneer Tikka Pizza, Garlic Bread, Cheese Breadsticks, Whole Wheat Bread
- **Non-Veg (4)**: Chicken Pepperoni Pizza, Chicken Tikka Pizza, BBQ Chicken Pizza, Chicken Sausage Pizza

---

## API Testing with Postman

1. Import `docs/RetailOrderingAPI.postman_collection.json` into Postman
2. Run backend server
3. Execute requests in order:
   - Authentication → Login User
   - Products → Get All Products
   - Cart → Add to Cart
   - Orders → Place Order

---

## Verification Checklist

### Backend Verification
```bash
# 1. Check backend is running
curl http://localhost:8080/api/products

# Should return JSON with products list

# 2. Check Swagger
# Open http://localhost:8080/swagger-ui.html in browser

# 3. Check database
mysql -u root -p -e "USE retail_db; SHOW TABLES;"
```

### Frontend Verification
```bash
# 1. Check frontend is running
curl http://localhost:3000

# Should return HTML

# 2. Open browser
# Navigate to http://localhost:3000
```

### Integration Verification
1. Login as user
2. Add product to cart
3. Check cart shows item
4. Place order
5. Check order appears in Orders page
6. Login as admin
7. Check order appears in dashboard
8. Update product stock
9. Check stock updated in product list

---

## Development Commands

### Backend
```bash
# Build
mvn clean install

# Run
mvn spring-boot:run

# Run tests
mvn test

# Package as JAR
mvn package

# Run JAR
java -jar target/retail-ordering-backend-1.0.0.jar
```

### Frontend
```bash
# Install dependencies
npm install

# Start dev server
npm start

# Build for production
npm run build

# Run tests
npm test

# Check for issues
npm run lint
```

---

## Production Deployment

### Backend (JAR Deployment)
```bash
# Build JAR
mvn clean package

# Run JAR
java -jar target/retail-ordering-backend-1.0.0.jar

# Or as service (Linux)
sudo cp target/retail-ordering-backend-1.0.0.jar /opt/retail-ordering.jar
sudo nano /etc/systemd/system/retail-ordering.service
# Add service configuration
sudo systemctl start retail-ordering
```

### Frontend (Static Build)
```bash
# Build production bundle
npm run build

# Copy build folder to web server
cp -r build/* /var/www/html/

# Or deploy to Netlify/Vercel
# Push to GitHub and connect to Netlify/Vercel
```

### Database
```sql
-- Use production MySQL credentials
-- Update application.properties with production values
spring.datasource.url=jdbc:mysql://production-host:3306/retail_db
spring.datasource.username=prod_user
spring.datasource.password=prod_password

-- For production, change ddl-auto to 'validate'
spring.jpa.hibernate.ddl-auto=validate
```

---

## Additional Resources

### Documentation Files
- `README.md` - Project overview
- `IMPLEMENTATION_GUIDE.md` - Complete code guide
- `docs/DETAILED_DOCUMENTATION.md` - Comprehensive documentation
- `docs/RetailOrderingAPI.postman_collection.json` - API testing

### Useful Commands

```bash
# Check Java version
java -version

# Check Node version
node --version

# Check Maven version
mvn --version

# Check MySQL version
mysql --version

# View running processes (port usage)
# Windows:
netstat -ano

# Mac/Linux:
lsof -i

# Kill process by port
# Windows:
taskkill /F /PID <PID>

# Mac/Linux:
kill -9 <PID>
```

---

## Support

If you encounter any issues:

1. Check the Troubleshooting section above
2. Review `docs/DETAILED_DOCUMENTATION.md`
3. Check backend logs in console
4. Check frontend console (F12 → Console)
5. Verify all prerequisites are installed correctly

---

## Success Criteria

Your application is working correctly if:

✅ Backend starts without errors
✅ Swagger UI loads at http://localhost:8080/swagger-ui.html
✅ Frontend starts without errors
✅ Can access http://localhost:3000
✅ Can login as user (user@retail.com / user123)
✅ Can browse products
✅ Can add items to cart
✅ Can place order
✅ Can view orders
✅ Can login as admin (admin@retail.com / admin123)
✅ Can view admin dashboard
✅ Can add new product
✅ Can update stock
✅ Can view all orders

**Congratulations! Your Retail Ordering Website is ready for the hackathon!** 🎉