import { createAsyncThunk, createSlice } from '@reduxjs/toolkit'
import { getProductId } from './productHelpers.js'
import { apiRequest } from '../services/apiClient.js'

const normalizeCartItem = item => ({
  id: item.productoId ?? item.id,
  nombre: item.nombreProducto ?? item.nombre,
  descripcion: item.descripcion,
  precio: item.precioUnitario ?? item.precio,
  stock: item.stock,
  quantity: item.cantidad ?? item.quantity ?? 1,
  imagen: item.imagen ?? null,
})

const normalizeCartResponse = response => {
  if (!response) {
    return []
  }

  const items = Array.isArray(response) ? response : response.items
  return Array.isArray(items) ? items.map(normalizeCartItem) : []
}

const initialState = {
  items: [],
  status: 'idle',
  error: null,
}

export const loadCart = createAsyncThunk('cart/loadCart', async () => {
  const response = await apiRequest('/api/carrito')
  return normalizeCartResponse(response)
})

export const addToCart = createAsyncThunk('cart/addToCart', async ({ product, quantity = 1 }) => {
  const response = await apiRequest('/api/carrito/items', {
    method: 'POST',
    body: JSON.stringify({ productoId: getProductId(product), cantidad: quantity }),
  })

  return normalizeCartResponse(response)
})

export const removeFromCart = createAsyncThunk('cart/removeFromCart', async productId => {
  const response = await apiRequest(`/api/carrito/items/${productId}`, {
    method: 'DELETE',
  })

  return normalizeCartResponse(response)
})

export const updateQuantity = createAsyncThunk(
  'cart/updateQuantity',
  async ({ productId, quantity }) => {
    const response = await apiRequest(`/api/carrito/items/${productId}?cantidad=${quantity}`, {
      method: 'PUT',
    })

    return normalizeCartResponse(response)
  },
)

export const clearCart = createAsyncThunk('cart/clearCart', async () => {
  await apiRequest('/api/carrito/clear', {
    method: 'DELETE',
  })

  return []
})

const cartSlice = createSlice({
  name: 'cart',
  initialState,
  reducers: {
    resetCart(state) {
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
      .addCase(loadCart.pending, pending)
      .addCase(loadCart.fulfilled, fulfilled)
      .addCase(loadCart.rejected, rejected)
      .addCase(addToCart.pending, pending)
      .addCase(addToCart.fulfilled, fulfilled)
      .addCase(addToCart.rejected, rejected)
      .addCase(removeFromCart.pending, pending)
      .addCase(removeFromCart.fulfilled, fulfilled)
      .addCase(removeFromCart.rejected, rejected)
      .addCase(updateQuantity.pending, pending)
      .addCase(updateQuantity.fulfilled, fulfilled)
      .addCase(updateQuantity.rejected, rejected)
      .addCase(clearCart.pending, pending)
      .addCase(clearCart.fulfilled, state => {
        state.status = 'succeeded'
        state.items = []
      })
      .addCase(clearCart.rejected, rejected)
  },
})

export const { resetCart } = cartSlice.actions
export default cartSlice.reducer

export const selectCartItems = state => state.cart.items
export const selectCartStatus = state => state.cart.status
export const selectCartError = state => state.cart.error
export const selectCartCount = state =>
  state.cart.items.reduce((count, item) => count + item.quantity, 0)
export const selectCartTotal = state =>
  state.cart.items.reduce((total, item) => total + Number(item.precio) * item.quantity, 0)
export const selectIsInCart = (state, productId) =>
  state.cart.items.some(item => getProductId(item) === productId)
