import React, { useContext } from 'react';
import { Link } from 'react-router-dom';
import { FavoriteContext } from '../context/FavoriteContext';
import './CardProductos.css';

const CardProductos = ({ product }) => {
  const { favoriteItems, addToFavorite } = useContext(FavoriteContext)
  const isFavorite = favoriteItems.some((p) => p.id === product.id)

  return (
    <div className="card-producto">
      <div className="producto-imagen-container">
        {product.imagen ? (
          <img 
            src={product.imagen} 
            alt={product.nombre} 
            className="producto-imagen"
          />
        ) : (
          <div style={{ width: '100%', height: '200px', backgroundColor: '#e0e0e0', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
            Sin imagen
          </div>
        )}
        {product.categoria && (
          <span className="producto-categoria">{product.categoria}</span>
        )}
      </div>
      
      <div className="producto-info">
        <h3 className="producto-nombre">
          <Link to={`/products/${product.id}`} style={{ color: 'inherit', textDecoration: 'none' }}>
            {product.nombre}
          </Link>
        </h3>
        {product.descripcion && (
          <p className="producto-descripcion">{product.descripcion}</p>
        )}
        
        {product.rating && (
          <div className="producto-rating">
            <span className="stars">⭐ {product.rating}</span>
          </div>
        )}
        
        {product.stock !== undefined && (
          <div className="producto-stock">
            <span className={product.stock > 0 ? 'en-stock' : 'sin-stock'}>
              {product.stock > 0 ? `Stock: ${product.stock}` : 'Agotado'}
            </span>
          </div>
        )}
        
        <div className="producto-footer">
          <span className="producto-precio">${product.precio}</span>
          <div style={{ display: 'flex', gap: '8px' }}>
            <button
              onClick={() => addToFavorite(product)}
              style={{ background: 'none', border: 'none', fontSize: '20px', cursor: 'pointer' }}
              title={isFavorite ? 'Quitar de favoritos' : 'Agregar a favoritos'}
            >
              {isFavorite ? '❤️' : '🤍'}
            </button>
            <button className="btn-agregar">Agregar</button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default CardProductos;
