import { createContext, useContext, useEffect, useState } from 'react'

const FavoriteContext = createContext(null)
const storageKey = 'favoriteItems'

const getProductId = product => product?.id ?? product?._id ?? product?.codigo ?? product?.nombre

export function FavoriteProvider({ children }) {
    const [favoriteItems, setFavoriteItems] = useState(() => {
        try {
            const storedItems = JSON.parse(localStorage.getItem(storageKey) || '[]')
            return Array.isArray(storedItems) ? storedItems : []
        } catch {
            return []
        }
    })

    useEffect(() => {
        localStorage.setItem(storageKey, JSON.stringify(favoriteItems))
    }, [favoriteItems])

    const addToFavorite = product => {
        const normalizedProduct = { ...product, id: getProductId(product) }

        setFavoriteItems(previousItems => {
            const alreadyFavorite = previousItems.some(
                item => getProductId(item) === normalizedProduct.id,
            )

            if (alreadyFavorite) {
                return previousItems
            }

            return [...previousItems, normalizedProduct]
        })
    }

    const removeFromFavorite = productId => {
        setFavoriteItems(previousItems =>
            previousItems.filter(item => getProductId(item) !== productId),
        )
    }

    const isFavorite = productId =>
        favoriteItems.some(item => getProductId(item) === productId)

    return (
        <FavoriteContext.Provider
            value={{
                favoriteItems,
                addToFavorite,
                removeFromFavorite,
                isFavorite,
            }}
        >
            {children}
        </FavoriteContext.Provider>
    )
}

export const useFavorites = () => {
    const context = useContext(FavoriteContext)

    if (!context) {
        throw new Error('useFavorites must be used within a FavoriteProvider')
    }

    return context
}