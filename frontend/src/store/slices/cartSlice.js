const initialState = {
  items: JSON.parse(localStorage.getItem('cartItems') || '[]'),
}

export const ADD_TO_CART = 'cart/addToCart'
export const REMOVE_FROM_CART = 'cart/removeFromCart'
export const INCREMENT_QUANTITY = 'cart/incrementQuantity'
export const DECREMENT_QUANTITY = 'cart/decrementQuantity'
export const CLEAR_CART = 'cart/clearCart'

const saveCart = (items) => {
  localStorage.setItem('cartItems', JSON.stringify(items))
}

export const addToCart = (product) => ({
  type: ADD_TO_CART,
  payload: product,
})

export const removeFromCart = (productId) => ({
  type: REMOVE_FROM_CART,
  payload: productId,
})

export const incrementQuantity = (productId) => ({
  type: INCREMENT_QUANTITY,
  payload: productId,
})

export const decrementQuantity = (productId) => ({
  type: DECREMENT_QUANTITY,
  payload: productId,
})

export const clearCart = () => ({
  type: CLEAR_CART,
})

export default function cartReducer(state = initialState, action) {
  switch (action.type) {
    case ADD_TO_CART: {
      const product = action.payload
      const existingItem = state.items.find((item) => item.id === product.id)
      let items

      if (existingItem) {
        const quantity = existingItem.quantity + 1
        if (product.stock !== undefined && quantity > product.stock) {
          return state
        }
        items = state.items.map((item) =>
          item.id === product.id ? { ...item, quantity } : item,
        )
      } else {
        items = [...state.items, { ...product, quantity: 1 }]
      }

      saveCart(items)
      return { ...state, items }
    }
    case REMOVE_FROM_CART: {
      const items = state.items.filter((item) => item.id !== action.payload)
      saveCart(items)
      return { ...state, items }
    }
    case INCREMENT_QUANTITY: {
      const items = state.items.map((item) => {
        if (item.id !== action.payload) return item
        const quantity = item.quantity + 1
        if (item.stock !== undefined && quantity > item.stock) {
          return item
        }
        return { ...item, quantity }
      })
      saveCart(items)
      return { ...state, items }
    }
    case DECREMENT_QUANTITY: {
      const existingItem = state.items.find((item) => item.id === action.payload)
      if (!existingItem) return state

      let items
      if (existingItem.quantity <= 1) {
        items = state.items.filter((item) => item.id !== action.payload)
      } else {
        items = state.items.map((item) =>
          item.id === action.payload
            ? { ...item, quantity: item.quantity - 1 }
            : item,
        )
      }

      saveCart(items)
      return { ...state, items }
    }
    case CLEAR_CART:
      saveCart([])
      return { ...state, items: [] }
    default:
      return state
  }
}
