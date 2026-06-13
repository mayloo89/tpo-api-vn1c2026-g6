import React, { useState, useEffect, useRef } from 'react';
import { apiRequest } from '../services/apiClient.js';
import { getAuthToken } from '../services/apiClient.js';
import './ProductManagement.css';

const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080';

const initialProductForm = {
  nombre: '',
  descripcion: '',
  precio: '',
  categoria: '',
  stock: '',
};

const ProductManagement = () => {
  const [products, setProducts] = useState([]);
  const [formData, setFormData] = useState(initialProductForm);
  const [imageFile, setImageFile] = useState(null);
  const [imagePreview, setImagePreview] = useState(null);
  const [editingId, setEditingId] = useState(null);
  const [message, setMessage] = useState('');
  const [loading, setLoading] = useState(false);
  const [showForm, setShowForm] = useState(false);
  const fileInputRef = useRef(null);

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

  const handleFileChange = (e) => {
    const file = e.target.files[0];
    if (!file) return;
    setImageFile(file);
    setImagePreview(URL.createObjectURL(file));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);

    try {
      const path = editingId ? `/api/productos/${editingId}` : '/api/productos';
      const method = editingId ? 'PUT' : 'POST';

      const saved = await apiRequest(path, {
        method,
        body: JSON.stringify({
          ...formData,
          precio: parseFloat(formData.precio),
          stock: parseInt(formData.stock),
        }),
      });

      if (imageFile) {
        const formDataImg = new FormData();
        formDataImg.append('archivo', imageFile);
        await fetch(`${API_BASE_URL}/api/productos/${saved.id}/imagen`, {
          method: 'POST',
          headers: { Authorization: `Bearer ${getAuthToken()}` },
          body: formDataImg,
        });
      }

      setMessage(editingId ? 'Producto actualizado exitosamente' : 'Producto creado exitosamente');
      resetForm();
      fetchProducts();
    } catch (err) {
      setMessage(`Error: ${err.message}`);
    } finally {
      setLoading(false);
    }
  };

  const handleEdit = (product) => {
    setFormData({
      nombre: product.nombre || '',
      descripcion: product.descripcion || '',
      precio: product.precio || '',
      categoria: product.categoria || '',
      stock: product.stock || '',
    });
    setImagePreview(product.imagen ? `${API_BASE_URL}${product.imagen}` : null);
    setImageFile(null);
    setEditingId(product.id);
    setShowForm(true);
  };

  const handleDelete = async (id) => {
    if (!window.confirm('¿Estás seguro de que deseas eliminar este producto?')) return;
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

  const resetForm = () => {
    setFormData(initialProductForm);
    setImageFile(null);
    setImagePreview(null);
    setEditingId(null);
    setShowForm(false);
    if (fileInputRef.current) fileInputRef.current.value = '';
  };

  return (
    <section className="product-management">
      <div className="product-management__header">
        <h2>Gestión de Productos</h2>
        {!showForm && (
          <button className="product-management__btn-add" onClick={() => setShowForm(true)}>
            + Agregar Producto
          </button>
        )}
      </div>

      {message && (
        <div className={`product-management__message ${message.includes('Error') ? 'error' : 'success'}`}>
          {message}
        </div>
      )}

      {showForm && (
        <form className="product-management__form" onSubmit={handleSubmit}>
          <h3>{editingId ? 'Editar Producto' : 'Nuevo Producto'}</h3>

          <div className="product-management__form-row">
            <label>
              Nombre
              <input type="text" name="nombre" value={formData.nombre} onChange={handleInputChange} required />
            </label>
            <label>
              Categoría
              <input type="text" name="categoria" value={formData.categoria} onChange={handleInputChange} />
            </label>
          </div>

          <label>
            Descripción
            <textarea name="descripcion" value={formData.descripcion} onChange={handleInputChange} rows={3} />
          </label>

          <div className="product-management__form-row">
            <label>
              Precio
              <input type="number" name="precio" step="0.01" value={formData.precio} onChange={handleInputChange} required />
            </label>
            <label>
              Stock
              <input type="number" name="stock" value={formData.stock} onChange={handleInputChange} required />
            </label>
          </div>

          <label>
            Imagen del producto
            <div className="product-management__file-area">
              {imagePreview && (
                <img src={imagePreview} alt="Preview" className="product-management__image-preview" />
              )}
              <input
                ref={fileInputRef}
                type="file"
                accept="image/*"
                onChange={handleFileChange}
                className="product-management__file-input"
              />
              <span className="product-management__file-hint">
                {imageFile ? imageFile.name : 'Seleccioná una imagen (JPG, PNG, WebP — máx. 5MB)'}
              </span>
            </div>
          </label>

          <div className="product-management__form-actions">
            <button type="submit" disabled={loading}>
              {loading ? 'Guardando...' : editingId ? 'Actualizar' : 'Crear'}
            </button>
            <button type="button" onClick={resetForm}>Cancelar</button>
          </div>
        </form>
      )}

      {loading && !showForm ? (
        <p style={{ color: 'var(--text-muted)', padding: '1rem 0' }}>Cargando productos...</p>
      ) : (
        <div className="product-management__table-container">
          <table className="product-management__table">
            <thead>
              <tr>
                <th>Imagen</th>
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
                <tr><td colSpan="7" className="product-management__no-data">No hay productos</td></tr>
              ) : (
                products.map(product => (
                  <tr key={product.id}>
                    <td>
                      {product.imagen ? (
                        <img
                          src={`${API_BASE_URL}${product.imagen}`}
                          alt={product.nombre}
                          className="product-management__thumb"
                        />
                      ) : (
                        <div className="product-management__thumb product-management__thumb--empty">—</div>
                      )}
                    </td>
                    <td>{product.nombre}</td>
                    <td>{product.categoria || '-'}</td>
                    <td>${Number(product.precio).toLocaleString('es-AR')}</td>
                    <td>{product.stock}</td>
                    <td className="product-management__desc">{product.descripcion}</td>
                    <td className="product-management__actions">
                      <button className="product-management__btn-edit" onClick={() => handleEdit(product)} disabled={loading}>Editar</button>
                      <button className="product-management__btn-delete" onClick={() => handleDelete(product.id)} disabled={loading}>Eliminar</button>
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
