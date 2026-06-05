import React, { useEffect, useState } from 'react'
import { Link, useParams } from 'react-router-dom'

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

  if (loading) return <p style={{ textAlign: 'center', marginTop: '40px' }}>Cargando producto...</p>
  if (error) {
    return (
      <div style={{ textAlign: 'center', marginTop: '40px' }}>
        <p>Error: {error}</p>
        <Link to="/products">Volver a productos</Link>
      </div>
    )
  }

  return (
    <div style={{ maxWidth: '500px', margin: '30px auto', padding: '25px', border: '1px solid #ddd', borderRadius: '8px' }}>
      <Link to="/products" style={{ color: '#007bff' }}>← Volver</Link>
      <h2 style={{ marginTop: '15px' }}>{product.nombre}</h2>
      <p style={{ color: '#666' }}>{product.descripcion}</p>
      <p style={{ fontSize: '22px', fontWeight: 'bold', color: '#28a745' }}>${product.precio}</p>
      <p>Stock: {product.stock}</p>
      {product.categorias?.length > 0 && (
        <p>Categorías: {product.categorias.join(', ')}</p>
      )}
    </div>
  )
}
