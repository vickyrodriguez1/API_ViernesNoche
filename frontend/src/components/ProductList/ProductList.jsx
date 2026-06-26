import React, { useEffect, useState } from 'react';
import CardProductos from '../CardProductos';
import styles from './ProductList.module.css';

// Componente reutilizable del listado de productos.
// Recibe por props la categoria seleccionada en la Home (prop "categoria").
// El backend ya devuelve los productos ordenados alfabeticamente.
const ProductList = ({ categoria = '' }) => {
  // Patron tipico de "traer datos del backend": useState + useEffect trabajan juntos.
  // - 3 estados: los datos (products), la carga (loading) y el error.
  // - el useEffect de abajo dispara el fetch y va completando esos estados.
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [search, setSearch] = useState(''); // texto del buscador (filtrado por nombre)

  // useEffect con [] -> se ejecuta UNA sola vez, al montar el componente.
  // El fetch es una llamada asincronica (async/await): el backend devuelve una
  // promesa, la esperamos, y guardamos el resultado en el estado con setProducts.
  useEffect(() => {
    const fetchProducts = async () => {
      try {
        const response = await fetch('http://localhost:8080/api/productos');
        if (!response.ok) {
          throw new Error('Error al cargar los productos');
        }
        const data = await response.json();
        setProducts(data); // al setear el estado, React re-renderiza con los productos
      } catch (err) {
        setError('No pudimos conectar con el servidor. Por favor, intenta más tarde.');
      } finally {
        setLoading(false); // pase lo que pase, dejamos de mostrar "cargando"
      }
    };

    fetchProducts();
  }, []);

  // Filtrado del lado del cliente: ya tenemos todos los productos en memoria, asi
  // que filtramos por texto (nombre) y por la categoria que llega por props, sin
  // volver a llamar al backend en cada tecla.
  const filteredProducts = products.filter((product) => {
    const coincideNombre = product.nombre
      .toLowerCase()
      .includes(search.toLowerCase());
    const coincideCategoria =
      categoria === '' ||
      (product.categorias && product.categorias.includes(categoria));
    return coincideNombre && coincideCategoria;
  });

  // Renderizado condicional: mientras carga mostramos skeletons; si hubo error,
  // el mensaje de error; y si todo OK, la grilla de productos (mas abajo).
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
        {/* Componente controlado: el input esta "atado" al estado `search`.
            value lo muestra y onChange lo actualiza en cada tecla -> filtra en vivo. */}
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
          {/* .map transforma cada producto (dato) en una tarjeta (componente).
              La `key` unica ayuda a React a redibujar solo lo que cambia. */}
          {filteredProducts.map((product) => (
            <CardProductos key={product.id} product={product} />
          ))}
        </div>
      )}
    </div>
  );
};

export default ProductList;
