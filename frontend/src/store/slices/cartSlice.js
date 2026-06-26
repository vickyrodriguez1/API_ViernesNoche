// =============================================================================
// cartSlice.js - Estado global del CARRITO con Redux Toolkit
// =============================================================================
// Este slice cumple la misma función que tendría un "CartProvider" de useContext,
// pero usando Redux Toolkit. Acá vive TODO el estado del carrito (los productos,
// el total, si está cargando, si hubo error) y las funciones que hablan con la API.
//
// createSlice    -> crea el reducer + el estado inicial de forma corta.
// createAsyncThunk -> crea acciones ASINCRÓNICAS (las que llaman a la API).
//                     Cada thunk genera automáticamente 3 acciones:
//                       - pending   : la petición arrancó (todavía no respondió)
//                       - fulfilled : la API respondió OK
//                       - rejected  : la API falló
// =============================================================================
import { createSlice, createAsyncThunk } from '@reduxjs/toolkit'

// URL base de la API del carrito (la que ya existe en el backend de Spring Boot)
const API_URL = 'http://localhost:8080/api/carritos'

// Helpers para no repetir código en cada llamada -----------------------------
// El token JWT y el id de usuario los guardamos en localStorage al hacer login.
const getToken = () => localStorage.getItem('token')
const getUserId = () => localStorage.getItem('userId')

// Headers que mandamos en cada request: tipo JSON + el token para autenticarnos.
const authHeaders = () => ({
  'Content-Type': 'application/json',
  Authorization: `Bearer ${getToken()}`,
})

// -----------------------------------------------------------------------------
// GET -> traer (o crear) el carrito del usuario logueado
// El backend, si el usuario no tiene carrito, le crea uno y lo devuelve.
// -----------------------------------------------------------------------------
export const fetchCart = createAsyncThunk('cart/fetchCart', async () => {
  const response = await fetch(`${API_URL}/usuario/${getUserId()}`, {
    method: 'GET',
    headers: authHeaders(),
  })
  if (!response.ok) {
    throw new Error('No se pudo cargar el carrito')
  }
  // Devuelve el CarritoResponseDTO: { id, usuarioId, productos: [...], total }
  return await response.json()
})

// Helper interno: antes de un POST/DELETE necesitamos el id del carrito.
// Si ya lo tenemos en el estado global lo usamos; si no, lo pedimos a la API.
const obtenerCarritoId = async (getState) => {
  const idActual = getState().cart.carritoId
  if (idActual) return idActual

  const response = await fetch(`${API_URL}/usuario/${getUserId()}`, {
    headers: authHeaders(),
  })
  if (!response.ok) {
    throw new Error('No se pudo cargar el carrito')
  }
  const carrito = await response.json()
  return carrito.id
}

// -----------------------------------------------------------------------------
// POST -> agregar un producto al carrito
// Recibe el id del producto (productoId) desde el componente.
// -----------------------------------------------------------------------------
export const addProductToCart = createAsyncThunk(
  'cart/addProductToCart',
  async (productoId, { getState }) => {
    const carritoId = await obtenerCarritoId(getState)
    const response = await fetch(`${API_URL}/${carritoId}/productos`, {
      method: 'POST',
      headers: authHeaders(),
      body: JSON.stringify({ productoId }),
    })
    if (!response.ok) {
      throw new Error('No se pudo agregar el producto al carrito')
    }
    // El backend devuelve el carrito ya actualizado
    return await response.json()
  }
)

// -----------------------------------------------------------------------------
// DELETE -> eliminar un producto del carrito
// -----------------------------------------------------------------------------
export const removeProductFromCart = createAsyncThunk(
  'cart/removeProductFromCart',
  async (productoId, { getState }) => {
    const carritoId = await obtenerCarritoId(getState)
    const response = await fetch(`${API_URL}/${carritoId}/productos/${productoId}`, {
      method: 'DELETE',
      headers: authHeaders(),
    })
    if (!response.ok) {
      throw new Error('No se pudo eliminar el producto del carrito')
    }
    return await response.json()
  }
)

// -----------------------------------------------------------------------------
// DELETE -> vaciar el carrito (saca todos los productos)
// -----------------------------------------------------------------------------
export const clearCart = createAsyncThunk(
  'cart/clearCart',
  async (_, { getState }) => {
    const carritoId = await obtenerCarritoId(getState)
    const response = await fetch(`${API_URL}/${carritoId}/vaciar`, {
      method: 'DELETE',
      headers: authHeaders(),
    })
    if (!response.ok) {
      throw new Error('No se pudo vaciar el carrito')
    }
    return await response.json()
  }
)

