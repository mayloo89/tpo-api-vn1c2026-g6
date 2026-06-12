import React, { useEffect, useState } from 'react';
import { useDispatch } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import { loadCart, resetCart } from '../store/cartSlice.js';
import { loadFavorites, resetFavorites } from '../store/favoritesSlice.js';
import { getStoredUser } from '../services/apiClient.js';
import './UserManagement.css';

const initialRegister = {
  nombre: '',
  apellido: '',
  email: '',
  password: '',
};

const initialLogin = {
  email: '',
  password: '',
};

const UserManagement = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const savedUser = JSON.parse(localStorage.getItem('user') || 'null');
  const [mode, setMode] = useState(savedUser ? 'profile' : 'login');
  const [registerData, setRegisterData] = useState(initialRegister);
  const [loginData, setLoginData] = useState(initialLogin);
  const [user, setUser] = useState(savedUser);
  const [message, setMessage] = useState('');
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const user = getStoredUser();

    if (user?.token) {
      dispatch(loadCart());
      dispatch(loadFavorites());
    }
  }, [dispatch]);

  const resetMessage = () => setMessage('');

  const handleChange = (event, formType) => {
    const { name, value } = event.target;
    if (formType === 'register') {
      setRegisterData(prev => ({ ...prev, [name]: value }));
    } else {
      setLoginData(prev => ({ ...prev, [name]: value }));
    }
  };

  const handleRegister = async event => {
    event.preventDefault();
    setLoading(true);
    setMessage('');
    try {
      const response = await fetch('http://localhost:8080/api/auth/register', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(registerData),
      });

      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(errorText || 'Error al registrar el usuario');
      }

      const text = await response.text();
      setMessage(`Registro exitoso: ${text}`);
      setRegisterData(initialRegister);
      setMode('login');
    } catch (err) {
      setMessage(`Registro fallido: ${err.message}`);
    } finally {
      setLoading(false);
    }
  };

  const handleLogin = async event => {
    event.preventDefault();
    setLoading(true);
    setMessage('');
    try {
      const response = await fetch('http://localhost:8080/api/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(loginData),
      });

      if (!response.ok) {
        const errorBody = await response.text();
        throw new Error(errorBody || 'Error en el inicio de sesión');
      }

      const data = await response.json();
      const currentUser = { nombre: data.nombre || loginData.email, email: loginData.email, token: data.token };
      setUser(currentUser);
      localStorage.setItem('user', JSON.stringify(currentUser));
      dispatch(loadCart());
      dispatch(loadFavorites());
      setMessage(`Bienvenido ${data.nombre || loginData.email}`);
      setLoginData(initialLogin);
      setMode('profile');
      navigate('/auth');
    } catch (err) {
      setMessage(`Login fallido: ${err.message}`);
    } finally {
      setLoading(false);
    }
  };

  const handleLogout = () => {
    setUser(null);
    localStorage.removeItem('user');
    dispatch(resetCart());
    dispatch(resetFavorites());
    setMode('login');
    setMessage('Sesión cerrada.');
  };

  return (
    <section className="user-management">
      <div className="user-management__header">
        <h2>Gestión de usuario</h2>
        <div className="user-management__tabs">
          <button type="button" className={mode === 'login' ? 'active' : ''} onClick={() => { setMode('login'); resetMessage(); }}>
            Login
          </button>
          <button type="button" className={mode === 'register' ? 'active' : ''} onClick={() => { setMode('register'); resetMessage(); }}>
            Registro
          </button>
          <button type="button" className={mode === 'profile' ? 'active' : ''} onClick={() => { setMode('profile'); resetMessage(); }}>
            Perfil
          </button>
        </div>
      </div>

      {message && <div className="user-management__message">{message}</div>}

      {mode === 'register' && (
        <form className="user-management__form" onSubmit={handleRegister}>
          <label>
            Nombre
            <input name="nombre" value={registerData.nombre} onChange={e => handleChange(e, 'register')} required />
          </label>
          <label>
            Apellido
            <input name="apellido" value={registerData.apellido} onChange={e => handleChange(e, 'register')} required />
          </label>
          <label>
            Email
            <input type="email" name="email" value={registerData.email} onChange={e => handleChange(e, 'register')} required />
          </label>
          <label>
            Contraseña
            <input type="password" name="password" value={registerData.password} onChange={e => handleChange(e, 'register')} required minLength={6} />
          </label>
          <button type="submit" disabled={loading}>{loading ? 'Registrando...' : 'Registrarse'}</button>
        </form>
      )}

      {mode === 'login' && (
        <form className="user-management__form" onSubmit={handleLogin}>
          <label>
            Email
            <input type="email" name="email" value={loginData.email} onChange={e => handleChange(e, 'login')} required />
          </label>
          <label>
            Contraseña
            <input type="password" name="password" value={loginData.password} onChange={e => handleChange(e, 'login')} required />
          </label>
          <button type="submit" disabled={loading}>{loading ? 'Iniciando sesión...' : 'Iniciar sesión'}</button>
        </form>
      )}

      {mode === 'profile' && (
        <div className="user-management__profile">
          {user ? (
            <>
              <p><strong>Nombre:</strong> {user.nombre}</p>
              <p><strong>Email:</strong> {user.email}</p>
              <p><strong>Token JWT:</strong></p>
              {user.token ? (
                <textarea
                  className="user-management__token"
                  readOnly
                  value={user.token}
                  rows={4}
                />
              ) : (
                <p>No disponible</p>
              )}
              <button type="button" onClick={handleLogout}>Cerrar sesión</button>
            </>
          ) : (
            <p>No hay usuario autenticado. Inicia sesión primero.</p>
          )}
        </div>
      )}
    </section>
  );
};

export default UserManagement;
