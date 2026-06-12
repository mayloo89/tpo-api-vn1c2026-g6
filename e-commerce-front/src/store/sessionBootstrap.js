import { useEffect } from 'react'
import { useDispatch } from 'react-redux'
import { loadCart } from './cartSlice.js'
import { loadFavorites } from './favoritesSlice.js'
import { getStoredUser } from '../services/apiClient.js'

const SessionBootstrap = () => {
  const dispatch = useDispatch()

  useEffect(() => {
    const user = getStoredUser()

    if (user?.token) {
      dispatch(loadCart())
      dispatch(loadFavorites())
    }
  }, [dispatch])

  return null
}

export default SessionBootstrap