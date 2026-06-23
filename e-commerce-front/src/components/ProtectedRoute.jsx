import { Navigate, Outlet } from 'react-router-dom'
import { useSelector } from 'react-redux'
import { selectIsLoggedIn, selectAuthStatus } from '../store/authSlice.js'

const ProtectedRoute = () => {
    const isLoggedIn = useSelector(selectIsLoggedIn)
    const authStatus = useSelector(selectAuthStatus)

    if (authStatus === 'idle' || authStatus === 'loading') {
        return <div>Cargando...</div>
    }

    if (!isLoggedIn) {
        return <Navigate to="/auth" replace />
    }

    return <Outlet />
}

export default ProtectedRoute