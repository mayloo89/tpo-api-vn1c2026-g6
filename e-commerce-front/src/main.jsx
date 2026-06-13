import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { BrowserRouter, Routes, Route } from 'react-router-dom'
import { Provider } from 'react-redux'
import './index.css'
import Navbar from './components/Navbar.jsx'
import Home from './components/Home.jsx'
import ProductList from './components/ProductList.jsx'
import ProductDetail from './components/ProductDetail.jsx'
import ProductManagement from './components/ProductManagement.jsx'
import UserManagement from './components/UserManagement.jsx'
import Favorite from './components/Favorite.jsx'
import ProtectedRoute from './components/ProtectedRoute.jsx'
import Cart from './components/Cart.jsx'
import store from './store/store.js'
import SessionBootstrap from './store/sessionBootstrap.js'
import { ThemeProvider } from './context/ThemeContext.jsx'

createRoot(document.getElementById('root')).render(
    <StrictMode>
        <Provider store={store}>
            <ThemeProvider>
            <BrowserRouter>
                <SessionBootstrap />
                <Navbar />
                <Routes>
                    <Route path="/" element={<Home />} />
                    <Route path="/products" element={<ProductList />} />
                    <Route path="/products/:id" element={<ProductDetail />} />
                    <Route path="/auth" element={<UserManagement />} />
                    <Route path="/cart" element={<Cart />} />
                    <Route element={<ProtectedRoute />}>
                        <Route path="/admin/products" element={<ProductManagement />} />
                        <Route path="/favorites" element={<Favorite />} />
                    </Route>
                </Routes>
            </BrowserRouter>
            </ThemeProvider>
        </Provider>
    </StrictMode>,
)
