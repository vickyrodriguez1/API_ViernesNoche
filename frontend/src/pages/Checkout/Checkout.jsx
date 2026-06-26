import React, { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { fetchCart, clearCart, pagarCarrito, removeProductFromCart, limpiarMensajes } from '../../store/slices/cartSlice';
import styles from './Checkout.module.css'; // Mantenemos tus estilos

export default function Checkout() {
  const dispatch = useDispatch();
  // Leemos el carrito del store global (Redux) y destructuramos lo que necesitamos.
  // Al estar suscritos con useSelector, si el carrito cambia, esta pantalla se actualiza sola.
  const { items, total, loading, error, ultimoPedido } = useSelector((state) => state.cart);

  // Al entrar, traemos el carrito del backend (fetchCart es un thunk asincronico).
  useEffect(() => {
    dispatch(limpiarMensajes());
    dispatch(fetchCart());
  }, [dispatch]);

  if (loading) return <p className={styles.loading}>Cargando carrito...</p>;

  return (
    <div className={styles.container}>
      <div className={styles.card}>
        <div className={styles.cardHeader}>
          <h2 className={styles.title}>Checkout</h2>
          <p className={styles.cardHeaderSub}>Revisá tu pedido antes de confirmar</p>
        </div>

        <div className={styles.body}>
          {error && <div className={`${styles.alert} ${styles.alertError}`}>{error}</div>}
          {ultimoPedido && <div className={`${styles.alert} ${styles.alertSuccess}`}>¡Pedido #{ultimoPedido.id} confirmado!</div>}

          {/* Renderizado condicional: si hay items mostramos la lista; si no, "carrito vacio".
              El <>...</> es un Fragment: agrupa sin agregar un div extra al HTML. */}
          {items && items.length > 0 ? (
            <>
              <ul className={styles.list}>
                {items.map((p) => (
                  <li key={p.id} className={styles.item}>
                    <span>{p.nombreProducto} (x{p.cantidad})</span>
                    {/* El único cambio realizado es p.precioUnitario */}
                    <span className={styles.itemPrice}>${p.precioUnitario}</span>
                    <button onClick={() => dispatch(removeProductFromCart(p.productoId))} className={styles.removeButton}>
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
                <button onClick={() => dispatch(clearCart())} className={styles.clearButton}>Vaciar carrito</button>
                <button onClick={() => dispatch(pagarCarrito())} className={styles.payButton}>Confirmar compra</button>
              </div>
            </>
          ) : (
            <p className={styles.empty}>Tu carrito está vacío.</p>
          )}
        </div>
      </div>
    </div>
  );
}