import React, { useContext } from 'react'
import { useNavigate } from 'react-router-dom'
import { FavoriteContext } from '../context/FavoriteContext'

export default function Favorite() {
  const { favoriteItems } = useContext(FavoriteContext)
  const navigate = useNavigate()

  return (
    <div style={{ maxWidth: '800px', margin: '30px auto', padding: '20px' }}>
      <h1>Mis Favoritos</h1>

      {favoriteItems.length === 0 ? (
        <p style={{ color: '#666', marginTop: '20px' }}>
          No tenés productos favoritos todavía. ¡Agregá alguno con el ❤️!
        </p>
      ) : (
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(220px, 1fr))', gap: '20px', marginTop: '20px' }}>
          {favoriteItems.map((product) => (
            <div
              key={product.id}
              style={{ border: '1px solid #ddd', borderRadius: '8px', padding: '16px', background: '#fff' }}
            >
              <h3 style={{ margin: '0 0 8px' }}>{product.nombre}</h3>
              {product.descripcion && (
                <p style={{ color: '#666', fontSize: '14px', margin: '0 0 8px' }}>{product.descripcion}</p>
              )}
              <p style={{ fontWeight: 'bold', color: '#28a745', margin: '0 0 12px' }}>${product.precio}</p>
              <button
                onClick={() => navigate(`/products/${product.id}`)}
                style={{
                  padding: '8px 14px',
                  background: '#007bff',
                  color: 'white',
                  border: 'none',
                  borderRadius: '4px',
                  cursor: 'pointer',
                  width: '100%',
                }}
              >
                Ver detalle
              </button>
            </div>
          ))}
        </div>
      )}
    </div>
  )
}
