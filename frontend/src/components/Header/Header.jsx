import React from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useSelector } from 'react-redux'
import styles from './Header.module.css'

export default function Header({ userRol, onLogout }) {
  const navigate = useNavigate()
  const favoriteCount = useSelector((state) => state.favorites.items.length)
  const cartCount = useSelector((state) =>
    state.cart.items.reduce((sum, item) => sum + item.quantity, 0),
  )

  const handleLogout = () => {
    onLogout()
    navigate('/login')
  }

  return (
    <header className={styles.header}>
      <Link to="/" className={styles.logo}>
        E-Commerce
      </Link>
      
      <nav className={styles.nav}>
        <Link to="/products" className={styles.navLink}>
          Productos
        </Link>
        <Link to="/favorites" className={styles.navLink}>
          Favoritos {favoriteCount > 0 ? `(${favoriteCount})` : ''}
        </Link>
        <Link to="/checkout" className={styles.navLink}>
          Carrito {cartCount > 0 ? `(${cartCount})` : ''}
        </Link>
        {userRol === 'ADMIN' && (
          <Link to="/create-product" className={`${styles.navLink} ${styles.admin}`}>
            Crear producto
          </Link>
        )}
      </nav>

      <div className={styles.userSection}>
        <span className={styles.userRol}>
          Rol:{' '}
          <span className={userRol === 'ADMIN' ? styles.rolLabelAdmin : styles.rolLabel}>
            {userRol}
          </span>
        </span>
        <button
          type="button"
          onClick={handleLogout}
          className={styles.logoutBtn}
        >
          Cerrar sesión
        </button>
      </div>
    </header>
  )
}
