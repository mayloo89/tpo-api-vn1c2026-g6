import { useEffect, useState } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import { addToCart, selectIsInCart } from '../store/cartSlice.js';
import { addToFavorite, removeFromFavorite, selectIsFavorite } from '../store/favoritesSlice.js';
import { apiRequest } from '../services/apiClient.js';
import './ProductDetail.css';

const ProductDetail = () => {
const { id } = useParams();
const navigate = useNavigate();
const dispatch = useDispatch();
const [product, setProduct] = useState(null);
const [loading, setLoading] = useState(true);
const [error, setError] = useState(null);

const productId = product ? (product.id ?? product._id ?? product.codigo) : null;
const favorite = useSelector(state => selectIsFavorite(state, productId));
const inCart = useSelector(state => selectIsInCart(state, productId));

useEffect(() => {
const fetchProduct = async () => {
try {
const data = await apiRequest(`/api/productos/${id}`);
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
  const handleFavoriteClick = () => {
    if (favorite) {
      dispatch(removeFromFavorite(productId));
      return;
    }

    dispatch(addToFavorite(product));
  };

  const handleAddToCart = () => {
    dispatch(addToCart({ product }));
  };

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
          <button type="button" className={favorite ? 'product-detail__favorite is-favorite' : 'product-detail__favorite'} onClick={handleFavoriteClick}>
            {favorite ? 'Quitar de favoritos' : 'Agregar a favoritos'}
          </button>
          <button type="button" className={inCart ? 'product-detail__cart in-cart' : 'product-detail__cart'} onClick={handleAddToCart}>
            {inCart ? 'En el carrito' : 'Agregar al carrito'}
          </button>
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
