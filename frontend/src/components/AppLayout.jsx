import { useEffect } from 'react'
import { Outlet } from 'react-router-dom'
import { useDispatch } from 'react-redux'
import { fetchCart } from '../store/slices/cartSlice'
import Header from './Header'

// Layout comun: pinta el Header arriba y debajo el contenido de cada ruta (Outlet).
export default function AppLayout({ isLoggedIn, userRol, onLogout }) {
  const dispatch = useDispatch()

  // Si hay un usuario logueado, traemos su carrito desde la API al montar
  // (y cuando cambia el login) para que el contador del navbar quede sincronizado.
  useEffect(() => {
    if (isLoggedIn) {
      dispatch(fetchCart())
    }
  }, [isLoggedIn, dispatch])

  return (
    <div>
      <Header isLoggedIn={isLoggedIn} userRol={userRol} onLogout={onLogout} />
      <Outlet />
    </div>
  )
}
