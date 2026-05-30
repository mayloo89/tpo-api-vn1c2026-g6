import React, { useEffect, useState } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import './ProductDetail.css';

const ProductDetail = () => {
const { id } = useParams();
const navigate = useNavigate();
const [product, setProduct] = useState(null);
const [loading, setLoading] = useState(true);
const [error, setError] = useState(null);

useEffect(() => {
const fetchProduct = async () => {
try {
const response = await fetch(`http://localhost:8080/api/productos/${id}`);
if (!response.ok) throw new Error('Producto no encontrado');
const data = await response.json();
setProduct(data);
} catch (err) {
setError(err.message);
} finally {
setLoading(false);
}
};

fetchProduct();
}, [id]);

if (loading) return <div className="product-detail__loading">Cargando producto...</div>;
if (error) return <div className="product-detail__error">Error: {error}</div>;
if (!product) return <div className="product-detail__error">Producto no encontrado</div>;

return (
<div className="product-detail">
<button className="product-detail__back" onClick={() => navigate(-1)}>
← Volver
</button>

<div className="product-detail__card">
{product.imagen && (
<img
src={product.imagen}
alt={product.nombre}
className="product-detail__image"
/>
)}
<div className="product-detail__info">
<h1 className="product-detail__name">{product.nombre}</h1>
<p className="product-detail__description">{product.descripcion}</p>
<p className="product-detail__price">
${Number(product.precio).toLocaleString('es-AR')}
</p>
<span className={`product-detail__stock ${product.stock > 0 ? 'en-stock' : 'sin-stock'}`}>
{product.stock > 0 ? `Stock disponible: ${product.stock}` : 'Agotado'}
</span>
<Link to="/products" className="product-detail__link">
Ver todos los productos
</Link>
</div>
</div>
</div>
);
};

export default ProductDetail;
