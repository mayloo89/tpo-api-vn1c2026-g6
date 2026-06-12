import { createAsyncThunk, createSlice } from '@reduxjs/toolkit'
import { getProductId } from './productHelpers.js'
import { apiRequest, getStoredUser } from '../services/apiClient.js'

const normalizeCartItem = item => ({
  id: item.productoId ?? item.id,
  nombre: item.nombreProducto ?? item.nombre,
  descripcion: item.descripcion,
  precio: item.precioUnitario ?? item.precio,
  stock: item.stock,
  quantity: item.cantidad ?? item.quantity ?? 1,
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
}

export const loadCart = createAsyncThunk('cart/loadCart', async () => {
  const user = getStoredUser()

  if (!user?.token) {
    return []
  }

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
    },
  },
  extraReducers: builder => {
    builder
      .addCase(loadCart.fulfilled, (state, action) => {
        state.items = action.payload
      })
      .addCase(addToCart.fulfilled, (state, action) => {
        state.items = action.payload
      })
      .addCase(removeFromCart.fulfilled, (state, action) => {
        state.items = action.payload
      })
      .addCase(updateQuantity.fulfilled, (state, action) => {
        state.items = action.payload
      })
      .addCase(clearCart.fulfilled, state => {
        state.items = []
      })
  },
})

export const { resetCart } = cartSlice.actions
export default cartSlice.reducer

export const selectCartItems = state => state.cart.items
export const selectCartCount = state =>
  state.cart.items.reduce((count, item) => count + item.quantity, 0)
export const selectCartTotal = state =>
  state.cart.items.reduce((total, item) => total + Number(item.precio) * item.quantity, 0)
export const selectIsInCart = (state, productId) =>
  state.cart.items.some(item => getProductId(item) === productId)