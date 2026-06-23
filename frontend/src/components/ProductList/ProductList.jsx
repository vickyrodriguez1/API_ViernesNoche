import React, { useEffect, useState } from 'react';
import CardProductos from '../CardProductos';
import styles from './ProductList.module.css';

// Componente reutilizable del listado de productos.
// Recibe por props la categoria seleccionada en la Home (prop "categoria").
// El backend ya devuelve los productos ordenados alfabeticamente.
const ProductList = ({ categoria = '' }) => {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [search, setSearch] = useState(''); // texto del buscador (filtrado por nombre)

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

  // Filtramos en el cliente por texto (nombre) y por la categoria que llega por props.
  const filteredProducts = products.filter((product) => {
    const coincideNombre = product.nombre
      .toLowerCase()
      .includes(search.toLowerCase());
    const coincideCategoria =
      categoria === '' ||
      (product.categorias && product.categorias.includes(categoria));
    return coincideNombre && coincideCategoria;
  });

  // Estado de carga: skeletons animados
  if (loading) {
    return (
      <div className={styles.container}>
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
      <div className={styles.toolbar}>
        <h2 className={styles.title}>Productos</h2>
        <input
          type="text"
          className={styles.search}
          value={search}
          onChange={(e) => setSearch(e.target.value)}
          placeholder="Buscar producto por nombre..."
        />
      </div>

      {filteredProducts.length === 0 ? (
        <p className={styles.empty}>
          No hay productos que coincidan con tu búsqueda.
        </p>
      ) : (
        <div className={styles.grid}>
          {filteredProducts.map((product) => (
            <CardProductos key={product.id} product={product} />
          ))}
        </div>
      )}
    </div>
  );
};

export default ProductList;
