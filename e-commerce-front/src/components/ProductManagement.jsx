import React, { useState, useEffect } from 'react';
import './ProductManagement.css';

const initialProductForm = {
  nombre: '',
  descripcion: '',
  precio: '',
  categoria: '',
  imagen: '',
  stock: '',
};

const ProductManagement = () => {
  const [products, setProducts] = useState([]);
  const [formData, setFormData] = useState(initialProductForm);
  const [editingId, setEditingId] = useState(null);
  const [message, setMessage] = useState('');
  const [loading, setLoading] = useState(false);
  const [showForm, setShowForm] = useState(false);

  // Cargar productos al montar el componente
  useEffect(() => {
    fetchProducts();
  }, []);

  const fetchProducts = async () => {
    setLoading(true);
    try {
      const response = await fetch('http://localhost:8080/api/productos');
      if (!response.ok) throw new Error('Error al cargar productos');
      const data = await response.json();
      setProducts(Array.isArray(data) ? data : data.data || []);
      setMessage('');
    } catch (err) {
      setMessage(`Error: ${err.message}`);
    } finally {
      setLoading(false);
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const getAuthHeaders = () => {
    const user = JSON.parse(localStorage.getItem('user') || 'null');
    const headers = { 'Content-Type': 'application/json' };
    if (user?.token) {
      headers['Authorization'] = `Bearer ${user.token}`;
    }
    return headers;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);

    try {
      const url = editingId
        ? `http://localhost:8080/api/productos/${editingId}`
        : 'http://localhost:8080/api/productos';

      const method = editingId ? 'PUT' : 'POST';

      const response = await fetch(url, {
        method,
        headers: getAuthHeaders(),
        body: JSON.stringify({
          ...formData,
          precio: parseFloat(formData.precio),
          stock: parseInt(formData.stock),
        }),
      });

      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(errorText || 'Error en la operación');
      }

      setMessage(
        editingId
          ? 'Producto actualizado exitosamente'
          : 'Producto creado exitosamente'
      );
      setFormData(initialProductForm);
      setEditingId(null);
      setShowForm(false);
      fetchProducts();
    } catch (err) {
      setMessage(`Error: ${err.message}`);
    } finally {
      setLoading(false);
    }
  };

  const handleEdit = (product) => {
    setFormData(product);
    setEditingId(product.id);
    setShowForm(true);
  };

  const handleDelete = async (id) => {
    if (!window.confirm('¿Estás seguro de que deseas eliminar este producto?')) {
      return;
    }

    setLoading(true);
    try {
      const response = await fetch(
        `http://localhost:8080/api/productos/${id}`,
        { 
          method: 'DELETE',
          headers: getAuthHeaders()
        }
      );

      if (!response.ok) throw new Error('Error al eliminar el producto');

      setMessage('Producto eliminado exitosamente');
      fetchProducts();
    } catch (err) {
      setMessage(`Error: ${err.message}`);
    } finally {
      setLoading(false);
    }
  };

  const handleCancel = () => {
    setFormData(initialProductForm);
    setEditingId(null);
    setShowForm(false);
  };

  return (
    <section className="product-management">
      <div className="product-management__header">
        <h2>Gestión de Productos</h2>
        {!showForm && (
          <button
            className="product-management__btn-add"
            onClick={() => setShowForm(true)}
          >
            + Agregar Producto
          </button>
        )}
      </div>

      {message && (
        <div
          className={`product-management__message ${
            message.includes('Error') ? 'error' : 'success'
          }`}
        >
          {message}
        </div>
      )}

      {showForm && (
        <form className="product-management__form" onSubmit={handleSubmit}>
          <h3>{editingId ? 'Editar Producto' : 'Nuevo Producto'}</h3>

          <div className="product-management__form-row">
            <label>
              Nombre
              <input
                type="text"
                name="nombre"
                value={formData.nombre}
                onChange={handleInputChange}
                required
              />
            </label>
            <label>
              Categoría
              <input
                type="text"
                name="categoria"
                value={formData.categoria}
                onChange={handleInputChange}
              />
            </label>
          </div>

          <label>
            Descripción
            <textarea
              name="descripcion"
              value={formData.descripcion}
              onChange={handleInputChange}
              rows={3}
            />
          </label>

          <div className="product-management__form-row">
            <label>
              Precio
              <input
                type="number"
                name="precio"
                step="0.01"
                value={formData.precio}
                onChange={handleInputChange}
                required
              />
            </label>
            <label>
              Stock
              <input
                type="number"
                name="stock"
                value={formData.stock}
                onChange={handleInputChange}
                required
              />
            </label>
          </div>

          <label>
            URL Imagen
            <input
              type="text"
              name="imagen"
              value={formData.imagen}
              onChange={handleInputChange}
            />
          </label>

          <div className="product-management__form-actions">
            <button type="submit" disabled={loading}>
              {loading ? 'Guardando...' : editingId ? 'Actualizar' : 'Crear'}
            </button>
            <button type="button" onClick={handleCancel}>
              Cancelar
            </button>
          </div>
        </form>
      )}

      {loading && !showForm ? (
        <p>Cargando productos...</p>
      ) : (
        <div className="product-management__table-container">
          <table className="product-management__table">
            <thead>
              <tr>
                <th>Nombre</th>
                <th>Categoría</th>
                <th>Precio</th>
                <th>Stock</th>
                <th>Descripción</th>
                <th>Acciones</th>
              </tr>
            </thead>
            <tbody>
              {products.length === 0 ? (
                <tr>
                  <td colSpan="6" className="product-management__no-data">
                    No hay productos
                  </td>
                </tr>
              ) : (
                products.map(product => (
                  <tr key={product.id}>
                    <td>{product.nombre}</td>
                    <td>{product.categoria || '-'}</td>
                    <td>${Number(product.precio).toLocaleString('es-AR')}</td>
                    <td>{product.stock}</td>
                    <td className="product-management__desc">
                      {product.descripcion}
                    </td>
                    <td className="product-management__actions">
                      <button
                        className="product-management__btn-edit"
                        onClick={() => handleEdit(product)}
                        disabled={loading}
                      >
                        Editar
                      </button>
                      <button
                        className="product-management__btn-delete"
                        onClick={() => handleDelete(product.id)}
                        disabled={loading}
                      >
                        Eliminar
                      </button>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      )}
    </section>
  );
};

export default ProductManagement;
