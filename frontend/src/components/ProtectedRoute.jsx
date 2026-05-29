import { Navigate, Outlet } from 'react-router-dom'

export default function ProtectedRoute({ isLoggedIn, authReady }) {
  if (!authReady) {
    return <p style={{ textAlign: 'center', marginTop: '40px' }}>Cargando...</p>
  }
  if (!isLoggedIn) {
    return <Navigate to="/login" replace />
  }
  return <Outlet />
}
