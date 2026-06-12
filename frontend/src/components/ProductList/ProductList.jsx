import React, { useEffect, useState } from 'react';
import CardProductos from '../CardProductos';
import styles from './ProductList.module.css';

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

  // Estado de carga: skeletons animados
  if (loading) {
    return (
      <div className={styles.container}>
        <div className={styles.header}>
          <span className={styles.eyebrow}>Catálogo</span>
          <h2 className={styles.title}>Nuestros Productos</h2>
        </div>
        <div className={styles.grid}>
          {Array.from({ length: 8 }).map((_, i) => (
            <div key={i} className={styles.skeletonCard}>
              <div className={`${styles.skeletonImage} ${styles.shimmer}`} />
              <div className={`${styles.skeletonLine} ${styles.shimmer}`} />
              <div className={`${styles.skeletonLine} ${styles.short} ${styles.shimmer}`} />
            </div>
          ))}
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className={styles.container}>
        <div className={styles.stateMessage}>{error}</div>
      </div>
    );
  }

  return (
    <div className={styles.container}>
      <div className={styles.header}>
        <span className={styles.eyebrow}>Catálogo</span>
        <h2 className={styles.title}>Nuestros Productos</h2>
        <p className={styles.subtitle}>
          Descubrí nuestra selección de productos seleccionados para vos.
        </p>
      </div>
      {products.length === 0 ? (
        <p className={styles.empty}>
          No hay productos disponibles en este momento.
        </p>
      ) : (
        <div className={styles.grid}>
          {products.map((product) => (
            <CardProductos key={product.id} product={product} />
          ))}
        </div>
      )}
    </div>
  );
};

export default ProductList;
