import React, { useState, useEffect, useCallback } from 'react';
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
  const [isLoaded, setIsLoaded] = useState(false);

  // Load user from localStorage on mount (only once)
  useEffect(() => {
    try {
      const savedUser = localStorage.getItem('user');
      const savedAdmin = localStorage.getItem('isAdmin');
      if (savedUser) {
        setUser(JSON.parse(savedUser));
      }
      if (savedAdmin) {
        setIsAdmin(JSON.parse(savedAdmin));
      }
    } catch (e) {
      console.error('Error loading user from localStorage:', e);
      localStorage.removeItem('user');
      localStorage.removeItem('isAdmin');
    }
    setIsLoaded(true);
  }, []);

  // Use useCallback to prevent re-creation on each render
  const handleLogin = useCallback((userData, admin = false) => {
    try {
      setUser(userData);
      setIsAdmin(admin);
      localStorage.setItem('user', JSON.stringify(userData));
      localStorage.setItem('isAdmin', JSON.stringify(admin));
      console.log('Login successful, user saved:', userData);
    } catch (e) {
      console.error('Error saving user to localStorage:', e);
    }
  }, []);

  const handleLogout = useCallback(() => {
    try {
      setUser(null);
      setIsAdmin(false);
      localStorage.removeItem('user');
      localStorage.removeItem('isAdmin');
      console.log('User logged out');
    } catch (e) {
      console.error('Error during logout:', e);
    }
  }, []);

  // Don't render until we've checked localStorage
  if (!isLoaded) {
    return (
      <div className="text-center mt-5">
        <div className="spinner-border text-primary" role="status">
          <span className="visually-hidden">Loading...</span>
        </div>
      </div>
    );
  }

  return (
    <Router>
      <div className="App">
        <Navbar user={user} isAdmin={isAdmin} onLogout={handleLogout} />
        <div className="container mt-4">
          <Routes>
            <Route path="/" element={<Home user={user} isAdmin={isAdmin} />} />
            <Route path="/login" element={<Login onLogin={handleLogin} />} />
            <Route path="/register" element={<Register />} />
            <Route path="/cart" element={user ? <Cart user={user} isAdmin={isAdmin} /> : <Navigate to="/login" />} />
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