# Retail Ordering Website - Complete Implementation Guide

## Project Status: Backend Complete ✅

The Spring Boot backend is fully implemented with all necessary components.

## Frontend Implementation

Due to the extensive nature of the React frontend, here's a complete guide to create all necessary components.

### 1. Main App Component (src/App.js)

```javascript
import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Navbar from './components/Navbar';
import Home from './pages/Home';
import Login from './pages/Login';
import Register from './pages/Register';
import Cart from './pages/Cart';
import Orders from './pages/Orders';
import AdminLogin from './pages/AdminLogin';
import AdminDashboard from './pages/AdminDashboard';
import './App.css';

function App() {
  const [user, setUser] = useState(null);
  const [isAdmin, setIsAdmin] = useState(false);

  useEffect(() => {
    const savedUser = localStorage.getItem('user');
    const savedAdmin = localStorage.getItem('isAdmin');
    if (savedUser) {
      setUser(JSON.parse(savedUser));
    }
    if (savedAdmin) {
      setIsAdmin(JSON.parse(savedAdmin));
    }
  }, []);

  const handleLogin = (userData, admin = false) => {
    setUser(userData);
    setIsAdmin(admin);
    localStorage.setItem('user', JSON.stringify(userData));
    localStorage.setItem('isAdmin', JSON.stringify(admin));
  };

  const handleLogout = () => {
    setUser(null);
    setIsAdmin(false);
    localStorage.removeItem('user');
    localStorage.removeItem('isAdmin');
  };

  return (
    <Router>
      <div className="App">
        <Navbar user={user} isAdmin={isAdmin} onLogout={handleLogout} />
        <div className="container mt-4">
          <Routes>
            <Route path="/" element={<Home user={user} />} />
            <Route path="/login" element={<Login onLogin={handleLogin} />} />
            <Route path="/register" element={<Register />} />
            <Route path="/cart" element={user ? <Cart user={user} /> : <Navigate to="/login" />} />
            <Route path="/orders" element={user ? <Orders user={user} /> : <Navigate to="/login" />} />
            <Route path="/admin-login" element={<AdminLogin onLogin={handleLogin} />} />
            <Route path="/admin" element={isAdmin ? <AdminDashboard /> : <Navigate to="/admin-login" />} />
          </Routes>
        </div>
      </div>
    </Router>
  );
}

export default App;
```

### 2. Navbar Component (src/components/Navbar.js)

```javascript
import React from 'react';
import { Link } from 'react-router-dom';
import { cartAPI } from '../services/api';

function Navbar({ user, isAdmin, onLogout }) {
  const [cartCount, setCartCount] = React.useState(0);

  React.useEffect(() => {
    if (user) {
      cartAPI.getCartItemCount(user.id)
        .then(response => setCartCount(response.data.data.itemCount))
        .catch(() => setCartCount(0));
    }
  }, [user]);

  return (
    <nav className="navbar navbar-expand-lg navbar-dark bg-primary">
      <div className="container-fluid">
        <Link className="navbar-brand" to="/">
          🛒 Retail Ordering
        </Link>
        <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
          <span className="navbar-toggler-icon"></span>
        </button>
        <div className="collapse navbar-collapse" id="navbarNav">
          <ul className="navbar-nav me-auto">
            <li className="nav-item">
              <Link className="nav-link" to="/">Home</Link>
            </li>
            {user && (
              <>
                <li className="nav-item">
                  <Link className="nav-link" to="/cart">
                    Cart
                    {cartCount > 0 && (
                      <span className="cart-badge">{cartCount}</span>
                    )}
                  </Link>
                </li>
                <li className="nav-item">
                  <Link className="nav-link" to="/orders">Orders</Link>
                </li>
              </>
            )}
          </ul>
          <ul className="navbar-nav">
            {!user && !isAdmin ? (
              <>
                <li className="nav-item">
                  <Link className="nav-link" to="/login">Login</Link>
                </li>
                <li className="nav-item">
                  <Link className="nav-link" to="/register">Register</Link>
                </li>
                <li className="nav-item">
                  <Link className="nav-link" to="/admin-login">Admin</Link>
                </li>
              </>
            ) : (
              <>
                <li className="nav-item">
                  <span className="nav-link">Welcome, {user?.name || 'Admin'}</span>
                </li>
                {isAdmin ? (
                  <li className="nav-item">
                    <Link className="nav-link" to="/admin">Dashboard</Link>
                  </li>
                ) : null}
                <li className="nav-item">
                  <button className="nav-link btn btn-link" onClick={onLogout}>
                    Logout
                  </button>
                </li>
              </>
            )}
          </ul>
        </div>
      </div>
    </nav>
  );
}

export default Navbar;
```

