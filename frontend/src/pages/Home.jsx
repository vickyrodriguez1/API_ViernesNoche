import React from 'react'
import { Link } from 'react-router-dom'

export default function Home({ userRol }) {
  return (
    <div style={{ maxWidth: '600px', margin: '40px auto', padding: '20px', textAlign: 'center' }}>
      <h1>Bienvenido al E-Commerce</h1>
      <p style={{ color: '#666', marginBottom: '25px' }}>
        Sesión iniciada como <strong>{userRol}</strong>
      </p>
      <div style={{ display: 'flex', gap: '10px', justifyContent: 'center', flexWrap: 'wrap' }}>
        <Link
          to="/products"
          style={{
            padding: '10px 20px',
            background: '#007bff',
            color: 'white',
            textDecoration: 'none',
            borderRadius: '4px',
          }}
        >
          Ver productos
        </Link>
        <Link
          to="/checkout"
          style={{
            padding: '10px 20px',
            background: '#6c757d',
            color: 'white',
            textDecoration: 'none',
            borderRadius: '4px',
          }}
        >
          Ir al checkout
        </Link>
        {userRol === 'ADMIN' && (
          <Link
            to="/create-product"
            style={{
              padding: '10px 20px',
              background: '#28a745',
              color: 'white',
              textDecoration: 'none',
              borderRadius: '4px',
            }}
          >
            Crear producto
          </Link>
        )}
      </div>
    </div>
  )
}
