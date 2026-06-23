import { Link } from 'react-router-dom'
import { useDispatch, useSelector } from 'react-redux'
import { addToCart, selectIsInCart } from '../store/cartSlice.js'
import { addToFavorite, removeFromFavorite, selectIsFavorite } from '../store/favoritesSlice.js'
import './CardProductos.css'

const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080'

const CardProductos = ({ product }) => {
  const dispatch = useDispatch()
  const productId = product.id ?? product._id ?? product.codigo
  const favorite = useSelector(state => selectIsFavorite(state, productId))
  const inCart = useSelector(state => selectIsInCart(state, productId))

  const handleFavoriteClick = event => {
    event.preventDefault()
    event.stopPropagation()

    if (favorite) {
      dispatch(removeFromFavorite(productId))
      return
    }

    dispatch(addToFavorite(product))
  }

  const handleAddToCart = event => {
    event.preventDefault()
    event.stopPropagation()
    dispatch(addToCart({ product }))
  }

  return (
    <article className="card-producto">
      <div className="producto-imagen-container">
        {product.imagen ? (
          <img
            src={`${API_BASE_URL}${product.imagen}`}
            alt={product.nombre}
            className="producto-imagen"
          />
        ) : (
          <div className="producto-imagen producto-imagen--placeholder">Sin imagen</div>
        )}
        <button
          type="button"
          className={favorite ? 'producto-favorite is-favorite' : 'producto-favorite'}
          onClick={handleFavoriteClick}
          aria-label={favorite ? `Quitar ${product.nombre} de favoritos` : `Agregar ${product.nombre} a favoritos`}
        >
          {favorite ? '♥' : '♡'}
        </button>
        {product.categoria ? <span className="producto-categoria">{product.categoria}</span> : null}
      </div>

      <div className="producto-info">
        <div className="producto-info__header">
          <h3 className="producto-nombre">{product.nombre}</h3>
          <span className="producto-precio">${Number(product.precio).toLocaleString('es-AR')}</span>
        </div>
        <p className="producto-descripcion">{product.descripcion}</p>

        {product.rating ? (
          <div className="producto-rating">
            <span className="stars">⭐ {product.rating}</span>
          </div>
        ) : null}

  <div className="producto-footer">
  <span className={product.stock > 0 ? 'en-stock' : 'sin-stock'}>
    {product.stock > 0 ? `Stock: ${product.stock}` : 'Agotado'}
  </span>
  <div className="producto-footer__actions">
    <button
      type="button"
      className={inCart ? 'btn-carrito in-cart' : 'btn-carrito'}
      onClick={handleAddToCart}
    >
      {inCart ? 'En carrito' : 'Carrito'}
    </button>
    <Link to={`/products/${productId}`} className="btn-agregar">
      Ver detalle
    </Link>
  </div>
</div>
      </div>
    </article>
  )
}

export default CardProductos
