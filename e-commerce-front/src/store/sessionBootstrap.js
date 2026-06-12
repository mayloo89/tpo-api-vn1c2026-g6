import { useEffect } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import { loadCart } from './cartSlice.js'
import { loadFavorites } from './favoritesSlice.js'
import { selectIsLoggedIn } from './authSlice.js'

const SessionBootstrap = () => {
  const dispatch = useDispatch()
  const isLoggedIn = useSelector(selectIsLoggedIn)

  useEffect(() => {
    if (isLoggedIn) {
      dispatch(loadCart())
      dispatch(loadFavorites())
    }
  }, [dispatch, isLoggedIn])

  return null
}

export default SessionBootstrap