import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { BrowserRouter, Routes, Route } from 'react-router-dom'
import './index.css'
import Navbar from './components/Navbar.jsx'
import Home from './components/Home.jsx'
import ProductList from './components/ProductList.jsx'
import ProductDetail from './components/ProductDetail.jsx'
import ProductManagement from './components/ProductManagement.jsx'
import UserManagement from './components/UserManagement.jsx'
import Favorite from './components/Favorite.jsx'
import ProtectedRoute from './components/ProtectedRoute.jsx'
import { FavoriteProvider } from './context/FavoriteContext.jsx'

createRoot(document.getElementById('root')).render(
<StrictMode>
<FavoriteProvider>
<BrowserRouter>
<Navbar />
<Routes>
<Route path="/" element={<Home />} />
<Route path="/products" element={<ProductList />} />
<Route path="/products/:id" element={<ProductDetail />} />
<Route path="/auth" element={<UserManagement />} />
<Route element={<ProtectedRoute />}>
<Route path="/admin/products" element={<ProductManagement />} />
<Route path="/favorites" element={<Favorite />} />
</Route>
</Routes>
</BrowserRouter>
</FavoriteProvider>
</StrictMode>,
)
