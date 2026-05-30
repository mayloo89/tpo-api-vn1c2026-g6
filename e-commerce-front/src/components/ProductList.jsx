import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import CardProductos from './CardProductos.jsx';
import './ProductList.css';

const ProductList = () => {
const [products, setProducts] = useState([]);
const [loading, setLoading] = useState(true);
const [error, setError] = useState(null);

useEffect(() => {
const fetchProducts = async () => {
try {
const response = await fetch('http://localhost:8080/api/productos');
if (!response.ok) {
throw new Error('Error al cargar los productos');
}
const data = await response.json();
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
: products?.productos || products?.data || products?.items || [];

return (
<div className="product-list-container">
<h1 className="product-list-title">Lista de Productos</h1>
<div className="products-grid">
{items.length === 0 && <div>No hay productos.</div>}
{items.map(product => {
const id = product.id ?? product._id ?? product.codigo;
return (
<Link
to={`/products/${id}`}
key={id}
style={{ textDecoration: 'none', color: 'inherit' }}
>
<CardProductos product={product} />
</Link>
);
})}
</div>
</div>
);
};

export default ProductList;
