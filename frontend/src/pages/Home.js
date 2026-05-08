import React, { useState, useEffect } from 'react';
import { productAPI, cartAPI } from '../services/api';

function Home({ user, isAdmin }) {
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

    if (isAdmin) {
      setMessage('Admin accounts cannot add items to cart');
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
            <button className="btn btn-primary" onClick={handleSearch}>Search</button>
          </div>
        </div>
        <div className="col-md-6">
          <div className="btn-group" role="group">
            <button
              className={`btn ${selectedCategory === 'all' ? 'btn-primary' : 'btn-outline-primary'}`}
              onClick={() => handleCategoryFilter('all')}
            >All</button>
            <button
              className={`btn ${selectedCategory === 'cooldrinks' ? 'btn-info' : 'btn-outline-info'}`}
              onClick={() => handleCategoryFilter('cooldrinks')}
            >Cold Drinks</button>
            <button
              className={`btn ${selectedCategory === 'veg' ? 'btn-success' : 'btn-outline-success'}`}
              onClick={() => handleCategoryFilter('veg')}
            >Veg</button>
            <button
              className={`btn ${selectedCategory === 'non-veg' ? 'btn-danger' : 'btn-outline-danger'}`}
              onClick={() => handleCategoryFilter('non-veg')}
            >Non-Veg</button>
          </div>
        </div>
      </div>

      <div className="row">
        {filteredProducts.map(product => (
          <div className="col-md-3 mb-4" key={product.id}>
            <div className="card product-card h-100">
              {product.stockQuantity === 0 && (
                <span className="out-of-stock-badge">OUT OF STOCK</span>
              )}
              <img
                src={product.imageUrl && product.imageUrl.trim() !== '' ? product.imageUrl : `https://via.placeholder.com/200x200/808080/FFFFFF?text=${encodeURIComponent(product.productName.substring(0, 15))}`}
                className="card-img-top product-image"
                alt={product.productName}
                onError={(e) => { e.target.src = 'https://via.placeholder.com/200x200/808080/FFFFFF?text=No+Image'; }}
              />
              <div className="card-body">
                <span className={`badge ${getCategoryBadgeClass(product.category)} mb-2`}>
                  {product.category}
                </span>
                <h5 className="card-title">{product.productName}</h5>
                <p className="card-text"><strong>${product.price.toFixed(2)}</strong></p>
                <p className="text-muted small">Stock: {product.stockQuantity}</p>
              </div>
              <div className="card-footer">
                {(!isAdmin && user) ? (
                  <button
                    className="btn btn-primary w-100"
                    onClick={() => handleAddToCart(product.id)}
                    disabled={product.stockQuantity === 0}
                  >
                    {product.stockQuantity === 0 ? 'Out of Stock' : 'Add to Cart'}
                  </button>
                ) : isAdmin ? (
                  <span className="text-muted small">Admin view - Go to Dashboard to manage</span>
                ) : (
                  <button
                    className="btn btn-primary w-100"
                    onClick={() => handleAddToCart(product.id)}
                    disabled={product.stockQuantity === 0}
                  >
                    {product.stockQuantity === 0 ? 'Out of Stock' : 'Add to Cart'}
                  </button>
                )}
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