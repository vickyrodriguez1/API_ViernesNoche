import React, { useEffect } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import {
  fetchCart,
  clearCart,
  pagarCarrito,
  removeProductFromCart,
  limpiarMensajes,
} from '../../store/slices/cartSlice'
import styles from './Checkout.module.css'

export default function Checkout() {
  const dispatch = useDispatch()

  // Leemos el estado del carrito desde el store global de Redux.
  // useSelector vuelve a renderizar el componente cada vez que estos datos cambian.
  const items = useSelector((state) => state.cart.items)
  const total = useSelector((state) => state.cart.total)
  const loading = useSelector((state) => state.cart.loading)
  const error = useSelector((state) => state.cart.error)
  const ultimoPedido = useSelector((state) => state.cart.ultimoPedido)

  // Al montar el componente, pedimos el carrito a la API (acción GET).
  useEffect(() => {
    dispatch(limpiarMensajes()) // limpiamos carteles viejos
    dispatch(fetchCart())
  }, [dispatch])

  const handleEliminar = (productoId) => {
    dispatch(removeProductFromCart(productoId)) // DELETE producto
  }

  const handleVaciar = () => {
    dispatch(clearCart()) // DELETE vaciar
  }

  const handlePagar = () => {
    dispatch(pagarCarrito()) // POST pagar
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
          {ultimoPedido && (
            <div className={`${styles.alert} ${styles.alertSuccess}`}>
              ¡Pedido #{ultimoPedido.id} confirmado! Total: ${ultimoPedido.precioTotal}
            </div>
          )}

          {items.length === 0 ? (
            <p className={styles.empty}>Tu carrito está vacío.</p>
          ) : (
            <>
              <ul className={styles.list}>
                {items.map((p) => (
                  <li key={p.id} className={styles.item}>
                    <span>{p.nombre}</span>
                    <span className={styles.itemPrice}>${p.precio}</span>
                    <button
                      type="button"
                      onClick={() => handleEliminar(p.id)}
                      className={styles.removeButton}
                    >
                      Eliminar
                    </button>
                  </li>
                ))}
              </ul>
              <div className={styles.totalRow}>
                <span className={styles.totalLabel}>Total</span>
                <span className={styles.totalValue}>${total}</span>
              </div>
              <div className={styles.actions}>
                <button
                  type="button"
                  onClick={handleVaciar}
                  className={styles.clearButton}
                >
                  Vaciar carrito
                </button>
                <button
                  type="button"
                  onClick={handlePagar}
                  className={styles.payButton}
                >
                  Confirmar compra
                </button>
              </div>
            </>
          )}
        </div>
      </div>
    </div>
  )
}
