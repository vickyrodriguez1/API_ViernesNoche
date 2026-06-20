import React from 'react'
import { Link, NavLink, useNavigate } from 'react-router-dom'
import { useSelector } from 'react-redux'
import styles from './Header.module.css'

export default function Header({ userRol, onLogout }) {
  const navigate = useNavigate()
  const favoriteCount = useSelector((state) => state.favorites.items.length)
  const cartCount = useSelector((state) => state.cart.items.length)

  const handleLogout = () => {
    onLogout()
    navigate('/login')
  }

  // Función dinámica para los enlaces comunes
  const getNavLinkClass = ({ isActive }) => 
    isActive ? `${styles.navLink} ${styles.active}` : styles.navLink;

  return (
    <header className={styles.header}>
      <Link to="/" className={styles.logo}>
        E-Commerce
      </Link>
      
      <nav className={styles.nav}>
        <NavLink to="/products" className={getNavLinkClass}>
          Productos
        </NavLink>
        
        <NavLink to="/favorites" className={getNavLinkClass}>
          Favoritos {favoriteCount > 0 ? `(${favoriteCount})` : ''}
        </NavLink>
        
        <NavLink to="/checkout" className={getNavLinkClass}>
          Checkout {cartCount > 0 ? `(${cartCount})` : ''}
        </NavLink>
        
        {userRol === 'ADMIN' && (
          <NavLink 
            to="/create-product" 
            className={({ isActive }) => 
              isActive ? `${styles.navLink} ${styles.active}` : `${styles.navLink} ${styles.admin}`
            }
          >
            Crear producto
          </NavLink>
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