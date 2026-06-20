import { Routes, Route } from 'react-router-dom'
import CrearProducto from './CrearProducto'

export default function AdminRouter() {
  return (
    <Routes>
      <Route index element={<CrearProducto />} />
      <Route path="usuarios" element={<h1>Usuarios</h1>} />
      <Route path="usuarios/:id" element={<h1>Usuario ID</h1>} />
    </Routes>
  )
}
