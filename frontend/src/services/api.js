import axios from 'axios';

// Base URL for the backend API
const API_BASE_URL = 'http://localhost:8080/api';

// Create axios instance with default config
const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Auth APIs
export const authAPI = {
  // User registration
  register: (name, email, password) => 
    api.post('/auth/register', { name, email, password }),
  
  // User login
  login: (email, password) => 
    api.post('/auth/login', { email, password }),
  
  // Admin registration
  registerAdmin: (name, email, password) => 
    api.post('/auth/register-admin', { name, email, password }),
  
  // Admin login
  loginAdmin: (email, password) => 
    api.post('/auth/login-admin', { email, password }),
  
  // Verify user
  verifyUser: (userId) => 
    api.get(`/auth/verify/${userId}`),
};

// Product APIs
export const productAPI = {
  // Get all products
  getAllProducts: () => 
    api.get('/products'),
  
  // Get product by ID
  getProductById: (id) => 
    api.get(`/products/${id}`),
  
  // Search products
  searchProducts: (keyword) => 
    api.get('/products/search', { params: { keyword } }),
  
  // Get products by category
  getProductsByCategory: (category) => 
    api.get(`/products/category/${category}`),
  
  // Add new product (Admin)
  addProduct: (productData) => 
    api.post('/products', productData),
  
  // Update product stock (Admin)
  updateStock: (id, stockQuantity) => 
    api.put(`/products/${id}/stock`, { stockQuantity }),
  
  // Update product (Admin)
  updateProduct: (id, productData) => 
    api.put(`/products/${id}`, productData),
  
  // Delete product (Admin)
  deleteProduct: (id) => 
    api.delete(`/products/${id}`),
};

// Cart APIs
export const cartAPI = {
  // Get user cart
  getCart: (userId) => 
    api.get('/cart', { params: { userId } }),
  
  // Add item to cart
  addToCart: (userId, productId, quantity) => 
    api.post('/cart/add', { userId, productId, quantity }),
  
  // Update cart item quantity
  updateCartQuantity: (userId, productId, quantity) => 
    api.put('/cart/update', { userId, productId, quantity }),
  
  // Remove item from cart
  removeFromCart: (userId, productId) => 
    api.delete(`/cart/${productId}`, { params: { userId } }),
  
  // Get cart item count
  getCartItemCount: (userId) => 
    api.get('/cart/count', { params: { userId } }),
};

// Order APIs
export const orderAPI = {
  // Place order
  placeOrder: (userId) => 
    api.post('/orders/place', null, { params: { userId } }),
  
  // Confirm payment
  confirmPayment: (orderId) => 
    api.post(`/orders/${orderId}/payment`),
  
  // Get order by ID
  getOrderById: (orderId) => 
    api.get(`/orders/${orderId}`),
  
  // Get user orders
  getUserOrders: (userId) => 
    api.get('/orders/user', { params: { userId } }),
  
  // Get all orders (Admin)
  getAllOrders: () => 
    api.get('/orders'),
  
  // Update order status (Admin)
  updateOrderStatus: (orderId, status) => 
    api.put(`/orders/${orderId}/status`, null, { params: { status } }),
};

export default api;