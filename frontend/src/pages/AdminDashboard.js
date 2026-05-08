import React, { useState, useEffect } from 'react';
import { productAPI, orderAPI } from '../services/api';

function AdminDashboard() {
  const [products, setProducts] = useState([]);
  const [orders, setOrders] = useState([]);
  const [activeTab, setActiveTab] = useState('products');
  const [loading, setLoading] = useState(true);
  const [message, setMessage] = useState('');
  const [showAddProduct, setShowAddProduct] = useState(false);
  const [newProduct, setNewProduct] = useState({
    productId: '', productName: '', category: 'veg', price: '', imageUrl: '', stockQuantity: ''
  });
  const [editingProduct, setEditingProduct] = useState(null);
  const [stockUpdate, setStockUpdate] = useState({});

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      const [productsRes, ordersRes] = await Promise.all([productAPI.getAllProducts(), orderAPI.getAllOrders()]);
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
      await productAPI.addProduct(newProduct);
      setMessage('Product added successfully!');
      setShowAddProduct(false);
      setNewProduct({ productId: '', productName: '', category: 'veg', price: '', imageUrl: '', stockQuantity: '' });
      loadData();
      setTimeout(() => setMessage(''), 3000);
    } catch (error) {
      setMessage(error.response?.data?.message || 'Failed to add product');
      setTimeout(() => setMessage(''), 3000);
    }
  };

  const handleUpdateStock = async (productId) => {
    try {
      await productAPI.updateStock(productId, { stockQuantity: parseInt(stockUpdate[productId]) });
      setMessage('Stock updated successfully!');
      setStockUpdate({});
      loadData();
      setTimeout(() => setMessage(''), 3000);
    } catch (error) {
      setMessage(error.response?.data?.message || 'Failed to update stock');
      setTimeout(() => setMessage(''), 3000);
    }
  };

  const handleUpdateOrderStatus = async (orderId, status) => {
    try {
      await orderAPI.updateOrderStatus(orderId, status);
      setMessage('Order status updated!');
      loadData();
      setTimeout(() => setMessage(''), 3000);
    } catch (error) {
      setMessage(error.response?.data?.message || 'Failed to update order');
      setTimeout(() => setMessage(''), 3000);
    }
  };

  const handleDeleteProduct = async (productId) => {
    if (window.confirm('Are you sure you want to delete this product?')) {
      try {
        await productAPI.deleteProduct(productId);
        setMessage('Product deleted successfully!');
        loadData();
        setTimeout(() => setMessage(''), 3000);
      } catch (error) {
        setMessage(error.response?.data?.message || 'Failed to delete product');
        setTimeout(() => setMessage(''), 3000);
      }
    }
  };

  if (loading) {
    return <div className="text-center mt-5"><div className="spinner-border text-primary" role="status"><span className="visually-hidden">Loading...</span></div></div>;
  }

  return (
    <div>
      <h2>Admin Dashboard</h2>
      {message && <div className="alert alert-info alert-dismissible fade show" role="alert">{message}<button type="button" className="btn-close" onClick={() => setMessage('')}></button></div>}

      <div className="row mb-4">
        <div className="col-md-4">
          <div className="card bg-primary text-white">
            <div className="card-body">
              <h5>Total Products</h5>
              <h2>{products.length}</h2>
            </div>
          </div>
        </div>
        <div className="col-md-4">
          <div className="card bg-success text-white">
            <div className="card-body">
              <h5>Total Orders</h5>
              <h2>{orders.length}</h2>
            </div>
          </div>
        </div>
        <div className="col-md-4">
          <div className="card bg-info text-white">
            <div className="card-body">
              <h5>Total Revenue</h5>
              <h2>${orders.reduce((sum, o) => sum + (o.paymentStatus === 'SUCCESS' ? o.totalAmount : 0), 0).toFixed(2)}</h2>
            </div>
          </div>
        </div>
      </div>

      <ul className="nav nav-tabs mb-4">
        <li className="nav-item"><button className={`nav-link ${activeTab === 'products' ? 'active' : ''}`} onClick={() => setActiveTab('products')}>Products</button></li>
        <li className="nav-item"><button className={`nav-link ${activeTab === 'orders' ? 'active' : ''}`} onClick={() => setActiveTab('orders')}>Orders</button></li>
      </ul>

      {activeTab === 'products' && (
        <div>
          <div className="d-flex justify-content-between mb-3">
            <h4>Product Inventory</h4>
            <button className="btn btn-primary" onClick={() => setShowAddProduct(!showAddProduct)}>Add New Product</button>
          </div>

          {showAddProduct && (
            <div className="card mb-4">
              <div className="card-body">
                <h5>Add New Product</h5>
                <form onSubmit={handleAddProduct}>
                  <div className="row">
                    <div className="col-md-3 mb-2"><input className="form-control" placeholder="Product ID" value={newProduct.productId} onChange={(e) => setNewProduct({...newProduct, productId: e.target.value})} required /></div>
                    <div className="col-md-3 mb-2"><input className="form-control" placeholder="Product Name" value={newProduct.productName} onChange={(e) => setNewProduct({...newProduct, productName: e.target.value})} required /></div>
                    <div className="col-md-2 mb-2">
                      <select className="form-control" value={newProduct.category} onChange={(e) => setNewProduct({...newProduct, category: e.target.value})}>
                        <option value="veg">Veg</option>
                        <option value="non-veg">Non-Veg</option>
                        <option value="cooldrinks">Cold Drinks</option>
                      </select>
                    </div>
                    <div className="col-md-2 mb-2"><input type="number" step="0.01" className="form-control" placeholder="Price" value={newProduct.price} onChange={(e) => setNewProduct({...newProduct, price: e.target.value})} required /></div>
                    <div className="col-md-2 mb-2"><input type="number" className="form-control" placeholder="Stock" value={newProduct.stockQuantity} onChange={(e) => setNewProduct({...newProduct, stockQuantity: e.target.value})} required /></div>
                  </div>
                  <div className="mb-2"><input className="form-control" placeholder="Image URL" value={newProduct.imageUrl} onChange={(e) => setNewProduct({...newProduct, imageUrl: e.target.value})} /></div>
                  <button type="submit" className="btn btn-success">Add Product</button>
                  <button type="button" className="btn btn-secondary ms-2" onClick={() => setShowAddProduct(false)}>Cancel</button>
                </form>
              </div>
            </div>
          )}

          <div className="table-responsive">
            <table className="table table-striped">
              <thead><tr><th>ID</th><th>Name</th><th>Category</th><th>Price</th><th>Stock</th><th>Actions</th></tr></thead>
              <tbody>
                {products.map(product => (
                  <tr key={product.id}>
                    <td>{product.productId}</td>
                    <td>{product.productName}</td>
                    <td><span className="badge bg-info">{product.category}</span></td>
                    <td>${product.price.toFixed(2)}</td>
                    <td>
                      <div className="input-group" style={{width: '150px'}}>
                        <input type="number" className="form-control" placeholder="New Stock" value={stockUpdate[product.id] || ''} onChange={(e) => setStockUpdate({...stockUpdate, [product.id]: e.target.value})} />
                        <button className="btn btn-outline-primary" onClick={() => handleUpdateStock(product.id)}>Update</button>
                      </div>
                    </td>
                    <td>
                      <button className="btn btn-danger btn-sm me-1" onClick={() => handleDeleteProduct(product.id)}>Delete</button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      )}

      {activeTab === 'orders' && (
        <div>
          <h4>All Orders</h4>
          <div className="table-responsive">
            <table className="table table-striped">
              <thead><tr><th>Order ID</th><th>Customer Name</th><th>Total</th><th>Payment</th><th>Status</th><th>Date</th></tr></thead>
              <tbody>
                {orders.map(order => (
                  <tr key={order.id}>
                    <td>#{order.id}</td>
                    <td>{order.user?.name || 'N/A'}</td>
                    <td>${order.totalAmount.toFixed(2)}</td>
                    <td><span className={`badge ${order.paymentStatus === 'SUCCESS' ? 'bg-success' : 'bg-warning'}`}>{order.paymentStatus}</span></td>
                    <td><span className={`badge ${order.orderStatus === 'DELIVERED' ? 'bg-success' : order.orderStatus === 'CANCELLED' ? 'bg-danger' : 'bg-primary'}`}>{order.orderStatus}</span></td>
                    <td>{new Date(order.createdAt).toLocaleDateString()}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      )}
    </div>
  );
}

export default AdminDashboard;