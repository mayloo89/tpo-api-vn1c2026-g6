import { configureStore } from '@reduxjs/toolkit'
import cartReducer from './cartSlice.js'
import favoritesReducer from './favoritesSlice.js'

const store = configureStore({
  reducer: {
    cart: cartReducer,
    favorites: favoritesReducer,
  },
})

export default store