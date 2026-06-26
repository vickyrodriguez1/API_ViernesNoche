import React, { useState, useEffect } from 'react'
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import { useDispatch } from 'react-redux'
import LoginForm from './components/LoginForm'
import RegisterForm from './components/RegisterForm'
import CrearProducto from './components/CrearProducto'
import ProtectedRoute from './components/ProtectedRoute'
import AdminRouter from './components/AdminRouter'
import AppLayout from './components/AppLayout'
import Snackbar from './components/Snackbar/Snackbar'
import { clearFavorites } from './store/slices/favoritesSlice'
import { resetCart } from './store/slices/cartSlice'
import Home from './pages/Home'
import ProductDetail from './pages/ProductDetail'
import Checkout from './pages/Checkout'
import Favorites from './pages/Favorites/Favorites'
import './App.css'

function App() {
  const dispatch = useDispatch()
  // Estado de autenticacion de la app (useState). Lo respaldamos en localStorage
  // para que sobreviva a recargas de pagina (persistencia simple del login).
  const [isLoggedIn, setIsLoggedIn] = useState(false)
  const [userRol, setUserRol] = useState(null)
  // authReady = "¿ya termine de revisar si habia sesion guardada?". Arranca en false.
  // Leer el localStorage es instantaneo pero ocurre DESPUES del primer render; sin
  // esta bandera, por un instante isLoggedIn seria false y te mandaria al login aunque
  // tuvieras sesion (parpadeo). Mostramos "Cargando..." hasta que authReady sea true.
  const [authReady, setAuthReady] = useState(false)

  // Al montar la app: si hay token guardado, restauramos la sesion. Luego authReady=true.
  useEffect(() => {
    const token = localStorage.getItem('token')
    const rol = localStorage.getItem('userRol')
    if (token) {
      setIsLoggedIn(true)
      setUserRol(rol)
    }
    setAuthReady(true)
  }, [])

  const handleLoginSuccess = (rol) => {
    setIsLoggedIn(true)
    setUserRol(rol)
  }

  const handleLogout = () => {
    localStorage.removeItem('token')
    localStorage.removeItem('userRol')
    localStorage.removeItem('userId')
    // Limpiamos el estado global del usuario anterior: favoritos (localStorage)
    // y carrito (estado en memoria), para que NO queden pegados al loguearse otro.
    dispatch(clearFavorites())
    dispatch(resetCart())
    setIsLoggedIn(false)
    setUserRol(null)
  }

  return (
    <BrowserRouter>
      <Routes>
        {/* Paginas de autenticacion (publicas, sin navbar).
            Si el usuario YA esta logueado, lo mandamos a la home. */}
        <Route
          path="/login"
          element={isLoggedIn ? <Navigate to="/" replace /> : <LoginForm onLoginSuccess={handleLoginSuccess} />}
        />
        <Route
          path="/register"
          element={isLoggedIn ? <Navigate to="/" replace /> : <RegisterForm />}
        />

        {/* TODO el sitio requiere estar logueado (consigna: la home se ve
            recien una vez autenticado). ProtectedRoute redirige al login si no hay sesion. */}
        <Route element={<ProtectedRoute isLoggedIn={isLoggedIn} authReady={authReady} />}>
          <Route element={<AppLayout isLoggedIn={isLoggedIn} userRol={userRol} onLogout={handleLogout} />}>
            {/* La home ES el catalogo (productos + categorias) */}
            <Route path="/" element={<Home />} />
            <Route path="/products/:id" element={<ProductDetail isLoggedIn={isLoggedIn} userRol={userRol} />} />
            <Route path="/favorites" element={<Favorites />} />
            <Route path="/checkout" element={<Checkout />} />

            {/* Solo ADMIN */}
            <Route element={<AdminRouter isLoggedIn={isLoggedIn} userRol={userRol} authReady={authReady} />}>
              <Route path="/create-product" element={<CrearProducto />} />
            </Route>
          </Route>
        </Route>

        {/* Cualquier ruta desconocida: a la home si hay sesion, sino al login */}
        <Route
          path="*"
          element={
            !authReady ? (
              <p style={{ textAlign: 'center', marginTop: '40px' }}>Cargando...</p>
            ) : (
              <Navigate to={isLoggedIn ? '/' : '/login'} replace />
            )
          }
        />
      </Routes>

      {/* Snackbar global: vive fuera de las rutas para sobrevivir a la navegacion */}
      <Snackbar />
    </BrowserRouter>
  )
}

export default App
