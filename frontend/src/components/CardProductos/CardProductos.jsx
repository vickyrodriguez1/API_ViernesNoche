import React from 'react';
import { Link } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import { toggleFavorite } from '../../store/slices/favoritesSlice';
import { addProductToCart } from '../../store/slices/cartSlice';
import styles from './CardProductos.module.css';

const CardProductos = ({ product }) => {
  const dispatch = useDispatch();
  const favorites = useSelector((state) => state.favorites.items);
  const isFavorite = favorites.some((item) => item?.id === product?.id);

  const handleToggleFavorite = () => {
    dispatch(toggleFavorite(product));
  }

  // Dispara el thunk que hace POST a la API para agregar este producto al carrito.
  const handleAddToCart = () => {
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
        {/* CORREGIDO: Cambiado a plural para que detecte el array del backend */}
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
            <button className={styles.addButton} type="button" onClick={handleAddToCart}>Agregar</button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default CardProductos;