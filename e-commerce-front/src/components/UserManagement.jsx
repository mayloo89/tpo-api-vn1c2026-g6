import React, { useState, useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import { loadCart, resetCart } from '../store/cartSlice.js';
import { loadFavorites, resetFavorites } from '../store/favoritesSlice.js';
import { loginThunk, registerThunk, logoutThunk, clearAuthError, selectUser, selectAuthStatus, selectAuthError } from '../store/authSlice.js';
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
  const user = useSelector(selectUser);
  const authStatus = useSelector(selectAuthStatus);
  const authError = useSelector(selectAuthError);

  const [mode, setMode] = useState('login');

  useEffect(() => {
    setMode(user ? 'profile' : 'login')
  }, [user])
  const [registerData, setRegisterData] = useState(initialRegister);
  const [loginData, setLoginData] = useState(initialLogin);
  const [message, setMessage] = useState('');

  const loading = authStatus === 'loading';

  const resetMessage = () => {
    setMessage('');
    dispatch(clearAuthError());
  };

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
    setMessage('');
    const result = await dispatch(registerThunk(registerData));
    if (registerThunk.fulfilled.match(result)) {
      setMessage(`Registro exitoso: ${result.payload}`);
      setRegisterData(initialRegister);
      setMode('login');
    }
  };

  const handleLogin = async event => {
    event.preventDefault();
    setMessage('');
    const result = await dispatch(loginThunk(loginData));
    if (loginThunk.fulfilled.match(result)) {
      dispatch(loadCart());
      dispatch(loadFavorites());
      setMessage(`Bienvenido ${result.payload.nombre}`);
      setLoginData(initialLogin);
      setMode('profile');
      navigate('/auth');
    }
  };

  const handleLogout = async () => {
    await dispatch(logoutThunk());
    dispatch(resetCart());
    dispatch(resetFavorites());
    setMode('login');
    setMessage('Sesión cerrada.');
  };

  const displayMessage = message || (authError ? `Error: ${authError}` : '');

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

      {displayMessage && <div className="user-management__message">{displayMessage}</div>}

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
