import { Routes, Route } from 'react-router-dom'

export default function AdminRouter() {
  return (
    <Routes>
      <Route index element={<h1>Admin Panel</h1>} />
      <Route path="usuarios" element={<h1>Usuarios</h1>} />
      <Route path="usuarios/:id" element={<h1>Usuario ID</h1>} />
    </Routes>
  )
}
