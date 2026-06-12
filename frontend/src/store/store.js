// configureStore es la forma de Redux Toolkit de crear el store global.
// Ventaja sobre createStore: ya incluye el middleware "thunk" (necesario para
// las llamadas asincrónicas a la API con createAsyncThunk) y las Redux DevTools.
import { configureStore } from '@reduxjs/toolkit'
import favoritesReducer from './slices/favoritesSlice'
import cartReducer from './slices/cartSlice'

// Cada clave es un "pedazo" del estado global de la app.
const store = configureStore({
  reducer: {
    favorites: favoritesReducer, // estado de favoritos (ya existía, sin cambios)
    cart: cartReducer, // estado del carrito (nuevo, con llamadas a la API)
  },
})

export default store
