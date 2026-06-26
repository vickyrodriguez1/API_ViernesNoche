// =============================================================================
// uiSlice.js - Estado de la interfaz (por ahora, el snackbar global)
// =============================================================================
// Guardamos en el store global si hay un snackbar abierto, su mensaje y su tipo.
//
// ¿Por qué en Redux y no con useState local? Porque el login, después de mostrar
// el cartel, navega a otra ruta: si fuera estado local del formulario, el componente
// se desmontaría y el snackbar nunca se vería. Al estar en el store global, el
// <Snackbar /> vive en la raíz de la app (fuera de las rutas) y sobrevive al cambio
// de ruta. Cualquier componente puede dispararlo con dispatch(showSnackbar(...)).
//
// sumar el snackbar como
// un slice más mantiene TODO el estado global en un solo sistema (en vez de crear un
// Context aparte solo para esto), reusa el mismo useSelector/dispatch y nos da las
// DevTools para depurar.
// =============================================================================
import { createSlice } from '@reduxjs/toolkit'

const uiSlice = createSlice({
  name: 'ui',
  initialState: {
    snackbar: {
      open: false,
      message: '',
      type: 'success', // 'success' | 'error'
    },
  },
  reducers: {
    // Abre el snackbar con un mensaje. type por defecto 'success'.
    showSnackbar: (state, action) => {
      state.snackbar.open = true
      state.snackbar.message = action.payload.message
      state.snackbar.type = action.payload.type || 'success'
    },
    // Lo cierra (lo dispara solo el temporizador del componente).
    hideSnackbar: (state) => {
      state.snackbar.open = false
    },
  },
})

export const { showSnackbar, hideSnackbar } = uiSlice.actions
export default uiSlice.reducer
