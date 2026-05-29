import React, { useEffect, useState } from 'react';
import CardProductos from './CardProductos';
import './ProductList.css';

const ProductList = () => {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchProducts = async () => {
      try {
        const response = await fetch('http://localhost:8080/api/productos');
        if (!response.ok) {
          throw new Error('Error al cargar los productos');
        }
        const data = await response.json();
        setProducts(data);
      } catch (err) {        
        setError('No pudimos conectar con el servidor. Por favor, intenta más tarde.');
      } finally {
        setLoading(false);
      }
    };

    fetchProducts();
  }, []);

  // Estados de carga y error
  if (loading) return <div className="product-list-container"><p>Cargando productos...</p></div>;
  if (error) return <div className="product-list-container"><p>Error: {error}</p></div>;

  return (
    <div className="product-list-container">
      <h2 className="product-list-title">Nuestros Productos</h2>
      {products.length === 0 ? (
        <p style={{ textAlign: 'center', fontSize: '18px', color: '#666' }}>
          No hay productos disponibles en este momento.
        </p>
      ) : (
        <div className="products-grid">
          {products.map((product) => (
            <CardProductos key={product.id} product={product} />
          ))}
        </div>
      )}
    </div>
  );
};

export default ProductList;
