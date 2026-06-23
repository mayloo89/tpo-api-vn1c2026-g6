import { Link, useNavigate } from 'react-router-dom'
import { useDispatch, useSelector } from 'react-redux'
import {
  clearCart,
  removeFromCart,
  selectCartCount,
  selectCartError,
  selectCartItems,
  selectCartStatus,
  selectCartTotal,
  updateQuantity,
} from '../store/cartSlice.js'
import './Cart.css'

const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080'

const Cart = () => {
  const dispatch = useDispatch()
  const navigate = useNavigate()

  const cartItems = useSelector(selectCartItems)
  const cartTotal = useSelector(selectCartTotal)
  const cartCount = useSelector(selectCartCount)
  const cartStatus = useSelector(selectCartStatus)
  const cartError = useSelector(selectCartError)

  const handleCheckout = () => {
  const confirmar = window.confirm(
    '¿Estás seguro de que querés confirmar la compra?'
  )

  if (!confirmar) return

  dispatch(clearCart())

  alert(
    '🎉 ¡Compra realizada con éxito!\n\nGracias por comprar en nuestro e-commerce.'
  )

  navigate('/')
}

  if (cartStatus === 'loading') {
    return <div className="cart-page__loading">Cargando carrito...</div>
  }

  if (cartStatus === 'failed') {
    return <div className="cart-page__error">Error al cargar el carrito: {cartError}</div>
  }

  return (
    <section className="cart-page">
      <header className="cart-page__header">
        <h1 className="cart-page__title">Carrito</h1>
        <p className="cart-page__subtitle">
          {cartCount > 0
            ? `${cartCount} producto${cartCount !== 1 ? 's' : ''} — Total: $${cartTotal.toLocaleString('es-AR')}`
            : 'Tu carrito está vacío'}
        </p>
      </header>

      {cartItems.length === 0 ? (
        <div className="cart-page__empty">
          <p>No tenés productos en el carrito.</p>
          <Link to="/products" className="cart-page__link">
            Explorar productos
          </Link>
        </div>
      ) : (
        <>
          <div className="cart-page__grid">
            {cartItems.map(product => {
              const productId = product.id ?? product._id ?? product.codigo

              return (
                <article key={productId} className="cart-card">
                  {product.imagen ? (
                    <img
                      src={`${API_BASE_URL}${product.imagen}`}
                      alt={product.nombre}
                      className="cart-card__image"
                    />
                  ) : (
                    <div className="cart-card__image cart-card__image--placeholder">
                      Sin imagen
                    </div>
                  )}

                  <div className="cart-card__body">
                    <div className="cart-card__top">
                      <div>
                        <h2 className="cart-card__title">{product.nombre}</h2>
                        {product.categoria ? (
                          <p className="cart-card__category">
                            {product.categoria}
                          </p>
                        ) : null}
                      </div>

                      <span className="cart-card__price">
                        ${Number(product.precio).toLocaleString('es-AR')}
                      </span>
                    </div>

                    {product.descripcion ? (
                      <p className="cart-card__description">
                        {product.descripcion}
                      </p>
                    ) : null}

                    <div className="cart-card__quantity">
                      <button
                        type="button"
                        className="cart-card__qty-btn"
                        disabled={product.quantity <= 1}
                        onClick={() => {
                          if (product.quantity === 1) {
                            dispatch(removeFromCart(productId))
                          } else {
                            dispatch(
                              updateQuantity({
                                productId,
                                quantity: product.quantity - 1,
                              })
                            )
                          }
                        }}
                      >
                        −
                      </button>

                      <span className="cart-card__qty-value">
                        {product.quantity}
                      </span>

                      <button
                        type="button"
                        className="cart-card__qty-btn"
                        onClick={() =>
                          dispatch(
                            updateQuantity({
                              productId,
                              quantity: product.quantity + 1,
                            })
                          )
                        }
                      >
                        +
                      </button>
                    </div>

                    <div className="cart-card__actions">
                      <Link
                        to={`/products/${productId}`}
                        className="cart-card__button cart-card__button--secondary"
                      >
                        Ver detalle
                      </Link>

                      <button
                        type="button"
                        className="cart-card__button cart-card__button--danger"
                        onClick={() => dispatch(removeFromCart(productId))}
                      >
                        Quitar
                      </button>
                    </div>
                  </div>
                </article>
              )
            })}
          </div>

          <footer className="cart-page__footer">
            <div className="cart-page__total">
              <span>Total:</span>
              <span>${cartTotal.toLocaleString('es-AR')}</span>
            </div>

            <div className="cart-page__footer-actions">
              <button
                type="button"
                className="cart-page__button cart-page__button--clear"
                onClick={() => dispatch(clearCart())}
              >
                Vaciar carrito
              </button>
              

              <button
                type="button"
                className="cart-page__button cart-page__button--checkout"
                onClick={handleCheckout}
              >
                Confirmar compra
              </button>
            </div>
          </footer>
        </>
      )}
    </section>
  )
}

export default Cart
