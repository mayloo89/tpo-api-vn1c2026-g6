import { configureStore } from '@reduxjs/toolkit'
import cartReducer from './cartSlice.js'
import favoritesReducer from './favoritesSlice.js'
import authReducer from './authSlice.js'

const store = configureStore({
  reducer: {
    auth: authReducer,
    cart: cartReducer,
    favorites: favoritesReducer,
  },
})

export default store