import { configureStore } from '@reduxjs/toolkit'
import cartReducer from './cartSlice.js'
import favoritesReducer from './favoritesSlice.js'

const store = configureStore({
  reducer: {
    cart: cartReducer,
    favorites: favoritesReducer,
  },
})

store.subscribe(() => {
  const state = store.getState()

  localStorage.setItem('cartItems', JSON.stringify(state.cart.items))
  localStorage.setItem('favoriteItems', JSON.stringify(state.favorites.items))
})

export default store