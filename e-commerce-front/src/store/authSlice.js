import { createAsyncThunk, createSlice } from '@reduxjs/toolkit'
import { apiRequest } from '../services/apiClient.js'

export const loginThunk = createAsyncThunk('auth/login', async (credentials, { rejectWithValue }) => {
  try {
    const data = await apiRequest('/api/auth/login', {
      method: 'POST',
      body: JSON.stringify(credentials),
    })
    return { nombre: data.nombre, email: credentials.email }
  } catch (err) {
    return rejectWithValue(err.message)
  }
})

export const registerThunk = createAsyncThunk('auth/register', async (userData, { rejectWithValue }) => {
  try {
    return await apiRequest('/api/auth/register', {
      method: 'POST',
      body: JSON.stringify(userData),
    })
  } catch (err) {
    return rejectWithValue(err.message)
  }
})

export const logoutThunk = createAsyncThunk('auth/logout', async (_, { rejectWithValue }) => {
  try {
    return await apiRequest('/api/auth/logout', { method: 'POST' })
  } catch (err) {
    return rejectWithValue(err.message)
  }
})

export const checkAuthThunk = createAsyncThunk('auth/checkAuth', async (_, { rejectWithValue }) => {
  try {
    const data = await apiRequest('/api/auth/me')
    return { nombre: data.nombre, email: data.email }
  } catch (err) {
    return rejectWithValue(err.message)
  }
})

const authSlice = createSlice({
  name: 'auth',
  initialState: {
    user: null,
    status: 'idle',
    error: null,
  },
  reducers: {
    logout(state) {
      state.user = null
      state.status = 'idle'
      state.error = null
    },
    clearAuthError(state) {
      state.error = null
    },
  },
  extraReducers: builder => {
    builder
      .addCase(loginThunk.pending, state => {
        state.status = 'loading'
        state.error = null
      })
      .addCase(loginThunk.fulfilled, (state, action) => {
        state.status = 'succeeded'
        state.user = action.payload
      })
      .addCase(loginThunk.rejected, (state, action) => {
        state.status = 'failed'
        state.error = action.payload
      })
      .addCase(registerThunk.pending, state => {
        state.status = 'loading'
        state.error = null
      })
      .addCase(registerThunk.fulfilled, state => {
        state.status = 'succeeded'
      })
      .addCase(registerThunk.rejected, (state, action) => {
        state.status = 'failed'
        state.error = action.payload
      })
      .addCase(logoutThunk.fulfilled, (state) => {
        state.user = null
        state.status = 'idle'
        state.error = null
      })
      .addCase(checkAuthThunk.fulfilled, (state, action) => {
        state.user = action.payload
        state.status = 'succeeded'
      })
      .addCase(checkAuthThunk.rejected, (state) => {
        state.user = null
        state.status = 'failed'
      })
  },
})

export const { logout, clearAuthError } = authSlice.actions
export default authSlice.reducer

export const selectUser = state => state.auth.user
export const selectIsLoggedIn = state => !!state.auth.user
export const selectAuthStatus = state => state.auth.status
export const selectAuthError = state => state.auth.error
