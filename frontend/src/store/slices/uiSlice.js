// =============================================================================
// uiSlice.js - Estado de la interfaz (por ahora, el snackbar global)
// =============================================================================
// Guardamos en el store global si hay un snackbar abierto, su mensaje y su tipo.
// Lo manejamos en Redux (y no con useState local) porque el login, después de
// mostrarlo, navega a otra ruta: si fuera estado local del formulario, el
// componente se desmontaría y el snackbar nunca se vería. Al estar en el store
// global, el <Snackbar /> vive en la raíz de la app y sobrevive al cambio de ruta.
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
