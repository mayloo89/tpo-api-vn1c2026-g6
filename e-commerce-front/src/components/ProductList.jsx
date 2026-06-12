import React, { useEffect, useState } from 'react'
import CardProductos from './CardProductos.jsx'
import { apiRequest } from '../services/apiClient.js'
import './ProductList.css'

const ProductList = () => {
const [products, setProducts] = useState([])
const [loading, setLoading] = useState(true)
const [error, setError] = useState(null)

useEffect(() => {
const fetchProducts = async () => {
try {
const data = await apiRequest('/api/productos');
setProducts(data);
} catch (err) {
setError(err.message);
} finally {
setLoading(false);
}
};

fetchProducts();
}, []);

if (loading) return <div className="product-list-container"><p>Cargando productos...</p></div>;
if (error) return <div className="product-list-container"><p>Error: {error}</p></div>;

const items = Array.isArray(products)
? products
: products?.productos || products?.data || products?.items || []

return (
<div className="product-list-container">
<h1 className="product-list-title">Lista de Productos</h1>
<div className="products-grid">
{items.length === 0 && <div>No hay productos.</div>}
{items.map(product => {
const id = product.id ?? product._id ?? product.codigo
return (
<CardProductos key={id} product={{ ...product, id }} />
)
})}
</div>
</div>
)
}

export default ProductList
