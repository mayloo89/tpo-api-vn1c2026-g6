import React, { useState, useEffect } from 'react';
import { apiRequest } from '../services/apiClient.js';
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
      const data = await apiRequest('/api/productos');
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

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);

    try {
      const path = editingId ? `/api/productos/${editingId}` : '/api/productos';
      const method = editingId ? 'PUT' : 'POST';

      await apiRequest(path, {
        method,
        body: JSON.stringify({
          ...formData,
          precio: parseFloat(formData.precio),
          stock: parseInt(formData.stock),
        }),
      });

      setMessage(editingId ? 'Producto actualizado exitosamente' : 'Producto creado exitosamente');
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
      await apiRequest(`/api/productos/${id}`, { method: 'DELETE' });
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