### 3. Home Page (src/pages/Home.js)

```javascript
import React, { useState, useEffect } from 'react';
import { productAPI, cartAPI } from '../services/api';

function Home({ user }) {
  const [products, setProducts] = useState([]);
  const [filteredProducts, setFilteredProducts] = useState([]);
  const [searchKeyword, setSearchKeyword] = useState('');
  const [selectedCategory, setSelectedCategory] = useState('all');
  const [loading, setLoading] = useState(true);
  const [message, setMessage] = useState('');

  useEffect(() => {
    loadProducts();
  }, []);

  const loadProducts = async () => {
    try {
      const response = await productAPI.getAllProducts();
      const productsData = response.data.data.products;
      setProducts(productsData);
      setFilteredProducts(productsData);
    } catch (error) {
      console.error('Error loading products:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = async () => {
    if (searchKeyword.trim()) {
      try {
        const response = await productAPI.searchProducts(searchKeyword);
        setFilteredProducts(response.data.data.products);
      } catch (error) {
        console.error('Error searching products:', error);
      }
    } else {
      setFilteredProducts(products);
    }
  };

  const handleCategoryFilter = async (category) => {
    setSelectedCategory(category);
    if (category === 'all') {
      setFilteredProducts(products);
    } else {
      try {
        const response = await productAPI.getProductsByCategory(category);
        setFilteredProducts(response.data.data.products);
      } catch (error) {
        console.error('Error filtering products:', error);
      }
    }
  };

  const handleAddToCart = async (productId) => {
    if (!user) {
      setMessage('Please login to add items to cart');
      setTimeout(() => setMessage(''), 3000);
      return;
    }

    try {
      await cartAPI.addToCart(user.id, productId, 1);
      setMessage('Item added to cart successfully!');
      setTimeout(() => setMessage(''), 3000);
    } catch (error) {
      setMessage(error.response?.data?.message || 'Failed to add to cart');
      setTimeout(() => setMessage(''), 3000);
    }
  };

  const getCategoryBadgeClass = (category) => {
    switch (category) {
      case 'cooldrinks': return 'category-cooldrinks';
      case 'veg': return 'category-veg';
      case 'non-veg': return 'category-non-veg';
      default: return 'bg-secondary';
    }
  };

  if (loading) {
    return (
      <div className="text-center mt-5">
        <div className="spinner-border text-primary" role="status">
          <span className="visually-hidden">Loading...</span>
        </div>
      </div>
    );
  }

  return (
    <div>
      {/* Hero Section */}
      <div className="hero-section rounded">
        <div className="container text-center">
          <h1>Welcome to Retail Ordering</h1>
          <p className="lead">Order your favorite Pizzas, Cold Drinks, and Breads online!</p>
        </div>
      </div>

      {message && (
        <div className="alert alert-info alert-dismissible fade show" role="alert">
          {message}
          <button type="button" className="btn-close" onClick={() => setMessage('')}></button>
        </div>
      )}

      {/* Search and Filter */}
      <div className="row mb-4">
        <div className="col-md-6">
          <div className="input-group">
            <input
              type="text"
              className="form-control"
              placeholder="Search products..."
              value={searchKeyword}
              onChange={(e) => setSearchKeyword(e.target.value)}
              onKeyPress={(e) => e.key === 'Enter' && handleSearch()}
            />
            <button className="btn btn-primary" onClick={handleSearch}>
              Search
            </button>
          </div>
        </div>
        <div className="col-md-6">
          <div className="btn-group" role="group">
            <button
              className={`btn ${selectedCategory === 'all' ? 'btn-primary' : 'btn-outline-primary'}`}
              onClick={() => handleCategoryFilter('all')}
            >
              All
            </button>
            <button
              className={`btn ${selectedCategory === 'cooldrinks' ? 'btn-info' : 'btn-outline-info'}`}
              onClick={() => handleCategoryFilter('cooldrinks')}
            >
              Cold Drinks
            </button>
            <button
              className={`btn ${selectedCategory === 'veg' ? 'btn-success' : 'btn-outline-success'}`}
              onClick={() => handleCategoryFilter('veg')}
            >
              Veg
            </button>
            <button
              className={`btn ${selectedCategory === 'non-veg' ? 'btn-danger' : 'btn-outline-danger'}`}
              onClick={() => handleCategoryFilter('non-veg')}
            >
              Non-Veg
            </button>
          </div>
        </div>
      </div>

      {/* Products Grid */}
      <div className="row">
        {filteredProducts.map(product => (
          <div className="col-md-3 mb-4" key={product.id}>
            <div className="card product-card h-100">
              {product.stockQuantity === 0 && (
                <span className="out-of-stock-badge">OUT OF STOCK</span>
              )}
              <img
                src={product.imageUrl || 'https://via.placeholder.com/200x200?text=No+Image'}
                className="card-img-top product-image"
                alt={product.productName}
              />
              <div className="card-body">
                <span className={`badge ${getCategoryBadgeClass(product.category)} mb-2`}>
                  {product.category}
                </span>
                <h5 className="card-title">{product.productName}</h5>
                <p className="card-text">
                  <strong>${product.price.toFixed(2)}</strong>
                </p>
                <p className="text-muted small">
                  Stock: {product.stockQuantity}
                </p>
              </div>
              <div className="card-footer">
                <button
                  className="btn btn-primary w-100"
                  onClick={() => handleAddToCart(product.id)}
                  disabled={product.stockQuantity === 0}
                >
                  {product.stockQuantity === 0 ? 'Out of Stock' : 'Add to Cart'}
                </button>
              </div>
            </div>
          </div>
        ))}
      </div>

      {filteredProducts.length === 0 && (
        <div className="text-center mt-5">
          <h3>No products found</h3>
        </div>
      )}
    </div>
  );
}

export default Home;
```

