import { Navigate, Outlet } from 'react-router-dom'

// Ruta protegida SOLO para administradores (rutas anidadas via <Outlet />).
// - Mientras leemos el localStorage mostramos "Cargando..." para no parpadear.
// - Si no hay sesion -> al login.
// - Si hay sesion pero el rol no es ADMIN -> a la home.
// isLoggedIn, userRol y authReady son PROPS que vienen de App.jsx.
export default function AdminRouter({ isLoggedIn, userRol, authReady }) {
  if (!authReady) {
    return <p style={{ textAlign: 'center', marginTop: '40px' }}>Cargando...</p>
  }
  if (!isLoggedIn) {
    return <Navigate to="/login" replace />
  }
  if (userRol !== 'ADMIN') {
    return <Navigate to="/" replace />
  }
  return <Outlet />
}
