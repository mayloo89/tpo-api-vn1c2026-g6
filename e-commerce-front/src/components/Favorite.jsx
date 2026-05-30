import { Link } from 'react-router-dom'
import { useFavorites } from '../context/FavoriteContext.jsx'
import './Favorite.css'

const Favorite = () => {
    const { favoriteItems, removeFromFavorite } = useFavorites()

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
                                <img
                                    src={product.imagen}
                                    alt={product.nombre}
                                    className="favorite-card__image"
                                />
                                <div className="favorite-card__body">
                                    <div className="favorite-card__top">
                                        <div>
                                            <h2 className="favorite-card__title">{product.nombre}</h2>
                                            <p className="favorite-card__category">{product.categoria}</p>
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
                                            onClick={() => removeFromFavorite(productId)}
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