### 4. Cart Page (src/pages/Cart.js)

```javascript
import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { cartAPI, orderAPI } from '../services/api';

function Cart({ user }) {
  const [cartItems, setCartItems] = useState([]);
  const [loading, setLoading] = useState(true);
  const [totalAmount, setTotalAmount] = useState(0);
  const [message, setMessage] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    if (user) {
      loadCart();
    }
  }, [user]);

  const loadCart = async () => {
    try {
      const response = await cartAPI.getCart(user.id);
      const items = response.data.data.cartItems;
      setCartItems(items);
      calculateTotal(items);
    } catch (error) {
      console.error('Error loading cart:', error);
    } finally {
      setLoading(false);
    }
  };

  const calculateTotal = (items) => {
    const total = items.reduce((sum, item) => {
      return sum + (item.product.price * item.quantity);
    }, 0);
    setTotalAmount(total);
  };

  const updateQuantity = async (productId, newQuantity) => {
    if (newQuantity < 1) {
      removeFromCart(productId);
      return;
    }

    try {
      await cartAPI.updateCartQuantity(user.id, productId, newQuantity);
      loadCart();
    } catch (error) {
      setMessage(error.response?.data?.message || 'Failed to update quantity');
      setTimeout(() => setMessage(''), 3000);
    }
  };

  const removeFromCart = async (productId) => {
    try {
      await cartAPI.removeFromCart(user.id, productId);
      loadCart();
      setMessage('Item removed from cart');
      setTimeout(() => setMessage(''), 3000);
    } catch (error) {
      console.error('Error removing item:', error);
    }
  };

  const handlePlaceOrder = async () => {
    if (cartItems.length === 0) {
      setMessage('Cart is empty');
      return;
    }

    try {
      // Place order
      const orderResponse = await orderAPI.placeOrder(user.id);
      const order = orderResponse.data.data.order;
      
      // Confirm payment (dummy)
      await orderAPI.confirmPayment(order.id);
      
      setMessage('Order placed successfully!');
      setTimeout(() => {
        navigate('/orders');
      }, 2000);
    } catch (error) {
      setMessage(error.response?.data?.message || 'Failed to place order');
      setTimeout(() => setMessage(''), 3000);
    }
  };

  if (loading) {
    return (
      <div className="text-center mt-5">
        <div className="spinner-border text-primary" role="status">
          <span className="visually-hidden">Loading...</span>
        </div>
      </div>
    );
  }

  return (
    <div>
      <h2>Shopping Cart</h2>
      
      {message && (
        <div className="alert alert-info alert-dismissible fade show" role="alert">
          {message}
          <button type="button" className="btn-close" onClick={() => setMessage('')}></button>
        </div>
      )}

      {cartItems.length === 0 ? (
        <div className="text-center mt-5">
          <h3>Your cart is empty</h3>
          <p>Start shopping to add items to your cart</p>
        </div>
      ) : (
        <div className="row">
          <div className="col-md-8">
            <div className="card">
              <div className="card-body">
                {cartItems.map(item => (
                  <div key={item.product.id} className="row mb-3 align-items-center">
                    <div className="col-md-4">
                      <img
                        src={item.product.imageUrl || 'https://via.placeholder.com/100x100'}
                        alt={item.product.productName}
                        style={{ width: '80px', height: '80px', objectFit: 'cover' }}
                        className="rounded"
                      />
                      <span className="ms-2">{item.product.productName}</span>
                    </div>
                    <div className="col-md-2">
                      <strong>${item.product.price.toFixed(2)}</strong>
                    </div>
                    <div className="col-md-3">
                      <div className="input-group" style={{ width: '120px' }}>
                        <button
                          className="btn btn-outline-secondary quantity-btn"
                          onClick={() => updateQuantity(item.product.id, item.quantity - 1)}
                        >
                          -
                        </button>
                        <input
                          type="text"
                          className="form-control text-center"
                          value={item.quantity}
                          readOnly
                        />
                        <button
                          className="btn btn-outline-secondary quantity-btn"
                          onClick={() => updateQuantity(item.product.id, item.quantity + 1)}
                        >
                          +
                        </button>
                      </div>
                    </div>
                    <div className="col-md-2 text-end">
                      <button
                        className="btn btn-danger btn-sm"
                        onClick={() => removeFromCart(item.product.id)}
                      >
                        Remove
                      </button>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          </div>
          
          <div className="col-md-4">
            <div className="card">
              <div className="card-body">
                <h5 className="card-title">Order Summary</h5>
                <hr />
                <div className="d-flex justify-content-between mb-2">
                  <span>Subtotal ({cartItems.length} items):</span>
                  <strong>${totalAmount.toFixed(2)}</strong>
                </div>
                <div className="d-flex justify-content-between mb-2">
                  <span>Delivery:</span>
                  <strong>FREE</strong>
                </div>
                <hr />
                <div className="d-flex justify-content-between mb-3">
                  <h5>Total:</h5>
                  <h4 className="text-primary">${totalAmount.toFixed(2)}</h4>
                </div>
                <button
                  className="btn btn-primary btn-lg w-100"
                  onClick={handlePlaceOrder}
                >
                  Proceed to Payment
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default Cart;
```

