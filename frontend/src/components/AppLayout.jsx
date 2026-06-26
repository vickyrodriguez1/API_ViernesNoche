import { useEffect } from 'react'
import { Outlet } from 'react-router-dom'
import { useDispatch } from 'react-redux'
import { fetchCart } from '../store/slices/cartSlice'
import Header from './Header'

// AppLayout es un LAYOUT: la "plantilla" comun de las pantallas logueadas. Pinta el
// Header (la barra de navegacion) arriba, y debajo el contenido de la ruta actual.
//
// isLoggedIn, userRol y onLogout son PROPS que recibe de App.jsx (el padre). AppLayout
// las usa y/o las reenvia al Header (a esto, cuando un dato pasa por varios niveles,
// se le dice "pasar props para abajo").
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
      {/* <Outlet /> es el "hueco" de React Router: aca se inserta la pantalla de la
          ruta hija actual (Home, Checkout, Favorites, etc.). Asi el Header queda
          fijo arriba y abajo cambia solo el contenido segun la URL. */}
      <Outlet />
    </div>
  )
}
