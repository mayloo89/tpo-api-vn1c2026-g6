import { createSlice } from '@reduxjs/toolkit'
import { getProductId, normalizeProduct } from './productHelpers.js'

const storageKey = 'cartItems'

const loadCartItems = () => {
  try {
    const storedItems = JSON.parse(localStorage.getItem(storageKey) || '[]')
    return Array.isArray(storedItems) ? storedItems : []
  } catch {
    return []
  }
}

const initialState = {
  items: loadCartItems(),
}

const cartSlice = createSlice({
  name: 'cart',
  initialState,
  reducers: {
    addToCart(state, action) {
      const { product, quantity = 1 } = action.payload
      const normalizedProduct = { ...normalizeProduct(product), quantity }
      const existingIndex = state.items.findIndex(
        item => getProductId(item) === normalizedProduct.id,
      )

      if (existingIndex >= 0) {
        state.items[existingIndex].quantity += quantity
        return
      }

      state.items.push(normalizedProduct)
    },
    removeFromCart(state, action) {
      const productId = action.payload
      state.items = state.items.filter(item => getProductId(item) !== productId)
    },
    updateQuantity(state, action) {
      const { productId, quantity } = action.payload

      if (quantity < 1) {
        state.items = state.items.filter(item => getProductId(item) !== productId)
        return
      }

      state.items = state.items.map(item =>
        getProductId(item) === productId ? { ...item, quantity } : item,
      )
    },
    clearCart(state) {
      state.items = []
    },
  },
})

export const { addToCart, removeFromCart, updateQuantity, clearCart } = cartSlice.actions
export default cartSlice.reducer

export const selectCartItems = state => state.cart.items
export const selectCartCount = state =>
  state.cart.items.reduce((count, item) => count + item.quantity, 0)
export const selectCartTotal = state =>
  state.cart.items.reduce((total, item) => total + Number(item.precio) * item.quantity, 0)
export const selectIsInCart = (state, productId) =>
  state.cart.items.some(item => getProductId(item) === productId)