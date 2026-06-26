import { Navigate, Outlet } from 'react-router-dom'

// isLoggedIn y authReady son PROPS que vienen de App.jsx (donde vive el estado de sesion).
export default function ProtectedRoute({ isLoggedIn, authReady }) {
  if (!authReady) {
    return <p style={{ textAlign: 'center', marginTop: '40px' }}>Cargando...</p>
  }
  if (!isLoggedIn) {
    return <Navigate to="/login" replace />
  }
  return <Outlet />
}
