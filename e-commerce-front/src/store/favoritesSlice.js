import { createAsyncThunk, createSlice } from '@reduxjs/toolkit'
import { getProductId, normalizeProduct } from './productHelpers.js'
import { apiRequest } from '../services/apiClient.js'

const initialState = {
  items: [],
}

export const loadFavorites = createAsyncThunk('favorites/loadFavorites', async () => {
  const response = await apiRequest('/api/favoritos')
  return Array.isArray(response) ? response.map(normalizeProduct) : []
})

export const addToFavorite = createAsyncThunk('favorites/addToFavorite', async product => {
  const response = await apiRequest(`/api/favoritos/${getProductId(product)}`, {
    method: 'POST',
  })

  return Array.isArray(response) ? response.map(normalizeProduct) : []
})

export const removeFromFavorite = createAsyncThunk('favorites/removeFromFavorite', async productId => {
  const response = await apiRequest(`/api/favoritos/${productId}`, {
    method: 'DELETE',
  })

  return Array.isArray(response) ? response.map(normalizeProduct) : []
})

const favoritesSlice = createSlice({
  name: 'favorites',
  initialState,
  reducers: {
    resetFavorites(state) {
      state.items = []
    },
  },
  extraReducers: builder => {
    builder
      .addCase(loadFavorites.fulfilled, (state, action) => {
        state.items = action.payload
      })
      .addCase(addToFavorite.fulfilled, (state, action) => {
        state.items = action.payload
      })
      .addCase(removeFromFavorite.fulfilled, (state, action) => {
        state.items = action.payload
      })
  },
})

export const { resetFavorites } = favoritesSlice.actions
export default favoritesSlice.reducer

export const selectFavoriteItems = state => state.favorites.items
export const selectIsFavorite = (state, productId) =>
  state.favorites.items.some(item => getProductId(item) === productId)