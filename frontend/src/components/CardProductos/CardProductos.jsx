import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import { toggleFavorite } from '../../store/slices/favoritesSlice';
import { addProductToCart } from '../../store/slices/cartSlice';
import styles from './CardProductos.module.css';

// Componente reutilizable: pinta UNA tarjeta de producto. Recibe el producto por
// props y se usa tanto en el catalogo (ProductList) como en la pagina de Favoritos.
const CardProductos = ({ product }) => {
  const dispatch = useDispatch(); // para disparar acciones/thunks al store de Redux
  const navigate = useNavigate();
  const favorites = useSelector((state) => state.favorites.items); // leemos del store global
  // .some() devuelve true si ALGUN favorito tiene el id de este producto (ya es favorito)
  const isFavorite = favorites.some((item) => item?.id === product?.id);

  // El producto esta agotado cuando el backend nos manda stock 0.
  const sinStock = product.stock !== undefined && product.stock <= 0;

  const handleToggleFavorite = () => {
    dispatch(toggleFavorite(product));
  }

  // Dispara el thunk que hace POST a la API para agregar este producto al carrito.
  // Si no hay sesion iniciada, mandamos al login (no se puede comprar sin loguearse).
  const handleAddToCart = () => {
    const token = localStorage.getItem('token');
    if (!token) {
      navigate('/login');
      return;
    }
    dispatch(addProductToCart(product.id));
  }

  // Creamos la variable de forma segura leyendo el array que manda el Backend
  const categoriaDestacada = product.categorias && product.categorias.length > 0 
    ? product.categorias[0] 
    : null;

    const fotoProducto = product.imagen_base64 || product.imagenBase64;

  return (
        <div className={styles.card}>
      <div className={styles.imageContainer}>
        {/* 2. Evaluamos y renderizamos usando la variable inteligente */}
        {fotoProducto ? (
          <img 
            src={fotoProducto} 
            alt={product.nombre} 
            className={styles.image}
          />
        ) : (
          <div className={styles.imagePlaceholder}>
            Sin imagen
          </div>
        )}
        {categoriaDestacada && (
          <span className={styles.badge}>{categoriaDestacada}</span>
        )}
      </div>
      
      <div className={styles.content}>
        <h3 className={styles.name}>
          <Link to={`/products/${product.id}`} style={{ color: 'inherit', textDecoration: 'none' }}>
            {product.nombre}
          </Link>
        </h3>
        {product.descripcion && (
          <p className={styles.description}>{product.descripcion}</p>
        )}
        
        {product.rating && (
          <div className={styles.rating}>
            <span className={styles.stars}>⭐ {product.rating}</span>
          </div>
        )}
        
        {product.stock !== undefined && (
          <div className={styles.stock}>
            <span className={product.stock > 0 ? styles.inStock : styles.outOfStock}>
              {product.stock > 0 ? `Stock: ${product.stock}` : 'Agotado'}
            </span>
          </div>
        )}
        
        <div className={styles.footer}>
          <span className={styles.price}>${product.precio}</span>
          <div className={styles.footerActions}>
            <button
              className={`${styles.favoriteButton} ${isFavorite ? styles.favorited : ''}`}
              type="button"
              onClick={handleToggleFavorite}
              title={isFavorite ? 'Quitar de favoritos' : 'Agregar a favoritos'}
              aria-label={isFavorite ? 'Quitar de favoritos' : 'Agregar a favoritos'}
              aria-pressed={isFavorite}
            >
              {isFavorite ? '♥' : '♡'}
            </button>
            <button
              className={styles.addButton}
              type="button"
              onClick={handleAddToCart}
              disabled={sinStock}
            >
              {sinStock ? 'Agotado' : 'Agregar'}
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default CardProductos;