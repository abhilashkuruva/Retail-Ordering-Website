import React, { useState, useEffect } from 'react';
import { orderAPI } from '../services/api';

function Orders({ user }) {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (user) loadOrders();
  }, [user]);

  const loadOrders = async () => {
    try {
      const response = await orderAPI.getUserOrders(user.id);
      const data = response.data.data?.orders || response.data.data || [];
      setOrders(data);
    } catch (error) {
      console.error('Error loading orders:', error);
      setOrders([]);
    } finally {
      setLoading(false);
    }
  };

  const getStatusBadgeClass = (status) => {
    switch (status) {
      case 'PENDING': return 'bg-warning text-dark';
      case 'CONFIRMED': return 'bg-info';
      case 'PROCESSING': return 'bg-primary';
      case 'DELIVERED': return 'bg-success';
      case 'CANCELLED': return 'bg-danger';
      default: return 'bg-secondary';
    }
  };

  const getPaymentBadgeClass = (status) => {
    switch (status) {
      case 'SUCCESS': return 'bg-success';
      case 'PENDING': return 'bg-warning text-dark';
      case 'FAILED': return 'bg-danger';
      default: return 'bg-secondary';
    }
  };

  if (loading) {
    return <div className="text-center mt-5"><div className="spinner-border text-primary" role="status"><span className="visually-hidden">Loading...</span></div></div>;
  }

  return (
    <div>
      <h2>My Orders</h2>
      {orders.length === 0 ? (
        <div className="text-center mt-5"><h3>No orders yet</h3><p>Start shopping to see your orders here</p></div>
      ) : (
        <div className="row">
          {orders.map(order => (
            <div className="col-md-6 mb-4" key={order.id}>
              <div className="card">
                <div className="card-body">
                  <div className="d-flex justify-content-between mb-3">
                    <h5 className="card-title">Order #{order.id}</h5>
                    <span className={`badge ${getStatusBadgeClass(order.orderStatus)}`}>{order.orderStatus}</span>
                  </div>
                  <div className="mb-2">
                    <span className={`badge ${getPaymentBadgeClass(order.paymentStatus)}`}>{order.paymentStatus}</span>
                  </div>
                  <p className="mb-1"><strong>Date:</strong> {new Date(order.createdAt).toLocaleString()}</p>
                  <p className="mb-2"><strong>Total:</strong> <span className="text-primary">${order.totalAmount.toFixed(2)}</span></p>
                  {order.orderItems && order.orderItems.length > 0 && (
                    <div className="mt-3">
                      <hr />
                      <h6>Items:</h6>
                      <ul className="list-unstyled">
                        {order.orderItems.map((item, idx) => (
                          <li key={item.id || idx} className="d-flex justify-content-between">
                            <span>{item.product?.productName || 'Product'} x {item.quantity}</span>
                            <span>${((item.price || 0) * (item.quantity || 1)).toFixed(2)}</span>
                          </li>
                        ))}
                      </ul>
                    </div>
                  )}
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