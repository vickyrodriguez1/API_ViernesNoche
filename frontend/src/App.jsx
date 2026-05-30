import React, { useState, useEffect } from 'react'
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import LoginForm from './components/LoginForm'
import ProductList from './components/ProductList'
import CrearProducto from './components/CrearProducto'
import ProtectedRoute from './components/ProtectedRoute'
import AdminRoute from './components/AdminRoute'
import AppLayout from './components/AppLayout'
import Home from './pages/Home'
import ProductDetail from './pages/ProductDetail'
import Checkout from './pages/Checkout'
import Favorite from './pages/Favorite'
import { FavoriteProvider } from './context/FavoriteContext'
import './App.css'

function App() {
  const [isLoggedIn, setIsLoggedIn] = useState(false)
  const [userRol, setUserRol] = useState(null)
  const [authReady, setAuthReady] = useState(false)

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
    setIsLoggedIn(false)
    setUserRol(null)
  }

  return (
    <FavoriteProvider>
    <BrowserRouter>
      <Routes>
        <Route
          path="/login"
          element={
            !authReady ? (
              <p style={{ textAlign: 'center', marginTop: '40px' }}>Cargando...</p>
            ) : isLoggedIn ? (
              <Navigate to="/" replace />
            ) : (
              <LoginForm onLoginSuccess={handleLoginSuccess} />
            )
          }
        />

        <Route element={<ProtectedRoute isLoggedIn={isLoggedIn} authReady={authReady} />}>
          <Route element={<AppLayout userRol={userRol} onLogout={handleLogout} />}>
            <Route path="/" element={<Home userRol={userRol} />} />
            <Route path="/products" element={<ProductList />} />
            <Route path="/products/:id" element={<ProductDetail />} />
            <Route path="/checkout" element={<Checkout />} />
            <Route path="/favorites" element={<Favorite />} />
            <Route
              path="/create-product"
              element={
                <AdminRoute>
                  <CrearProducto />
                </AdminRoute>
              }
            />
          </Route>
        </Route>

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
    </BrowserRouter>
    </FavoriteProvider>
  )
}

export default App