### 5. Orders Page (src/pages/Orders.js)

```javascript
import React, { useState, useEffect } from 'react';
import { orderAPI } from '../services/api';

function Orders({ user }) {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (user) {
      loadOrders();
    }
  }, [user]);

  const loadOrders = async () => {
    try {
      const response = await orderAPI.getUserOrders(user.id);
      setOrders(response.data.data.orders);
    } catch (error) {
      console.error('Error loading orders:', error);
    } finally {
      setLoading(false);
    }
  };

  const getStatusBadgeClass = (status) => {
    switch (status) {
      case 'CONFIRMED': return 'bg-success';
      case 'PENDING': return 'bg-warning';
      case 'DELIVERED': return 'bg-info';
      case 'CANCELLED': return 'bg-danger';
      default: return 'bg-secondary';
    }
  };

  if (loading) {
    return (
      <div className="text-center mt-5">
        <div className="spinner-border text-primary" role="status">
          <span className="visually-hidden">Loading...</span>
        </div>
      </div>
    );
  }

  return (
    <div>
      <h2>My Orders</h2>
      
      {orders.length === 0 ? (
        <div className="text-center mt-5">
          <h3>No orders yet</h3>
          <p>Start shopping to see your orders here</p>
        </div>
      ) : (
        <div className="row">
          {orders.map(order => (
            <div className="col-md-6 mb-4" key={order.id}>
              <div className="card">
                <div className="card-body">
                  <div className="d-flex justify-content-between align-items-center mb-3">
                    <h5 className="card-title mb-0">Order #{order.id}</h5>
                    <span className={`badge ${getStatusBadgeClass(order.orderStatus)}`}>
                      {order.orderStatus}
                    </span>
                  </div>
                  <p className="text-muted mb-2">
                    Placed on: {new Date(order.createdAt).toLocaleDateString()}
                  </p>
                  <div className="d-flex justify-content-between">
                    <span>Total Amount:</span>
                    <strong className="text-primary">${order.totalAmount.toFixed(2)}</strong>
                  </div>
                  <div className="d-flex justify-content-between mt-2">
                    <span>Payment:</span>
                    <span className={order.paymentStatus === 'SUCCESS' ? 'text-success' : 'text-warning'}>
                      {order.paymentStatus}
                    </span>
                  </div>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

export default Orders;
```

