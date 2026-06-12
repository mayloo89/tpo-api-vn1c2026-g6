import { Navigate, Outlet } from 'react-router-dom'
import { useSelector } from 'react-redux'
import { selectIsLoggedIn } from '../store/authSlice.js'

const ProtectedRoute = () => {
    const isLoggedIn = useSelector(selectIsLoggedIn)

    if (!isLoggedIn) {
        return <Navigate to="/auth" replace />
    }

    return <Outlet />
}

export default ProtectedRoute