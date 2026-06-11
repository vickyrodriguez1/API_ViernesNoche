import React from 'react';
import { Link } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import { toggleFavorite } from '../../store/slices/favoritesSlice';
import { addToCart } from '../../store/slices/cartSlice'
import styles from './CardProductos.module.css'

const CardProductos = ({ product }) => {
  const dispatch = useDispatch();
  const favorites = useSelector((state) => state.favorites.items);
  const cartItems = useSelector((state) => state.cart.items);
  const isFavorite = favorites.some((item) => item?.id === product?.id);
  const inCart = cartItems.some((item) => item?.id === product?.id);

  const handleToggleFavorite = () => {
    dispatch(toggleFavorite(product));
  }

  return (
    <div className={styles.card}>
      <div className={styles.imageContainer}>
        {product.imagen ? (
          <img 
            src={product.imagen} 
            alt={product.nombre} 
            className={styles.image}
          />
        ) : (
          <div className={styles.imagePlaceholder}>
            Sin imagen
          </div>
        )}
        {product.categoria && (
          <span className={styles.badge}>{product.categoria}</span>
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
            >
              {isFavorite ? '♥ Favorito' : '♡ Favorito'}
            </button>
            <button
              className={styles.addButton}
              type="button"
              onClick={() => dispatch(addToCart(product))}
              disabled={product.stock === 0}
            >
              {product.stock === 0 ? 'Agotado' : inCart ? 'Añadir otra' : 'Agregar'}
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default CardProductos;
