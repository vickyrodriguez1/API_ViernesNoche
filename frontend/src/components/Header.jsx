import React from 'react'
import { Link, useNavigate } from 'react-router-dom'

export default function Header({ userRol, onLogout }) {
  const navigate = useNavigate()

  const handleLogout = () => {
    onLogout()
    navigate('/login')
  }

  return (
    <header style={{
      display: 'flex',
      justifyContent: 'space-between',
      alignItems: 'center',
      padding: '10px 20px',
      background: '#f8f9fa',
      borderBottom: '1px solid #ddd',
    }}>
      <nav style={{ display: 'flex', gap: '15px', alignItems: 'center' }}>
        <Link to="/" style={{ fontWeight: 'bold', textDecoration: 'none', color: '#333' }}>
          E-Commerce
        </Link>
        <Link to="/products" style={{ textDecoration: 'none', color: '#007bff' }}>Productos</Link>
        <Link to="/checkout" style={{ textDecoration: 'none', color: '#007bff' }}>Checkout</Link>
        {userRol === 'ADMIN' && (
          <Link to="/create-product" style={{ textDecoration: 'none', color: '#28a745' }}>
            Crear producto
          </Link>
        )}
      </nav>
      <div>
        <span style={{ marginRight: '15px', fontWeight: 'bold' }}>
          Rol:{' '}
          <span style={{ color: userRol === 'ADMIN' ? '#28a745' : '#007bff' }}>{userRol}</span>
        </span>
        <button
          type="button"
          onClick={handleLogout}
          style={{
            padding: '6px 16px',
            background: '#dc3545',
            color: 'white',
            border: 'none',
            borderRadius: '4px',
            cursor: 'pointer',
          }}
        >
          Cerrar sesión
        </button>
      </div>
    </header>
  )
}
