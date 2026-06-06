const initialState = {
  items: JSON.parse(localStorage.getItem('favoriteProducts') || '[]'),
}

export const TOGGLE_FAVORITE = 'favorites/toggleFavorite'
export const CLEAR_FAVORITES = 'favorites/clearFavorites'

export const toggleFavorite = (product) => ({
  type: TOGGLE_FAVORITE,
  payload: product,
})

export const clearFavorites = () => ({
  type: CLEAR_FAVORITES,
})

export default function favoritesReducer(state = initialState, action) {
  switch (action.type) {
    case TOGGLE_FAVORITE: {
      const product = action.payload
      const exists = state.items.some((item) => item.id === product.id)
      const items = exists
        ? state.items.filter((item) => item.id !== product.id)
        : [...state.items, product]
      localStorage.setItem('favoriteProducts', JSON.stringify(items))
      return { ...state, items }
    }
    case CLEAR_FAVORITES:
      localStorage.removeItem('favoriteProducts')
      return { ...state, items: [] }
    default:
      return state
  }
}
