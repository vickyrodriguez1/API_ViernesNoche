import React from 'react'
import { Link, NavLink, useNavigate } from 'react-router-dom'
import { useSelector } from 'react-redux'
import styles from './Header.module.css'

// Barra de navegacion. Solo se muestra cuando hay sesion iniciada
// (cuelga de las rutas protegidas), por eso siempre pinta la seccion de usuario.
export default function Header({ userRol, onLogout }) {
  const navigate = useNavigate()
  // Contadores que leemos del store global (Redux): favoritos y unidades en el carrito.
  const favoriteCount = useSelector((state) => state.favorites.items.length)
  const cartCount = useSelector((state) =>
    state.cart.items.reduce((total, item) => total + item.cantidad, 0)
  )

  const handleLogout = () => {
    onLogout()
    navigate('/login')
  }

  // Clase dinamica para resaltar el link activo
  const getNavLinkClass = ({ isActive }) =>
    isActive ? `${styles.navLink} ${styles.active}` : styles.navLink

  return (
    <header className={styles.header}>
      <Link to="/" className={styles.logo}>
        E-Commerce
      </Link>

      <nav className={styles.nav}>
        {/* "end" hace que "Productos" solo se marque activo en la home exacta "/" */}
        <NavLink to="/" end className={getNavLinkClass}>
          Productos
        </NavLink>

        <NavLink to="/favorites" className={getNavLinkClass}>
          Favoritos {favoriteCount > 0 ? `(${favoriteCount})` : ''}
        </NavLink>

        <NavLink to="/checkout" className={getNavLinkClass}>
          Carrito {cartCount > 0 ? `(${cartCount})` : ''}
        </NavLink>

        {userRol === 'ADMIN' && (
          <NavLink to="/create-product" className={getNavLinkClass}>
            Crear producto
          </NavLink>
        )}
      </nav>

      <div className={styles.userSection}>
        <span className={styles.userRol}>
          Rol: <span className={styles.rolLabel}>{userRol}</span>
        </span>
        <button type="button" onClick={handleLogout} className={styles.logoutBtn}>
          Cerrar sesión
        </button>
      </div>
    </header>
  )
}
