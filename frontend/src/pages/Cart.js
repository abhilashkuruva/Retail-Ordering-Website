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
      console.log('Loading cart for user:', user.id);
      const response = await cartAPI.getCart(user.id);
      console.log('Cart API response:', response);
      console.log('Response data:', response.data);

      // Handle different response structures
      let data = [];
      if (response.data && response.data.data) {
        if (Array.isArray(response.data.data)) {
          data = response.data.data;
        } else if (Array.isArray(response.data.data.cartItems)) {
          data = response.data.data.cartItems;
        } else if (response.data.data.cartItems) {
          data = Array.isArray(response.data.data.cartItems) ? response.data.data.cartItems : [];
        }
      } else if (Array.isArray(response.data)) {
        data = response.data;
      }

      console.log('Cart items:', data);
      setCartItems(data);

      // Calculate total - DTO has price directly, not nested in product
      const total = data.reduce((sum, item) => {
        if (!item) return sum;
        // DTO structure: item.price, item.quantity
        // Entity structure: item.product.price, item.quantity
        const price = item.price || (item.product && item.product.price) || 0;
        const qty = item.quantity || 1;
        return sum + (price * qty);
      }, 0);

      setTotalAmount(total);

    } catch (error) {

      console.error('Error loading cart:', error);
      setCartItems([]);

    } finally {

      setLoading(false);

    }
  };

  const updateQuantity = async (productId, newQuantity) => {

    if (newQuantity < 1) {
      removeFromCart(productId);
      return;
    }

    try {

      await cartAPI.updateCartQuantity(
        user.id,
        productId,
        newQuantity
      );

      loadCart();

    } catch (error) {

      setMessage(
        error.response?.data?.message ||
        'Failed to update quantity'
      );

      setTimeout(() => setMessage(''), 3000);
    }
  };

  const removeFromCart = async (productId) => {

    try {

      await cartAPI.removeFromCart(user.id, productId);

      loadCart();

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

      const orderResponse = await orderAPI.placeOrder(user.id);

      const order =
        orderResponse.data.data?.order ||
        orderResponse.data.data;

      if (order && order.id) {

        await orderAPI.confirmPayment(order.id);

        setMessage('Order placed successfully!');

        setCartItems([]);

        setTimeout(() => {
          navigate('/orders');
        }, 2000);

      } else {

        setMessage('Failed to create order');

      }

    } catch (error) {

      console.error('Order error:', error);

      setMessage(
        error.response?.data?.message ||
        'Failed to place order'
      );

    }

    setTimeout(() => setMessage(''), 3000);
  };

  if (loading) {
    return (
      <div className="text-center mt-5">
        <div className="spinner-border text-primary" role="status">
          <span className="visually-hidden">
            Loading...
          </span>
        </div>
      </div>
    );
  }

  return (
    <div>

      <h2>Shopping Cart</h2>

      {message && (
        <div className="alert alert-info alert-dismissible fade show">

          {message}

          <button
            type="button"
            className="btn-close"
            onClick={() => setMessage('')}
          ></button>

        </div>
      )}

      {cartItems.length === 0 ? (

        <div className="text-center mt-5">

          <h3>Your cart is empty</h3>

          <p>
            Start shopping to add items to your cart
          </p>

        </div>

      ) : (

        <div className="row">

          <div className="col-md-8">

            <div className="card">

              <div className="card-body">

                {cartItems.map((item, index) => {
                  // Support both DTO (flat) and Entity (nested) structures
                  const productId = item.productId || (item.product && item.product.id);
                  const productName = item.productName || (item.product && item.product.productName) || 'Product';
                  const imageUrl = item.imageUrl || (item.product && item.product.imageUrl) || '';
                  const price = item.price || (item.product && item.product.price) || 0;
                  const quantity = item.quantity || 1;

                  if (!productId) {
                    return null;
                  }

                  return (
                    <div
                      key={productId || index}
                      className="row mb-3 align-items-center"
                    >

                      <div className="col-md-4">

                        <img
                          src={
                            imageUrl && imageUrl !== ''
                              ? imageUrl
                              : 'https://via.placeholder.com/100x100'
                          }
                          alt={productName}
                          style={{
                            width: '80px',
                            height: '80px',
                            objectFit: 'cover'
                          }}
                          className="rounded"
                        />

                        <span className="ms-2">
                          {productName}
                        </span>

                      </div>

                      <div className="col-md-2">

                        <strong>
                          ${parseFloat(price).toFixed(2)}
                        </strong>

                      </div>

                      <div className="col-md-3">

                        <div
                          className="input-group"
                          style={{ width: '120px' }}
                        >

                          <button
                            className="btn btn-outline-secondary"
                            onClick={() =>
                              updateQuantity(
                                productId,
                                quantity - 1
                              )
                            }
                          >
                            -
                          </button>

                          <input
                            type="text"
                            className="form-control text-center"
                            value={quantity}
                            readOnly
                          />

                          <button
                            className="btn btn-outline-secondary"
                            onClick={() =>
                              updateQuantity(
                                productId,
                                quantity + 1
                              )
                            }
                          >
                            +
                          </button>

                        </div>

                      </div>

                      <div className="col-md-2 text-end">

                        <button
                          className="btn btn-danger btn-sm"
                          onClick={() =>
                            removeFromCart(productId)
                          }
                        >
                          Remove
                        </button>

                      </div>

                    </div>
                  );
                })}

              </div>

            </div>

          </div>

          <div className="col-md-4">

            <div className="card">

              <div className="card-body">

                <h5 className="card-title">
                  Order Summary
                </h5>

                <hr />

                <div className="d-flex justify-content-between mb-2">

                  <span>
                    Subtotal ({cartItems.length} items):
                  </span>

                  <strong>
                    ${totalAmount.toFixed(2)}
                  </strong>

                </div>

                <div className="d-flex justify-content-between mb-2">

                  <span>Delivery:</span>

                  <strong>FREE</strong>

                </div>

                <hr />

                <div className="d-flex justify-content-between mb-3">

                  <h5>Total:</h5>

                  <h4 className="text-primary">
                    ${totalAmount.toFixed(2)}
                  </h4>

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