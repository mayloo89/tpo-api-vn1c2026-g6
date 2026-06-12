import { createSlice } from '@reduxjs/toolkit'
import { getProductId, normalizeProduct } from './productHelpers.js'

const storageKey = 'favoriteItems'

const loadFavoriteItems = () => {
  try {
    const storedItems = JSON.parse(localStorage.getItem(storageKey) || '[]')
    return Array.isArray(storedItems) ? storedItems : []
  } catch {
    return []
  }
}

const initialState = {
  items: loadFavoriteItems(),
}

const favoritesSlice = createSlice({
  name: 'favorites',
  initialState,
  reducers: {
    addToFavorite(state, action) {
      const normalizedProduct = normalizeProduct(action.payload)
      const alreadyFavorite = state.items.some(
        item => getProductId(item) === normalizedProduct.id,
      )

      if (!alreadyFavorite) {
        state.items.push(normalizedProduct)
      }
    },
    removeFromFavorite(state, action) {
      const productId = action.payload
      state.items = state.items.filter(item => getProductId(item) !== productId)
    },
  },
})

export const { addToFavorite, removeFromFavorite } = favoritesSlice.actions
export default favoritesSlice.reducer

export const selectFavoriteItems = state => state.favorites.items
export const selectIsFavorite = (state, productId) =>
  state.favorites.items.some(item => getProductId(item) === productId)