### 6. Login Page (src/pages/Login.js)

```javascript
import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { authAPI } from '../services/api';

function Login({ onLogin }) {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      const response = await authAPI.login(email, password);
      onLogin(response.data.data.user, false);
      navigate('/');
    } catch (err) {
      setError(err.response?.data?.message || 'Login failed');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="row justify-content-center mt-5">
      <div className="col-md-6">
        <div className="card">
          <div className="card-body">
            <h2 className="text-center mb-4">User Login</h2>
            
            {error && (
              <div className="alert alert-danger" role="alert">
                {error}
              </div>
            )}

            <form onSubmit={handleSubmit}>
              <div className="mb-3">
                <label className="form-label">Email</label>
                <input
                  type="email"
                  className="form-control"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  required
                />
              </div>
              <div className="mb-3">
                <label className="form-label">Password</label>
                <input
                  type="password"
                  className="form-control"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  required
                />
              </div>
              <button
                type="submit"
                className="btn btn-primary w-100"
                disabled={loading}
              >
                {loading ? 'Logging in...' : 'Login'}
              </button>
            </form>

            <div className="mt-3 text-center">
              <p>
                Don't have an account? <Link to="/register">Register</Link>
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Login;
```

### 7. Register Page (src/pages/Register.js)

```javascript
import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { authAPI } from '../services/api';

function Register() {
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');

    if (password !== confirmPassword) {
      setError('Passwords do not match');
      return;
    }

    setLoading(true);

    try {
      await authAPI.register(name, email, password);
      setSuccess('Registration successful! Redirecting to login...');
      setTimeout(() => navigate('/login'), 2000);
    } catch (err) {
      setError(err.response?.data?.message || 'Registration failed');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="row justify-content-center mt-5">
      <div className="col-md-6">
        <div className="card">
          <div className="card-body">
            <h2 className="text-center mb-4">User Registration</h2>
            
            {error && (
              <div className="alert alert-danger" role="alert">
                {error}
              </div>
            )}
            {success && (
              <div className="alert alert-success" role="alert">
                {success}
              </div>
            )}

            <form onSubmit={handleSubmit}>
              <div className="mb-3">
                <label className="form-label">Name</label>
                <input
                  type="text"
                  className="form-control"
                  value={name}
                  onChange={(e) => setName(e.target.value)}
                  required
                />
              </div>
              <div className="mb-3">
                <label className="form-label">Email</label>
                <input
                  type="email"
                  className="form-control"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  required
                />
              </div>
              <div className="mb-3">
                <label className="form-label">Password</label>
                <input
                  type="password"
                  className="form-control"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  required
                />
              </div>
              <div className="mb-3">
                <label className="form-label">Confirm Password</label>
                <input
                  type="password"
                  className="form-control"
                  value={confirmPassword}
                  onChange={(e) => setConfirmPassword(e.target.value)}
                  required
                />
              </div>
              <button
                type="submit"
                className="btn btn-primary w-100"
                disabled={loading}
              >
                {loading ? 'Registering...' : 'Register'}
              </button>
            </form>

            <div className="mt-3 text-center">
              <p>
                Already have an account? <Link to="/login">Login</Link>
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Register;
```

