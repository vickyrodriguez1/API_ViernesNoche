import React, { useState } from 'react'
import { useSelector, useDispatch } from 'react-redux'
import { Link } from 'react-router-dom'
import {
  incrementQuantity,
  decrementQuantity,
  removeFromCart,
  clearCart,
} from '../../store/slices/cartSlice'

export default function Checkout() {
  const dispatch = useDispatch()
  const cartItems = useSelector((state) => state.cart.items)
  const [message, setMessage] = useState('')

  const total = cartItems.reduce(
    (sum, item) => sum + item.precio * item.quantity,
    0,
  )

  const handleConfirmPurchase = () => {
    if (cartItems.length === 0) {
      setMessage('No hay artículos en el carrito para procesar la compra.')
      return
    }
    dispatch(clearCart())
    setMessage('¡Compra confirmada! Gracias por tu pedido.')
  }

  return (
    <div style={{ maxWidth: '700px', margin: '30px auto', padding: '25px', border: '1px solid #ddd', borderRadius: '8px' }}>
      <h2 style={{ textAlign: 'center', marginBottom: '20px' }}>Carrito de compras</h2>

      {message && (
        <div style={{
          color: '#155724',
          backgroundColor: '#d4edda',
          padding: '12px',
          borderRadius: '6px',
          marginBottom: '18px',
          textAlign: 'center',
        }}>
          {message}
        </div>
      )}

      {cartItems.length === 0 ? (
        <div style={{ textAlign: 'center', color: '#666' }}>
          <p>Tu carrito está vacío.</p>
          <Link to="/products" style={{ color: '#007bff' }}>
            Ver productos
          </Link>
        </div>
      ) : (
        <>
          <div style={{ marginBottom: '24px' }}>
            {cartItems.map((item) => (
              <div
                key={item.id}
                style={{
                  display: 'grid',
                  gridTemplateColumns: 'auto 1fr auto',
                  gap: '16px',
                  alignItems: 'center',
                  padding: '16px',
                  borderBottom: '1px solid #eee',
                }}
              >
                <div style={{ minWidth: '100px' }}>
                  {item.imagen ? (
                    <img
                      src={item.imagen}
                      alt={item.nombre}
                      style={{ width: '100%', borderRadius: '8px', objectFit: 'cover' }}
                    />
                  ) : (
                    <div style={{
                      width: '100px',
                      height: '100px',
                      background: '#f2f2f2',
                      borderRadius: '8px',
                      display: 'flex',
                      alignItems: 'center',
                      justifyContent: 'center',
                      color: '#999',
                    }}>
                      Sin imagen
                    </div>
                  )}
                </div>

                <div>
                  <h3 style={{ margin: '0 0 6px 0' }}>{item.nombre}</h3>
                  <p style={{ margin: 0, color: '#666' }}>${item.precio} por unidad</p>
                  <p style={{ margin: '8px 0 0 0', color: '#333', fontWeight: '600' }}>
                    Subtotal: ${item.precio * item.quantity}
                  </p>
                </div>

                <div style={{ textAlign: 'right' }}>
                  <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'flex-end', gap: '8px', marginBottom: '10px' }}>
                    <button
                      type="button"
                      onClick={() => dispatch(decrementQuantity(item.id))}
                      style={{ padding: '6px 10px', borderRadius: '6px', border: '1px solid #ddd', background: '#fff', cursor: 'pointer' }}
                    >
                      −
                    </button>
                    <span style={{ minWidth: '24px', textAlign: 'center' }}>{item.quantity}</span>
                    <button
                      type="button"
                      onClick={() => dispatch(incrementQuantity(item.id))}
                      disabled={item.stock !== undefined && item.quantity >= item.stock}
                      style={{ padding: '6px 10px', borderRadius: '6px', border: '1px solid #ddd', background: '#fff', cursor: 'pointer' }}
                    >
                      +
                    </button>
                  </div>
                  <button
                    type="button"
                    onClick={() => dispatch(removeFromCart(item.id))}
                    style={{ padding: '8px 12px', borderRadius: '6px', border: '1px solid #dc3545', background: '#fff', color: '#dc3545', cursor: 'pointer' }}
                  >
                    Eliminar
                  </button>
                </div>
              </div>
            ))}
          </div>

          <div style={{ display: 'flex', flexDirection: 'column', gap: '14px' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', fontWeight: '700', fontSize: '18px' }}>
              <span>Total</span>
              <span>${total}</span>
            </div>
            <button
              type="button"
              onClick={handleConfirmPurchase}
              style={{
                width: '100%',
                padding: '14px',
                background: '#007bff',
                color: '#fff',
                border: 'none',
                borderRadius: '8px',
                fontSize: '16px',
                cursor: 'pointer',
              }}
            >
              Confirmar compra
            </button>
            <button
              type="button"
              onClick={() => dispatch(clearCart())}
              style={{
                width: '100%',
                padding: '14px',
                background: '#f8f9fa',
                color: '#333',
                border: '1px solid #ddd',
                borderRadius: '8px',
                fontSize: '16px',
                cursor: 'pointer',
              }}
            >
              Vaciar carrito
            </button>
          </div>
        </>
      )}
    </div>
  )
}
