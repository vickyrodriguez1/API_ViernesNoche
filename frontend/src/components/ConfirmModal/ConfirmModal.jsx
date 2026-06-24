import { useEffect } from 'react'
import styles from './ConfirmModal.module.css'

// Modal de confirmacion reutilizable con el look & feel de la app.
// Reemplaza al window.confirm() nativo para que las confirmaciones queden
// estandarizadas con el resto de la UI (card oscura, acento naranja, etc.).
//
// Props:
//   open        -> si se muestra o no (renderizado condicional)
//   title       -> titulo del modal
//   message     -> texto/explicacion de la accion
//   confirmText -> texto del boton de confirmar (por defecto "Confirmar")
//   cancelText  -> texto del boton de cancelar (por defecto "Cancelar")
//   variant     -> 'danger' (rojo, para eliminar) o 'primary' (naranja)
//   onConfirm   -> callback al confirmar
//   onCancel    -> callback al cancelar (click afuera, Escape o boton)
export default function ConfirmModal({
  open,
  title = '¿Estás seguro?',
  message,
  confirmText = 'Confirmar',
  cancelText = 'Cancelar',
  variant = 'danger',
  onConfirm,
  onCancel,
}) {
  // Cerrar con la tecla Escape mientras el modal esta abierto.
  useEffect(() => {
    if (!open) return
    const onKeyDown = (e) => {
      if (e.key === 'Escape') onCancel?.()
    }
    document.addEventListener('keydown', onKeyDown)
    return () => document.removeEventListener('keydown', onKeyDown)
  }, [open, onCancel])

  if (!open) return null

  return (
    // Fondo oscuro: un click afuera de la card cancela.
    <div className={styles.overlay} onClick={onCancel} role="presentation">
      <div
        className={styles.dialog}
        role="dialog"
        aria-modal="true"
        aria-labelledby="confirm-modal-title"
        // Frenamos la propagacion para que un click DENTRO de la card no cierre.
        onClick={(e) => e.stopPropagation()}
      >
        <h3 id="confirm-modal-title" className={styles.title}>
          {title}
        </h3>
        {message && <p className={styles.message}>{message}</p>}

        <div className={styles.actions}>
          <button type="button" className={styles.cancelButton} onClick={onCancel}>
            {cancelText}
          </button>
          <button
            type="button"
            className={`${styles.confirmButton} ${styles[variant] || ''}`}
            onClick={onConfirm}
            autoFocus
          >
            {confirmText}
          </button>
        </div>
      </div>
    </div>
  )
}
