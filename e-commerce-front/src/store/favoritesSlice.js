import { createAsyncThunk, createSlice } from '@reduxjs/toolkit'
import { getProductId, normalizeProduct } from './productHelpers.js'
import { apiRequest } from '../services/apiClient.js'

const initialState = {
  items: [],
  status: 'idle',
  error: null,
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
      state.status = 'idle'
      state.error = null
    },
  },
  extraReducers: builder => {
    const pending = state => {
      state.status = 'loading'
      state.error = null
    }
    const rejected = (state, action) => {
      state.status = 'failed'
      state.error = action.error.message
    }
    const fulfilled = (state, action) => {
      state.status = 'succeeded'
      state.items = action.payload
    }

    builder
      .addCase(loadFavorites.pending, pending)
      .addCase(loadFavorites.fulfilled, fulfilled)
      .addCase(loadFavorites.rejected, rejected)
      .addCase(addToFavorite.pending, pending)
      .addCase(addToFavorite.fulfilled, fulfilled)
      .addCase(addToFavorite.rejected, rejected)
      .addCase(removeFromFavorite.pending, pending)
      .addCase(removeFromFavorite.fulfilled, fulfilled)
      .addCase(removeFromFavorite.rejected, rejected)
  },
})

export const { resetFavorites } = favoritesSlice.actions
export default favoritesSlice.reducer

export const selectFavoriteItems = state => state.favorites.items
export const selectFavoritesStatus = state => state.favorites.status
export const selectFavoritesError = state => state.favorites.error
export const selectIsFavorite = (state, productId) =>
  state.favorites.items.some(item => getProductId(item) === productId)
