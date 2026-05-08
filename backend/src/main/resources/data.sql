-- Insert sample users if not exists
INSERT INTO users (name, email, password, role) 
SELECT 'Admin User', 'admin@retail.com', 'admin123', 'ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'admin@retail.com');

INSERT INTO users (name, email, password, role) 
SELECT 'John Customer', 'user@retail.com', 'user123', 'USER'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'user@retail.com');

-- Insert sample products if not exists
-- Cold Drinks (Blue themed)
INSERT INTO products (product_id, product_name, category, price, image_url, stock_quantity)
SELECT 'COLD001', 'Coca Cola', 'cooldrinks', 2.50, 'https://via.placeholder.com/200x200/FF0000/FFFFFF?text=Coca+Cola', 50
WHERE NOT EXISTS (SELECT 1 FROM products WHERE product_id = 'COLD001');

INSERT INTO products (product_id, product_name, category, price, image_url, stock_quantity)
SELECT 'COLD002', 'Pepsi', 'cooldrinks', 2.00, 'https://via.placeholder.com/200x200/0000FF/FFFFFF?text=Pepsi', 45
WHERE NOT EXISTS (SELECT 1 FROM products WHERE product_id = 'COLD002');

INSERT INTO products (product_id, product_name, category, price, image_url, stock_quantity)
SELECT 'COLD003', 'Sprite', 'cooldrinks', 2.00, 'https://via.placeholder.com/200x200/00AA00/FFFFFF?text=Sprite', 40
WHERE NOT EXISTS (SELECT 1 FROM products WHERE product_id = 'COLD003');

INSERT INTO products (product_id, product_name, category, price, image_url, stock_quantity)
SELECT 'COLD004', 'Fanta', 'cooldrinks', 2.00, 'https://via.placeholder.com/200x200/FF8800/FFFFFF?text=Fanta', 35
WHERE NOT EXISTS (SELECT 1 FROM products WHERE product_id = 'COLD004');

INSERT INTO products (product_id, product_name, category, price, image_url, stock_quantity)
SELECT 'COLD005', 'Mountain Dew', 'cooldrinks', 2.50, 'https://via.placeholder.com/200x200/00DD00/000000?text=Mountain+Dew', 30
WHERE NOT EXISTS (SELECT 1 FROM products WHERE product_id = 'COLD005');

-- Veg Products (Green themed - Pizzas and Breads)
INSERT INTO products (product_id, product_name, category, price, image_url, stock_quantity)
SELECT 'VEG001', 'Margherita Pizza', 'veg', 8.99, 'https://via.placeholder.com/200x200/FFD700/000000?text=Margherita+Pizza', 25
WHERE NOT EXISTS (SELECT 1 FROM products WHERE product_id = 'VEG001');

INSERT INTO products (product_id, product_name, category, price, image_url, stock_quantity)
SELECT 'VEG002', 'Farmhouse Pizza', 'veg', 10.99, 'https://via.placeholder.com/200x200/228B22/FFFFFF?text=Farmhouse+Pizza', 20
WHERE NOT EXISTS (SELECT 1 FROM products WHERE product_id = 'VEG002');

INSERT INTO products (product_id, product_name, category, price, image_url, stock_quantity)
SELECT 'VEG003', 'Paneer Tikka Pizza', 'veg', 12.99, 'https://via.placeholder.com/200x200/FF6347/FFFFFF?text=Paneer+Tikka', 15
WHERE NOT EXISTS (SELECT 1 FROM products WHERE product_id = 'VEG003');

INSERT INTO products (product_id, product_name, category, price, image_url, stock_quantity)
SELECT 'VEG004', 'Garlic Bread', 'veg', 3.99, 'https://via.placeholder.com/200x200/F5DEB3/000000?text=Garlic+Bread', 50
WHERE NOT EXISTS (SELECT 1 FROM products WHERE product_id = 'VEG004');

INSERT INTO products (product_id, product_name, category, price, image_url, stock_quantity)
SELECT 'VEG005', 'Cheese Breadsticks', 'veg', 4.99, 'https://via.placeholder.com/200x200/FFA500/000000?text=Cheese+Bread', 40
WHERE NOT EXISTS (SELECT 1 FROM products WHERE product_id = 'VEG005');

INSERT INTO products (product_id, product_name, category, price, image_url, stock_quantity)
SELECT 'VEG006', 'Whole Wheat Bread', 'veg', 2.99, 'https://via.placeholder.com/200x200/D2691E/FFFFFF?text=Wheat+Bread', 60
WHERE NOT EXISTS (SELECT 1 FROM products WHERE product_id = 'VEG006');

-- Non-Veg Products (Red themed - Pizzas)
INSERT INTO products (product_id, product_name, category, price, image_url, stock_quantity)
SELECT 'NON001', 'Chicken Pepperoni Pizza', 'non-veg', 14.99, 'https://via.placeholder.com/200x200/DC143C/FFFFFF?text=Pepperoni+Pizza', 20
WHERE NOT EXISTS (SELECT 1 FROM products WHERE product_id = 'NON001');

INSERT INTO products (product_id, product_name, category, price, image_url, stock_quantity)
SELECT 'NON002', 'Chicken Tikka Pizza', 'non-veg', 13.99, 'https://via.placeholder.com/200x200/B22222/FFFFFF?text=Chicken+Tikka', 18
WHERE NOT EXISTS (SELECT 1 FROM products WHERE product_id = 'NON002');

INSERT INTO products (product_id, product_name, category, price, image_url, stock_quantity)
SELECT 'NON003', 'BBQ Chicken Pizza', 'non-veg', 15.99, 'https://via.placeholder.com/200x200/8B0000/FFFFFF?text=BBQ+Chicken', 15
WHERE NOT EXISTS (SELECT 1 FROM products WHERE product_id = 'NON003');

INSERT INTO products (product_id, product_name, category, price, image_url, stock_quantity)
SELECT 'NON004', 'Chicken Sausage Pizza', 'non-veg', 12.99, 'https://via.placeholder.com/200x200/A52A2A/FFFFFF?text=Sausage+Pizza', 22
WHERE NOT EXISTS (SELECT 1 FROM products WHERE product_id = 'NON004');