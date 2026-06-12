import { Link, useLocation } from 'react-router-dom'
import { useSelector } from 'react-redux'
import { selectCartCount } from '../store/cartSlice.js'
import { selectFavoriteItems } from '../store/favoritesSlice.js'
import '../styles/Navbar.css'

function Navbar() {
  const location = useLocation()
  const favoriteItems = useSelector(selectFavoriteItems)
  const cartCount = useSelector(selectCartCount)
  const user = JSON.parse(localStorage.getItem('user') || 'null')

const isActive = (path) => {
if (path === '/products') return location.pathname.startsWith('/products')
return location.pathname === path
}

return (
<nav className="navbar">
<div className="navbar-container">
<Link to="/" className="navbar-brand">
E-Commerce
</Link>

<ul className="nav-menu">
<li>
<Link to="/" className={isActive('/') ? 'nav-link active' : 'nav-link'}>
Inicio
</Link>
</li>
<li>
<Link to="/products" className={isActive('/products') ? 'nav-link active' : 'nav-link'}>
Productos
</Link>
</li>
        <li>
          <Link to="/favorites" className={isActive('/favorites') ? 'nav-link active' : 'nav-link'}>
            Favoritos {favoriteItems.length > 0 ? `(${favoriteItems.length})` : ''}
          </Link>
        </li>
        <li>
          <Link to="/cart" className={isActive('/cart') ? 'nav-link active' : 'nav-link'}>
            Carrito {cartCount > 0 ? `(${cartCount})` : ''}
          </Link>
        </li>
{user ? (
<li>
<Link to="/admin/products" className={isActive('/admin/products') ? 'nav-link active' : 'nav-link'}>
Gestion
</Link>
</li>
) : null}
<li>
<Link to="/auth" className={isActive('/auth') ? 'nav-link active' : 'nav-link'}>
{user ? 'Mi Cuenta' : 'Ingresar'}
</Link>
</li>
</ul>
</div>
</nav>
)
}

export default Navbar
