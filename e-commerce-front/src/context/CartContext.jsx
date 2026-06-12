import { createContext, useContext, useEffect, useState } from 'react'

const CartContext = createContext(null)
const storageKey = 'cartItems'

const getProductId = product => product?.id ?? product?._id ?? product?.codigo ?? product?.nombre

export function CartProvider({ children }) {
  const [cartItems, setCartItems] = useState(() => {
    try {
      const storedItems = JSON.parse(localStorage.getItem(storageKey) || '[]')
      return Array.isArray(storedItems) ? storedItems : []
    } catch {
      return []
    }
  })

  useEffect(() => {
    localStorage.setItem(storageKey, JSON.stringify(cartItems))
  }, [cartItems])

  const addToCart = (product, quantity = 1) => {
    const normalizedProduct = { ...product, id: getProductId(product) }

    setCartItems(previousItems => {
      const existingIndex = previousItems.findIndex(
        item => getProductId(item) === normalizedProduct.id,
      )

      if (existingIndex >= 0) {
        const updatedItems = [...previousItems]
        updatedItems[existingIndex] = {
          ...updatedItems[existingIndex],
          quantity: updatedItems[existingIndex].quantity + quantity,
        }
        return updatedItems
      }

      return [...previousItems, { ...normalizedProduct, quantity }]
    })
  }

  const removeFromCart = productId => {
    setCartItems(previousItems =>
      previousItems.filter(item => getProductId(item) !== productId),
    )
  }

  const updateQuantity = (productId, quantity) => {
    if (quantity < 1) {
      removeFromCart(productId)
      return
    }

    setCartItems(previousItems =>
      previousItems.map(item =>
        getProductId(item) === productId ? { ...item, quantity } : item,
      ),
    )
  }

  const clearCart = () => {
    setCartItems([])
  }

  const cartTotal = cartItems.reduce(
    (total, item) => total + Number(item.precio) * item.quantity,
    0,
  )

  const cartCount = cartItems.reduce((count, item) => count + item.quantity, 0)

  const isInCart = productId =>
    cartItems.some(item => getProductId(item) === productId)

  return (
    <CartContext.Provider
      value={{
        cartItems,
        addToCart,
        removeFromCart,
        updateQuantity,
        clearCart,
        cartTotal,
        cartCount,
        isInCart,
      }}
    >
      {children}
    </CartContext.Provider>
  )
}

export const useCart = () => {
  const context = useContext(CartContext)

  if (!context) {
    throw new Error('useCart must be used within a CartProvider')
  }

  return context
}
