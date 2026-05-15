import React from 'react';
import './CardProductos.css';

const CardProductos = ({ product }) => {
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
        <h3 className="producto-nombre">{product.nombre}</h3>
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
          <button className="btn-agregar">Agregar</button>
        </div>
      </div>
    </div>
  );
};

export default CardProductos;
