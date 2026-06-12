import React, { useEffect, useState } from 'react'
import styles from './Checkout.module.css'

export default function Checkout() {
  const [carrito, setCarrito] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)
  const [mensaje, setMensaje] = useState('')
  const [pagando, setPagando] = useState(false)

  const token = localStorage.getItem('token')
  const userId = localStorage.getItem('userId')

  const cargarCarrito = async () => {
    setLoading(true)
    setError(null)
    try {
      const response = await fetch(`http://localhost:8080/api/carritos/usuario/${userId}`, {
        headers: { Authorization: `Bearer ${token}` },
      })
      if (!response.ok) {
        throw new Error('No se pudo cargar el carrito')
      }
      const data = await response.json()
      setCarrito(data)
    } catch (err) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    if (userId) {
      cargarCarrito()
    } else {
      setLoading(false)
      setError('No hay usuario asociado a la sesión')
    }
  }, [])

  // Auto-dismiss error after 4s
  useEffect(() => {
    if (!error) return
    const timer = setTimeout(() => setError(null), 4000)
    return () => clearTimeout(timer)
  }, [error])

  const handlePagar = async () => {
    if (!carrito?.id) return
    setPagando(true)
    setMensaje('')
    setError(null)
    try {
      const response = await fetch(`http://localhost:8080/api/carritos/${carrito.id}/pagar`, {
        method: 'POST',
        headers: { Authorization: `Bearer ${token}` },
      })
      if (!response.ok) {
        const errorData = await response.json()
        throw new Error(errorData.message || 'Error al procesar el pago')
      }
      const pedido = await response.json()
      setMensaje(`¡Pedido #${pedido.id} confirmado! Total: $${pedido.precioTotal}`)
      cargarCarrito()
    } catch (err) {
      setError(err.message)
    } finally {
      setPagando(false)
    }
  }

  if (loading) return <p className={styles.loading}>Cargando carrito...</p>

  return (
    <div className={styles.container}>
      <div className={styles.card}>
        <div className={styles.cardHeader}>
          <h2 className={styles.title}>Checkout</h2>
          <p className={styles.cardHeaderSub}>Revisá tu pedido antes de confirmar</p>
        </div>

        <div className={styles.body}>
          {error && (
            <div className={`${styles.alert} ${styles.alertError}`}>{error}</div>
          )}
          {mensaje && (
            <div className={`${styles.alert} ${styles.alertSuccess}`}>{mensaje}</div>
          )}

          {!carrito || carrito.productos?.length === 0 ? (
            <p className={styles.empty}>Tu carrito está vacío.</p>
          ) : (
            <>
              <ul className={styles.list}>
                {carrito.productos.map((p) => (
                  <li key={p.id} className={styles.item}>
                    <span>{p.nombre}</span>
                    <span className={styles.itemPrice}>${p.precio}</span>
                  </li>
                ))}
              </ul>
              <div className={styles.totalRow}>
                <span className={styles.totalLabel}>Total</span>
                <span className={styles.totalValue}>${carrito.total}</span>
              </div>
              <button
                type="button"
                onClick={handlePagar}
                disabled={pagando}
                className={styles.payButton}
              >
                {pagando ? 'Procesando...' : 'Confirmar compra'}
              </button>
            </>
          )}
        </div>
      </div>
    </div>
  )
}
