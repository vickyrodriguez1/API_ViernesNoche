import { Outlet } from 'react-router-dom'
import Header from './Header'

export default function AppLayout({ userRol, onLogout }) {
  return (
    <div>
      <Header userRol={userRol} onLogout={onLogout} />
      <Outlet />
    </div>
  )
}
