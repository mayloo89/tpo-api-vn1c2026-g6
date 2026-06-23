import { Link } from 'react-router-dom'
import { useDispatch, useSelector } from 'react-redux'
import { removeFromFavorite, selectFavoriteItems, selectFavoritesStatus, selectFavoritesError } from '../store/favoritesSlice.js'
import './Favorite.css'

const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080'

const Favorite = () => {
    const dispatch = useDispatch()
    const favoriteItems = useSelector(selectFavoriteItems)
    const favoritesStatus = useSelector(selectFavoritesStatus)
    const favoritesError = useSelector(selectFavoritesError)

    if (favoritesStatus === 'loading') {
        return <div className="favorite-page__loading">Cargando favoritos...</div>
    }

    if (favoritesStatus === 'failed') {
        return <div className="favorite-page__error">Error al cargar favoritos: {favoritesError}</div>
    }

    return (
        <section className="favorite-page">
            <header className="favorite-page__header">
                <h1 className="favorite-page__title">Favoritos</h1>
                <p className="favorite-page__subtitle">
                    Guardaste {favoriteItems.length > 0 ? `${favoriteItems.length} productos` : 'ningún producto'}.
                </p>
            </header>

            {favoriteItems.length === 0 ? (
                <div className="favorite-page__empty">
                    <p>Tu lista de favoritos está vacía.</p>
                    <Link to="/products" className="favorite-page__link">
                        Explorar productos
                    </Link>
                </div>
            ) : (
                <div className="favorite-page__grid">
                    {favoriteItems.map(product => {
                        const productId = product.id ?? product._id ?? product.codigo

                        return (
                            <article key={productId} className="favorite-card">
                                {product.imagen ? (
                                    <img
                                        src={`${API_BASE_URL}${product.imagen}`}
                                        alt={product.nombre}
                                        className="favorite-card__image"
                                    />
                                ) : (
                                    <div className="favorite-card__image favorite-card__image--placeholder">
                                        Sin imagen
                                    </div>
                                )}
                                <div className="favorite-card__body">
                                    <div className="favorite-card__top">
                                        <div>
                                            <h2 className="favorite-card__title">{product.nombre}</h2>
                                            {product.categoria ? (
                                                <p className="favorite-card__category">{product.categoria}</p>
                                            ) : null}
                                        </div>
                                        <span className="favorite-card__price">${Number(product.precio).toLocaleString('es-AR')}</span>
                                    </div>
                                    <p className="favorite-card__description">{product.descripcion}</p>
                                    <div className="favorite-card__actions">
                                        <Link to={`/products/${productId}`} className="favorite-card__button favorite-card__button--secondary">
                                            Ver detalle
                                        </Link>
                                        <button
                                            type="button"
                                            className="favorite-card__button favorite-card__button--danger"
                                            onClick={() => dispatch(removeFromFavorite(productId))}
                                        >
                                            Quitar
                                        </button>
                                    </div>
                                </div>
                            </article>
                        )
                    })}
                </div>
            )}
        </section>
    )
}

export default Favorite