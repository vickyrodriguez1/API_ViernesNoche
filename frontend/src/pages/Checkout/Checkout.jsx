import React, { useEffect, useState } from 'react'

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

  if (loading) return <p style={{ textAlign: 'center', marginTop: '40px' }}>Cargando carrito...</p>

  return (
    <div style={{ maxWidth: '500px', margin: '30px auto', padding: '25px', border: '1px solid #ddd', borderRadius: '8px' }}>
      <h2 style={{ textAlign: 'center', marginBottom: '20px' }}>Checkout</h2>

      {error && (
        <div style={{ color: 'red', backgroundColor: '#ffe6e6', padding: '10px', borderRadius: '4px', marginBottom: '15px', textAlign: 'center' }}>
          {error}
        </div>
      )}
      {mensaje && (
        <div style={{ color: 'green', backgroundColor: '#e6ffe6', padding: '10px', borderRadius: '4px', marginBottom: '15px', textAlign: 'center' }}>
          {mensaje}
        </div>
      )}

      {!carrito || carrito.productos?.length === 0 ? (
        <p style={{ textAlign: 'center', color: '#666' }}>Tu carrito está vacío.</p>
      ) : (
        <>
          <ul style={{ listStyle: 'none', padding: 0 }}>
            {carrito.productos.map((p) => (
              <li key={p.id} style={{ padding: '10px 0', borderBottom: '1px solid #eee' }}>
                {p.nombre} — ${p.precio}
              </li>
            ))}
          </ul>
          <p style={{ fontSize: '20px', fontWeight: 'bold', marginTop: '15px' }}>
            Total: ${carrito.total}
          </p>
          <button
            type="button"
            onClick={handlePagar}
            disabled={pagando}
            style={{
              width: '100%',
              marginTop: '15px',
              padding: '12px',
              background: pagando ? '#ccc' : '#007bff',
              color: 'white',
              border: 'none',
              borderRadius: '4px',
              fontSize: '16px',
              cursor: pagando ? 'not-allowed' : 'pointer',
            }}
          >
            {pagando ? 'Procesando...' : 'Confirmar compra'}
          </button>
        </>
      )}
    </div>
  )
}
