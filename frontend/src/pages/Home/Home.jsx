import React from 'react'
import { Link } from 'react-router-dom'
import styles from './Home.module.css'

export default function Home({ userRol }) {
  return (
    <div className={styles.container}>
      <span className={styles.eyebrow}>Bienvenido</span>
      <h1 className={styles.title}>
        Tu tienda <span className={styles.highlight}>E-Commerce</span>
      </h1>

      <p className={styles.subtitle}>
        Explorá nuestros productos y gestioná tu carrito de compras en un solo lugar.
      </p>

      <div className={styles.buttonsContainer}>
        <Link to="/products" className={`${styles.btn} ${styles.btnPrimary}`}>
          Ver productos
        </Link>
        <Link to="/checkout" className={`${styles.btn} ${styles.btnSecondary}`}>
          Ir al checkout
        </Link>
        {userRol === 'ADMIN' && (
          <Link to="/create-product" className={`${styles.btn} ${styles.btnSuccess}`}>
            ＋ Crear producto
          </Link>
        )}
      </div>
    </div>
  )
}
