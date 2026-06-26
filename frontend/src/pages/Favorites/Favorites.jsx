import React from 'react'
import { useSelector } from 'react-redux'
import { Link } from 'react-router-dom'
import CardProductos from '../../components/CardProductos'
import styles from './Favorites.module.css'

// Pagina de favoritos. No tiene estado propio: lee la lista del store global.
export default function Favorites() {
  // `favorites` sale del store de Redux (state.favorites.items). Lo escribe el corazon
  // de CardProductos con dispatch(toggleFavorite(...)) y se persiste en localStorage.
  const favorites = useSelector((state) => state.favorites.items)

  return (
    <div className={styles.container}>
      <div className={styles.header}>
        <h2 className={styles.title}>Productos favoritos</h2>
        <p className={styles.subtitle}>
          {favorites.length > 0
            ? `Tenés ${favorites.length} producto${favorites.length > 1 ? 's' : ''} guardado${favorites.length > 1 ? 's' : ''}.`
            : 'Tus productos guardados aparecerán acá.'}
        </p>
      </div>

      {favorites.length === 0 ? (
        <div className={styles.empty}>
          <div className={styles.emptyIcon}>♡</div>
          <p className={styles.emptyText}>
            No tienes productos marcados como favoritos todavía.
          </p>
          <Link to="/" className={styles.emptyLink}>
            Ver productos
          </Link>
        </div>
      ) : (
        <div className={styles.grid}>
          {favorites.map((product) => (
            <CardProductos key={product.id} product={product} />
          ))}
        </div>
      )}
    </div>
  )
}
