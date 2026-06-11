import { createStore, combineReducers } from 'redux'
import favoritesReducer from './slices/favoritesSlice'
import cartReducer from './slices/cartSlice'

const rootReducer = combineReducers({
  favorites: favoritesReducer,
  cart: cartReducer,
})

const store = createStore(
  rootReducer,
  window.__REDUX_DEVTOOLS_EXTENSION__ && window.__REDUX_DEVTOOLS_EXTENSION__(),
)

export default store
