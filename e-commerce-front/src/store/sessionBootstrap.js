import { useEffect } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import { loadCart } from './cartSlice.js'
import { loadFavorites } from './favoritesSlice.js'
import { checkAuthThunk, selectIsLoggedIn, selectAuthStatus } from './authSlice.js'

const SessionBootstrap = () => {
  const dispatch = useDispatch()
  const isLoggedIn = useSelector(selectIsLoggedIn)
  const authStatus = useSelector(selectAuthStatus)

  useEffect(() => {
    dispatch(checkAuthThunk())
  }, [dispatch])

  useEffect(() => {
    if (isLoggedIn) {
      dispatch(loadCart())
      dispatch(loadFavorites())
    }
  }, [dispatch, isLoggedIn])

  return null
}

export default SessionBootstrap