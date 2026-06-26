import React, { useEffect, useState } from 'react'
import ProductList from '../../components/ProductList'
import styles from './Home.module.css'

// Home del sitio (lo primero que se ve, como cualquier e-commerce).
// Muestra dos secciones:
//   1) el listado de categorias (las trae de la API)
//   2) el listado de productos ordenados alfabeticamente (componente ProductList)
// Al tocar una categoria, se filtra el listado de productos (estado selectedCategoria).
export default function Home() {
  const [categorias, setCategorias] = useState([])
  // "Lifting state up": la categoria elegida vive ACA (el padre) y se la pasamos por
  // props a ProductList (el hijo que filtra). Asi el estado compartido esta arriba.
  const [selectedCategoria, setSelectedCategoria] = useState('') // '' = mostrar todas

  useEffect(() => {
    const fetchCategorias = async () => {
      try {
        const res = await fetch('http://localhost:8080/api/categorias')
        if (!res.ok) throw new Error('No se pudieron cargar las categorías')
        const data = await res.json()
        setCategorias(data)
      } catch {
        setCategorias([]) // si falla, simplemente no mostramos chips de categorias
      }
    }
    fetchCategorias()
  }, [])

  return (
    <div className={styles.page}>
      <header className={styles.hero}>     
        <h1 className={styles.title}>
          Bienvenido a <span className={styles.highlight}>Defiant</span>
        </h1>
        <p className={styles.subtitle}>
          Explorá los productos por categoría y agregá lo que quieras a tu carrito.
        </p>
      </header>

      <section className={styles.categoriesSection}>
        <h2 className={styles.sectionTitle}>Categorías</h2>
        <div className={styles.chips}>
          {/* Chip "Todas" para limpiar el filtro */}
          <button
            type="button"
            className={`${styles.chip} ${selectedCategoria === '' ? styles.chipActive : ''}`}
            onClick={() => setSelectedCategoria('')}
          >
            Todas
          </button>

          {categorias.map((cat) => (
            <button
              key={cat.id}
              type="button"
              className={`${styles.chip} ${selectedCategoria === cat.nombre ? styles.chipActive : ''}`}
              onClick={() => setSelectedCategoria(cat.nombre)}
            >
              {cat.nombre}
            </button>
          ))}
        </div>
      </section>

      {/* Renderizamos el listado de productos pasandole la categoria elegida para
          filtrar. ProductList es un componente aparte (responsabilidad unica: listar),
          hoy se usa solo aca. El que SI se reutiliza es CardProductos (en este listado
          y en la pagina de Favoritos). */}
      <ProductList categoria={selectedCategoria} />
    </div>
  )
}
