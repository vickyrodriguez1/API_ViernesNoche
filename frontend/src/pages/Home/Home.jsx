import React from 'react'
import { Link } from 'react-router-dom'
import styles from './Home.module.css'

export default function Home({ userRol }) {
  return (
    <div className={styles.container}>
      <h1 className={styles.title}>Bienvenido al E-Commerce</h1>
      
      <p className={styles.subtitle}>
        Explora nuestros productos y gestiona tu carrito de compras
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
            ➕ Crear producto
          </Link>
        )}
      </div>
    </div>
  )
}
