import { Navigate, Outlet } from 'react-router-dom'

const ProtectedRoute = () => {
    const user = JSON.parse(localStorage.getItem('user') || 'null')

    if (!user) {
        return <Navigate to="/auth" replace />
    }

    return <Outlet />
}

export default ProtectedRoute