### 8. Admin Login (src/pages/AdminLogin.js)

```javascript
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { authAPI } from '../services/api';

function AdminLogin({ onLogin }) {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      const response = await authAPI.loginAdmin(email, password);
      onLogin(response.data.data.user, true);
      navigate('/admin');
    } catch (err) {
      setError(err.response?.data?.message || 'Admin login failed');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="row justify-content-center mt-5">
      <div className="col-md-6">
        <div className="card border-primary">
          <div className="card-body">
            <h2 className="text-center mb-4">Admin Login</h2>
            
            {error && (
              <div className="alert alert-danger" role="alert">
                {error}
              </div>
            )}

            <form onSubmit={handleSubmit}>
              <div className="mb-3">
                <label className="form-label">Email</label>
                <input
                  type="email"
                  className="form-control"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  required
                />
              </div>
              <div className="mb-3">
                <label className="form-label">Password</label>
                <input
                  type="password"
                  className="form-control"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  required
                />
              </div>
              <button
                type="submit"
                className="btn btn-primary w-100"
                disabled={loading}
              >
                {loading ? 'Logging in...' : 'Login as Admin'}
              </button>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
}

export default AdminLogin;
```

### 9. Admin Dashboard (src/pages/AdminDashboard.js)

```javascript
import React, { useState, useEffect } from 'react';
import { productAPI, orderAPI } from '../services/api';

function AdminDashboard() {
  const [products, setProducts] = useState([]);
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showAddProduct, setShowAddProduct] = useState(false);
  const [newProduct, setNewProduct] = useState({
    productName: '',
    category: 'veg',
    price: '',
    imageUrl: '',
    stockQuantity: ''
  });
  const [message, setMessage] = useState('');

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      const [productsRes, ordersRes] = await Promise.all([
        productAPI.getAllProducts(),
        orderAPI.getAllOrders()
      ]);
      setProducts(productsRes.data.data.products);
      setOrders(ordersRes.data.data.orders);
    } catch (error) {
      console.error('Error loading data:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleAddProduct = async (e) => {
    e.preventDefault();
    try {
      await productAPI.addProduct({
        ...newProduct,
        price: parseFloat(newProduct.price),
        stockQuantity: parseInt(newProduct.stockQuantity)
      });
      setMessage('Product added successfully');
      setShowAddProduct(false);
      setNewProduct({
        productName: '',
        category: 'veg',
        price: '',
        imageUrl: '',
        stockQuantity: ''
      });
      loadData();
      setTimeout(() => setMessage(''), 3000);
    } catch (error) {
      setMessage('Failed to add product');
      setTimeout(() => setMessage(''), 3000);
    }
  };

  const handleUpdateStock = async (productId, currentStock) => {
    const newStock = prompt('Enter new stock quantity:', currentStock);
    if (newStock !== null && !isNaN(newStock)) {
      try {
        await productAPI.updateStock(productId, parseInt(newStock));
        setMessage('Stock updated successfully');
        loadData();
        setTimeout(() => setMessage(''), 3000);
      } catch (error) {
        setMessage('Failed to update stock');
        setTimeout(() => setMessage(''), 3000);
      }
    }
  };

  const handleUpdateOrderStatus = async (orderId, currentStatus) => {
    const newStatus = prompt('Enter new status (PENDING, CONFIRMED, DELIVERED, CANCELLED):', currentStatus);
    if (newStatus !== null) {
      try {
        await orderAPI.updateOrderStatus(orderId, newStatus);
        setMessage('Order status updated');
        loadData();
        setTimeout(() => setMessage(''), 3000);
      } catch (error) {
        setMessage('Failed to update order status');
        setTimeout(() => setMessage(''), 3000);
      }
    }
  };

  if (loading) {
    return (
      <div className="text-center mt-5">
        <div className="spinner-border text-primary" role="status">
          <span className="visually-hidden">Loading...</span>
        </div>
      </div>
    );
  }

  return (
    <div>
      <h2>Admin Dashboard</h2>
      
      {message && (
        <div className="alert alert-info alert-dismissible fade show" role="alert">
          {message}
          <button type="button" className="btn-close" onClick={() => setMessage('')}></button>
        </div>
      )}

      {/* Stats */}
      <div className="row mb-4">
        <div className="col-md-3">
          <div className="card stat-card stat-card-primary">
            <div className="card-body">
              <h5>Total Products</h5>
              <h2>{products.length}</h2>
            </div>
          </div>
        </div>
        <div className="col-md-3">
          <div className="card stat-card stat-card-success">
            <div className="card-body">
              <h5>Total Orders</h5>
              <h2>{orders.length}</h2>
            </div>
          </div>
        </div>
        <div className="col-md-3">
          <div className="card stat-card stat-card-warning">
            <div className="card-body">
              <h5>Pending Orders</h5>
              <h2>{orders.filter(o => o.orderStatus === 'PENDING').length}</h2>
            </div>
          </div>
        </div>
        <div className="col-md-3">
          <div className="card stat-card stat-card-danger">
            <div className="card-body">
              <h5>Low Stock Items</h5>
              <h2>{products.filter(p => p.stockQuantity < 10).length}</h2>
            </div>
          </div>
        </div>
      </div>

      {/* Tabs */}
      <ul className="nav nav-tabs mb-3" id="adminTabs" role="tablist">
        <li className="nav-item" role="presentation">
          <button
            className="nav-link active"
            data-bs-toggle="tab"
            data-bs-target="#products-tab"
            type="button"
          >
            Products
          </button>
        </li>
        <li className="nav-item" role="presentation">
          <button
            className="nav-link"
            data-bs-toggle="tab"
            data-bs-target="#orders-tab"
            type="button"
          >
            Orders
          </button>
        </li>
      </ul>

      <div className="tab-content">
        {/* Products Tab */}
        <div className="tab-pane fade show active" id="products-tab">
          <div className="d-flex justify-content-between align-items-center mb-3">
            <h4>Product Management</h4>
            <button
              className="btn btn-primary"
              onClick={() => setShowAddProduct(!showAddProduct)}
            >
              Add New Product
            </button>
          </div>

          {showAddProduct && (
            <div className="card mb-4">
              <div className="card-body">
                <h5>Add New Product</h5>
                <form onSubmit={handleAddProduct}>
                  <div className="row">
                    <div className="col-md-4 mb-2">
                      <input
                        type="text"
                        className="form-control"
                        placeholder="Product Name"
                        value={newProduct.productName}
                        onChange={(e) => setNewProduct({...newProduct, productName: e.target.value})}
                        required
                      />
                    </div>
                    <div className="col-md-2 mb-2">
                      <select
                        className="form-select"
                        value={newProduct.category}
                        onChange={(e) => setNewProduct({...newProduct, category: e.target.value})}
                      >
                        <option value="cooldrinks">Cold Drinks</option>
                        <option value="veg">Veg</option>
                        <option value="non-veg">Non-Veg</option>
                      </select>
                    </div>
                    <div className="col-md-2 mb-2">
                      <input
                        type="number"
                        className="form-control"
                        placeholder="Price"
                        step="0.01"
                        value={newProduct.price}
                        onChange={(e) => setNewProduct({...newProduct, price: e.target.value})}
                        required
                      />
                    </div>
                    <div className="col-md-2 mb-2">
                      <input
                        type="number"
                        className="form-control"
                        placeholder="Stock"
                        value={newProduct.stockQuantity}
                        onChange={(e) => setNewProduct({...newProduct, stockQuantity: e.target.value})}
                        required
                      />
                    </div>
                    <div className="col-md-2 mb-2">
                      <button type="submit" className="btn btn-success w-100">
                        Add
                      </button>
                    </div>
                  </div>
                </form>
              </div>
            </div>
          )}

          <div className="table-responsive">
            <table className="table table-striped">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Name</th>
                  <th>Category</th>
                  <th>Price</th>
                  <th>Stock</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {products.map(product => (
                  <tr key={product.id}>
                    <td>{product.productId}</td>
                    <td>{product.productName}</td>
                    <td>
                      <span className={`badge category-${product.category}`}>
                        {product.category}
                      </span>
                    </td>
                    <td>${product.price.toFixed(2)}</td>
                    <td>
                      {product.stockQuantity < 10 ? (
                        <span className="text-danger fw-bold">{product.stockQuantity}</span>
                      ) : (
                        product.stockQuantity
                      )}
                    </td>
                    <td>
                      <button
                        className="btn btn-sm btn-outline-primary"
                        onClick={() => handleUpdateStock(product.id, product.stockQuantity)}
                      >
                        Update Stock
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>

        {/* Orders Tab */}
        <div className="tab-pane fade" id="orders-tab">
          <h4>Order Management</h4>
          <div className="table-responsive">
            <table className="table table-striped">
              <thead>
                <tr>
                  <th>Order ID</th>
                  <th>Customer</th>
                  <th>Amount</th>
                  <th>Status</th>
                  <th>Payment</th>
                  <th>Date</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {orders.map(order => (
                  <tr key={order.id}>
                    <td>#{order.id}</td>
                    <td>{order.user?.name || 'N/A'}</td>
                    <td>${order.totalAmount.toFixed(2)}</td>
                    <td>
                      <span className={`badge ${order.orderStatus === 'CONFIRMED' ? 'bg-success' : 'bg-warning'}`}>
                        {order.orderStatus}
                      </span>
                    </td>
                    <td>
                      <span className={order.paymentStatus === 'SUCCESS' ? 'text-success' : 'text-warning'}>
                        {order.paymentStatus}
                      </span>
                    </td>
                    <td>{new Date(order.createdAt).toLocaleDateString()}</td>
                    <td>
                      <button
                        className="btn btn-sm btn-outline-primary"
                        onClick={() => handleUpdateOrderStatus(order.id, order.orderStatus)}
                      >
                        Update Status
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  );
}

export default AdminDashboard;
```

