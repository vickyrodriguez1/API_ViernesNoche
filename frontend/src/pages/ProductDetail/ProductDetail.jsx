import React, { useEffect, useState } from 'react'
import { Link, useParams } from 'react-router-dom'
import styles from './ProductDetail.module.css'

export default function ProductDetail() {
  const { id } = useParams()
  const [product, setProduct] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)

  useEffect(() => {
    const fetchProduct = async () => {
      try {
        const response = await fetch(`http://localhost:8080/api/productos/${id}`)
        if (!response.ok) {
          throw new Error('Producto no encontrado')
        }
        const data = await response.json()
        setProduct(data)
      } catch (err) {
        setError(err.message)
      } finally {
        setLoading(false)
      }
    }

    fetchProduct()
  }, [id])

  if (loading) {
    return (
      <div className={styles.container}>
        <div className={styles.stateMessage}>Cargando producto...</div>
      </div>
    )
  }

  if (error) {
    return (
      <div className={styles.container}>
        <div className={styles.stateMessage}>
          <p>Error: {error}</p>
          <Link to="/products">← Volver a productos</Link>
        </div>
      </div>
    )
  }

  return (
    <div className={styles.container}>
      <Link to="/products" className={styles.back}>
        ← Volver a productos
      </Link>

      <div className={styles.card}>
        <div className={styles.imageArea}>
          {product.imagenBase64 ? (
            <img
              src={product.imagenBase64}
              alt={product.nombre}
             // style={{ width: '100%', height: '100%', objectFit: 'cover' }}
            />
          ) : (
            'Sin imagen'
          )}
        </div>

        <div className={styles.body}>
          <div className={styles.titleRow}>
            <h2 className={styles.title}>{product.nombre}</h2>
            <span className={styles.price}>${product.precio}</span>
          </div>

          {product.descripcion && (
            <p className={styles.description}>{product.descripcion}</p>
          )}

          <div className={styles.meta}>
            <span className={`${styles.chip} ${styles.chipStock}`}>
              Stock: {product.stock}
            </span>
            {product.categorias?.map((cat) => (
              <span key={cat} className={styles.chip}>
                {cat}
              </span>
            ))}
          </div>
        </div>
      </div>
    </div>
  )
}
