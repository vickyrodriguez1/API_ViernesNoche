import { useEffect } from 'react'
import { useSelector, useDispatch } from 'react-redux'
import { hideSnackbar } from '../../store/slices/uiSlice'
import styles from './Snackbar.module.css'

// Snackbar global: cartel breve que se cierra solo (no bloquea como un alert).
// Lee su estado del store (state.ui.snackbar) y, mientras está abierto,
// programa un temporizador para cerrarse a los 2,5 segundos.
export default function Snackbar() {
  const dispatch = useDispatch()
  // open/message/type NO son estado local: salen del store global (state.ui.snackbar),
  // que escribe quien dispara showSnackbar (login, registro, ProductDetail, etc.).
  // Por eso este unico componente, montado en la raiz, muestra los avisos de toda la app.
  const { open, message, type } = useSelector((state) => state.ui.snackbar)

  useEffect(() => {
    if (!open) return
    // Al abrirse (o cambiar el mensaje) arrancamos el contador.
    const timer = setTimeout(() => dispatch(hideSnackbar()), 2500)
    // Limpieza del efecto: si se desmonta o cambia, cancelamos el timer.
    return () => clearTimeout(timer)
  }, [open, message, dispatch])

  // Renderizado condicional: si está cerrado, no mostramos nada.
  if (!open) return null

  return (
    <div className={`${styles.snackbar} ${styles[type] || ''}`} role="status">
      <span className={styles.icon}>{type === 'error' ? '⚠️' : '✓'}</span>
      <span className={styles.message}>{message}</span>
    </div>
  )
}
