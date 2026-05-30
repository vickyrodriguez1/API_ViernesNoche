import { Navigate } from 'react-router-dom'

export default function AdminRoute({ children }) {
  const rol = localStorage.getItem('userRol')
  if (rol !== 'ADMIN') {
    return <Navigate to="/" replace />
  }
  return children
}
