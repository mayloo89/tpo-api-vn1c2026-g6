import React from 'react'
import { Link } from 'react-router-dom'
import { useFavorites } from '../context/FavoriteContext.jsx'
import { useCart } from '../context/CartContext.jsx'
import './CardProductos.css'

const CardProductos = ({ product }) => {
  const { addToFavorite, removeFromFavorite, isFavorite } = useFavorites()
  const { addToCart, isInCart } = useCart()
  const productId = product.id ?? product._id ?? product.codigo
  const favorite = isFavorite(productId)

  const handleFavoriteClick = event => {
    event.preventDefault()
    event.stopPropagation()

    if (favorite) {
      removeFromFavorite(productId)
      return
    }

    addToFavorite(product)
  }

  const handleAddToCart = event => {
    event.preventDefault()
    event.stopPropagation()
    addToCart(product)
  }

  return (
    <article className="card-producto">
      <div className="producto-imagen-container">
        <img
          src={product.imagen}
          alt={product.nombre}
          className="producto-imagen"
        />
        <button
          type="button"
          className={favorite ? 'producto-favorite is-favorite' : 'producto-favorite'}
          onClick={handleFavoriteClick}
          aria-label={favorite ? `Quitar ${product.nombre} de favoritos` : `Agregar ${product.nombre} a favoritos`}
        >
          {favorite ? '♥' : '♡'}
        </button>
        <span className="producto-categoria">{product.categoria}</span>
      </div>

      <div className="producto-info">
        <div className="producto-info__header">
          <h3 className="producto-nombre">{product.nombre}</h3>
          <span className="producto-precio">${Number(product.precio).toLocaleString('es-AR')}</span>
        </div>
        <p className="producto-descripcion">{product.descripcion}</p>

        <div className="producto-rating">
          <span className="stars">⭐ {product.rating}</span>
        </div>

  <div className="producto-footer">
  <span className={product.stock > 0 ? 'en-stock' : 'sin-stock'}>
    {product.stock > 0 ? `Stock: ${product.stock}` : 'Agotado'}
  </span>
  <div className="producto-footer__actions">
    <button
      type="button"
      className={isInCart(productId) ? 'btn-carrito in-cart' : 'btn-carrito'}
      onClick={handleAddToCart}
    >
      {isInCart(productId) ? 'En carrito' : 'Carrito'}
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
