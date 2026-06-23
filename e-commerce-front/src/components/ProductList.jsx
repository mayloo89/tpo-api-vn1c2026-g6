import React, { useEffect, useState } from 'react'
import CardProductos from './CardProductos.jsx'
import { apiRequest } from '../services/apiClient.js'
import './ProductList.css'

const ProductList = () => {
  const [products, setProducts] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)
  const [nombre, setNombre] = useState('')
  const [precioMax, setPrecioMax] = useState('')

  useEffect(() => {
    const fetchProducts = async () => {
      setLoading(true)
      setError(null)
      try {
        const params = new URLSearchParams()
        if (nombre.trim()) params.append('nombre', nombre.trim())
        if (precioMax !== '') params.append('precioMax', precioMax)
        const query = params.toString() ? `?${params.toString()}` : ''
        const data = await apiRequest(`/api/productos${query}`)
        setProducts(Array.isArray(data) ? data : data?.data || [])
      } catch (err) {
        setError(err.message)
      } finally {
        setLoading(false)
      }
    }

    const debounce = setTimeout(fetchProducts, 400)
    return () => clearTimeout(debounce)
  }, [nombre, precioMax])

  return (
    <div className="product-list-container">
      <h1 className="product-list-title">Lista de Productos</h1>

      <div className="product-list-filters">
        <input
          type="text"
          className="product-list-filter-input"
          placeholder="Buscar por nombre..."
          value={nombre}
          onChange={e => setNombre(e.target.value)}
        />
        <input
          type="number"
          className="product-list-filter-input"
          placeholder="Precio máximo..."
          min="0"
          value={precioMax}
          onChange={e => setPrecioMax(e.target.value)}
        />
        {(nombre || precioMax) && (
          <button
            className="product-list-filter-clear"
            onClick={() => { setNombre(''); setPrecioMax('') }}
          >
            Limpiar filtros
          </button>
        )}
      </div>

      {loading && <p className="product-list-status">Cargando productos...</p>}
      {error && <p className="product-list-status product-list-status--error">Error: {error}</p>}

      {!loading && !error && (
        <div className="products-grid">
          {products.length === 0
            ? <p className="product-list-status">No se encontraron productos.</p>
            : products.map(product => {
                const id = product.id ?? product._id ?? product.codigo
                return <CardProductos key={id} product={{ ...product, id }} />
              })
          }
        </div>
      )}
    </div>
  )
}

export default ProductList
