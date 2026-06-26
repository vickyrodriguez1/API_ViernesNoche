import { useEffect, useState } from 'react'
import { Link, useParams, useNavigate } from 'react-router-dom'
import { useDispatch } from 'react-redux'
import { addProductToCart } from '../../store/slices/cartSlice'
import { showSnackbar } from '../../store/slices/uiSlice'
import ConfirmModal from '../../components/ConfirmModal'
import styles from './ProductDetail.module.css'

// Detalle del producto. Recibe por props (desde App.jsx) si hay sesion (isLoggedIn)
// y el rol (userRol), que usa para mostrar o no el panel de administracion.
export default function ProductDetail({ isLoggedIn, userRol }) {
  const { id } = useParams() // useParams lee el :id de la URL (/products/:id)
  const navigate = useNavigate()
  const dispatch = useDispatch()

  const [product, setProduct] = useState(null)
  const [categorias, setCategorias] = useState([]) // catalogo de categorias (para el PUT del admin)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)
  const [nuevoStock, setNuevoStock] = useState('') // input de stock del panel admin
  const [confirmOpen, setConfirmOpen] = useState(false) // modal de confirmar borrado

  // Traemos el producto por id cada vez que cambia el :id de la URL
  useEffect(() => {
    const fetchProduct = async () => {
      try {
        const response = await fetch(`http://localhost:8080/api/productos/${id}`)
        if (!response.ok) {
          throw new Error('Producto no encontrado')
        }
        const data = await response.json()
        setProduct(data)
        setNuevoStock(data.stock)
      } catch (err) {
        setError(err.message)
      } finally {
        setLoading(false)
      }
    }
    fetchProduct()
  }, [id]) // dependencia [id]: si navegas a otro producto, se vuelve a ejecutar el fetch

  // Traemos las categorias una sola vez (las necesita el admin para no perderlas al editar)
  useEffect(() => {
    const fetchCategorias = async () => {
      try {
        const res = await fetch('http://localhost:8080/api/categorias')
        if (res.ok) setCategorias(await res.json())
      } catch {
        /* si falla no rompe la pantalla */
      }
    }
    fetchCategorias()
  }, [])

  // La imagen puede venir como imagen_base64 (clave del backend) o imagenBase64
  const fotoProducto = product?.imagen_base64 || product?.imagenBase64
  const sinStock = product && product.stock <= 0

  // ---- Cliente: agregar al carrito (llamada asincronica via thunk de Redux) ----
  const handleAddToCart = async () => {
    if (!isLoggedIn) {
      navigate('/login') // para comprar hay que iniciar sesion
      return
    }
    try {
      // .unwrap() hace que, si el thunk falla (rejected), lance el error y caiga al catch.
      await dispatch(addProductToCart(product.id)).unwrap()
      dispatch(showSnackbar({ message: 'Producto agregado al carrito', type: 'success' }))
    } catch {
      dispatch(showSnackbar({ message: 'No se pudo agregar al carrito.', type: 'error' }))
    }
  }

  // ---- Admin: pasa los NOMBRES de categoria del producto a sus IDs (para el PUT) ----
  const obtenerCategoriaIds = () => {
    if (!product.categorias) return []
    return categorias
      .filter((c) => product.categorias.includes(c.nombre))
      .map((c) => c.id)
  }

  // ---- Admin: actualizar stock (PUT /api/productos/:id) ----
  // PUT = modificar un recurso existente. Mandamos el token en el header porque es
  // una accion protegida (el backend exige rol ADMIN).
  const handleUpdateStock = async (e) => {
    e.preventDefault() // evita que el form recargue la pagina
    const token = localStorage.getItem('token')
    const body = {
      nombre: product.nombre,
      descripcion: product.descripcion,
      precio: product.precio,
      stock: parseInt(nuevoStock),
      categoriaIds: obtenerCategoriaIds(),
      imagen_base64: fotoProducto,
    }
    try {
      const res = await fetch(`http://localhost:8080/api/productos/${product.id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json', Authorization: `Bearer ${token}` },
        body: JSON.stringify(body),
      })
      if (!res.ok) {
        const errData = await res.json()
        throw new Error(errData.message || 'No se pudo actualizar el stock')
      }
      const actualizado = await res.json()
      setProduct(actualizado)
      dispatch(showSnackbar({ message: 'Stock actualizado', type: 'success' }))
    } catch (err) {
      dispatch(showSnackbar({ message: err.message, type: 'error' }))
    }
  }

  // ---- Admin: eliminar producto (DELETE /api/productos/:id) ----
  // El click solo abre el modal; el borrado real ocurre al confirmar.
  const confirmarEliminar = async () => {
    setConfirmOpen(false)
    const token = localStorage.getItem('token')
    try {
      const res = await fetch(`http://localhost:8080/api/productos/${product.id}`, {
        method: 'DELETE',
        headers: { Authorization: `Bearer ${token}` },
      })
      if (!res.ok) {
        // Intentamos leer el mensaje que manda el backend (ej. 409 por integridad).
        let msg = 'No se pudo eliminar el producto'
        try {
          const errData = await res.json()
          msg = errData.message || msg
        } catch { /* la respuesta puede no traer body */ }
        throw new Error(msg)
      }
      dispatch(showSnackbar({ message: 'Producto eliminado', type: 'success' }))
      navigate('/') // volvemos al catalogo
    } catch (err) {
      dispatch(showSnackbar({ message: err.message, type: 'error' }))
    }
  }

  // Renderizado condicional por estado: primero "cargando", si falla "error", y si
  // todo OK, recien se pinta el detalle (mas abajo).
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
          <Link to="/">← Volver al catálogo</Link>
        </div>
      </div>
    )
  }

  return (
    <div className={styles.container}>
      <Link to="/" className={styles.back}>
        ← Volver al catálogo
      </Link>

      <div className={styles.card}>
        <div className={styles.imageArea}>
          {fotoProducto ? <img src={fotoProducto} alt={product.nombre} /> : 'Sin imagen'}
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
            {sinStock ? (
              <span className={`${styles.chip} ${styles.chipNoStock}`}>Sin stock</span>
            ) : (
              <span className={`${styles.chip} ${styles.chipStock}`}>Stock: {product.stock}</span>
            )}
            {product.categorias?.map((cat) => (
              <span key={cat} className={styles.chip}>
                {cat}
              </span>
            ))}
          </div>

          {/* El feedback (éxito/error) se muestra con el Snackbar global, que se
              cierra solo y usa rojo para errores; así queda estandarizado con el resto. */}

          {/* Accion de compra: si no hay stock, no se puede agregar */}
          <div className={styles.actions}>
            {sinStock ? (
              <p className={styles.noStockText}>
                Este producto no tiene stock disponible.
              </p>
            ) : (
              <button type="button" className={styles.addButton} onClick={handleAddToCart}>
                Agregar al carrito
              </button>
            )}
          </div>

          {/* Renderizado condicional por rol: este panel se muestra SOLO si userRol
              es ADMIN. Igual, la seguridad real la hace el backend (defensa en profundidad). */}
          {userRol === 'ADMIN' && (
            <div className={styles.adminPanel}>
              <h3 className={styles.adminTitle}>Gestión (Admin)</h3>
              <form onSubmit={handleUpdateStock} className={styles.stockForm}>
                <label className={styles.adminLabel}>Stock</label>
                <input
                  type="number"
                  min="0"
                  className={styles.stockInput}
                  value={nuevoStock}
                  onChange={(e) => setNuevoStock(e.target.value)}
                />
                <button type="submit" className={styles.updateButton}>
                  Actualizar stock
                </button>
              </form>
              <button
                type="button"
                className={styles.deleteButton}
                onClick={() => setConfirmOpen(true)}
              >
                Eliminar producto
              </button>
            </div>
          )}
        </div>
      </div>

      <ConfirmModal
        open={confirmOpen}
        title="Eliminar producto"
        message={`¿Seguro que querés eliminar "${product.nombre}"? Esta acción no se puede deshacer.`}
        confirmText="Eliminar"
        cancelText="Cancelar"
        variant="danger"
        onConfirm={confirmarEliminar}
        onCancel={() => setConfirmOpen(false)}
      />
    </div>
  )
}