// -----------------------------------------------------------------------------
// POST -> pagar el carrito: el backend crea el PEDIDO y vacía el carrito
// Devuelve el PedidoResponseDTO (con id y precioTotal).
// -----------------------------------------------------------------------------
export const pagarCarrito = createAsyncThunk(
  'cart/pagarCarrito',
  async (_, { getState }) => {
    const carritoId = await obtenerCarritoId(getState)
    const response = await fetch(`${API_URL}/${carritoId}/pagar`, {
      method: 'POST',
      headers: authHeaders(),
    })
    if (!response.ok) {
      const errorData = await response.json()
      throw new Error(errorData.message || 'No se pudo procesar el pago')
    }
    return await response.json()
  }
)

// =============================================================================
// EL SLICE: estado inicial + reducers síncronos + extraReducers (los async)
// =============================================================================
const cartSlice = createSlice({
  name: 'cart',
  initialState: {
    carritoId: null, // id del carrito en el backend
    items: [], // lista de productos del carrito (state.cart.items)
    total: 0, // total a pagar
    loading: false, // true mientras esperamos la respuesta de la API
    error: null, // mensaje de error si algo falla
    ultimoPedido: null, // el pedido que se generó al pagar (para el cartel de éxito)
  },

  // Reducers síncronos (no llaman a la API).
  reducers: {
    limpiarMensajes: (state) => {
      state.error = null
      state.ultimoPedido = null
    },
    // Vacía el carrito EN EL ESTADO (no en la API). Lo usamos al cerrar sesión
    // para que el carrito del usuario anterior no quede "pegado" al loguearse otro.
    resetCart: (state) => {
      state.carritoId = null
      state.items = []
      state.total = 0
      state.loading = false
      state.error = null
      state.ultimoPedido = null
    },
  },

  // extraReducers maneja las acciones que generan los createAsyncThunk de arriba.
  extraReducers: (builder) => {
    // Como fetch / add / remove / clear devuelven todos el carrito actualizado,
    // usamos esta MISMA función para guardar la respuesta en el estado y no repetir.
    const guardarCarrito = (state, action) => {
      state.carritoId = action.payload.id
      state.items = action.payload.items || []
      state.total = action.payload.total || 0
      state.loading = false
      state.error = null
    }

    builder
      // ---- GET (traer carrito) ----
      // Solo fetchCart maneja `loading` (con su caso .pending) porque es la carga
      // INICIAL del carrito, donde mostramos "Cargando carrito..." en el Checkout.
      // Las demas acciones (agregar/quitar/vaciar/pagar) son rapidas y no bloquean la
      // UI con un loading; igualmente, guardarCarrito (el fulfilled) deja loading=false.
      .addCase(fetchCart.pending, (state) => {
        state.loading = true
        state.error = null
      })
      .addCase(fetchCart.fulfilled, guardarCarrito)
      .addCase(fetchCart.rejected, (state, action) => {
        state.loading = false
        state.error = action.error.message
      })

      // ---- POST (agregar producto) ----
      .addCase(addProductToCart.fulfilled, guardarCarrito)
      .addCase(addProductToCart.rejected, (state, action) => {
        state.error = action.error.message
      })

      // ---- DELETE (eliminar producto) ----
      .addCase(removeProductFromCart.fulfilled, guardarCarrito)
      .addCase(removeProductFromCart.rejected, (state, action) => {
        state.error = action.error.message
      })

      // ---- DELETE (vaciar carrito) ----
      .addCase(clearCart.fulfilled, guardarCarrito)
      .addCase(clearCart.rejected, (state, action) => {
        state.error = action.error.message
      })

      // ---- POST (pagar) ----
      .addCase(pagarCarrito.fulfilled, (state, action) => {
        state.ultimoPedido = action.payload // guardamos el pedido para el cartel
        state.items = [] // el carrito quedó vacío
        state.total = 0
      })
      .addCase(pagarCarrito.rejected, (state, action) => {
        state.error = action.error.message
      })
  },
})

export const { limpiarMensajes, resetCart } = cartSlice.actions

export default cartSlice.reducer