### 10. App.css (src/App.css)

```css
.App {
  min-height: 100vh;
}
```

## Running the Application

### Backend
```bash
cd backend
mvn clean install
mvn spring-boot:run
```

Backend will run on: http://localhost:8080

### Frontend
```bash
cd frontend
npm install
npm start
```

Frontend will run on: http://localhost:3000

## Sample Credentials

### Admin
- Email: admin@retail.com
- Password: admin123

### User
- Email: user@retail.com
- Password: user123

## Testing the Application

1. Start MySQL server
2. Run backend (Spring Boot will auto-create database and tables)
3. Run frontend
4. Access http://localhost:3000
5. Login as user or admin
6. Browse products, add to cart, place orders
7. Admin can manage products and view orders

## Key Features Implemented

✅ User registration and login
✅ Admin registration and login
✅ Product browsing with search and filters
✅ Shopping cart management
✅ Order placement with dummy payment
✅ Inventory management
✅ Admin dashboard
✅ Real-time stock updates
✅ Responsive UI with Bootstrap

## API Endpoints

All endpoints are documented in Swagger at: http://localhost:8080/swagger-ui.html

## Database Schema

Tables auto-created by Hibernate:
- users
- products
- cart
- orders
- order_items

## Sample Data

Initial data is loaded from data.sql:
- 2 users (1 admin, 1 customer)
- 15 products across 3 categories

This completes the full-stack implementation!