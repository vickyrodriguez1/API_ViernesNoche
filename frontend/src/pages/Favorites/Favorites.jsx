import React from 'react'
import { useSelector } from 'react-redux'
import { Link } from 'react-router-dom'
import CardProductos from '../../components/CardProductos'

export default function Favorites() {
  const favorites = useSelector((state) => state.favorites.items)

  return (
    <div style={{ padding: '24px', maxWidth: '1200px', margin: '0 auto' }}>
      <h2 style={{ fontSize: '28px', marginBottom: '18px' }}>Productos favoritos</h2>
      {favorites.length === 0 ? (
        <div style={{ textAlign: 'center', color: '#555' }}>
          <p>No tienes productos marcados como favoritos todavía.</p>
          <Link to="/products" style={{ color: '#2563eb', textDecoration: 'none', fontWeight: '700' }}>
            Ver productos
          </Link>
        </div>
      ) : (
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(260px, 1fr))', gap: '20px' }}>
          {favorites.map((product) => (
            <CardProductos key={product.id} product={product} />
          ))}
        </div>
      )}
    </div>
  )
}
