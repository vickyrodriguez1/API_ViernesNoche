import React, { createContext, useState } from 'react'

export const FavoriteContext = createContext()

export function FavoriteProvider({ children }) {
  const [favoriteItems, setFavoriteItems] = useState([])

  const addToFavorite = (product) => {
    setFavoriteItems((prev) =>
      prev.some((p) => p.id === product.id)
        ? prev.filter((p) => p.id !== product.id)
        : [...prev, product]
    )
  }

  return (
    <FavoriteContext.Provider value={{ favoriteItems, addToFavorite }}>
      {children}
    </FavoriteContext.Provider>
  )
}
