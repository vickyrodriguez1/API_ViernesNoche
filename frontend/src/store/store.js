import { createStore, combineReducers } from 'redux'
import favoritesReducer from './slices/favoritesSlice'

const rootReducer = combineReducers({
  favorites: favoritesReducer,
})

const store = createStore(
  rootReducer,
  window.__REDUX_DEVTOOLS_EXTENSION__ && window.__REDUX_DEVTOOLS_EXTENSION__(),
)

export default store
