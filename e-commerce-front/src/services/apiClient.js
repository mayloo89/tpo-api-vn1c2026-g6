const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080'

export const getStoredUser = () => {
  try {
    return JSON.parse(localStorage.getItem('user') || 'null')
  } catch {
    return null
  }
}

export const getAuthToken = () => getStoredUser()?.token || null

export const apiRequest = async (path, options = {}) => {
  const token = getAuthToken()
  const headers = {
    'Content-Type': 'application/json',
    ...(options.headers || {}),
  }

  if (token) {
    headers.Authorization = `Bearer ${token}`
  }

  const response = await fetch(`${API_BASE_URL}${path}`, {
    ...options,
    headers,
  })

  if (!response.ok) {
    const errorText = await response.text()
    throw new Error(errorText || `Request failed with status ${response.status}`)
  }

  const contentType = response.headers.get('content-type') || ''
  if (contentType.includes('application/json')) {
    return response.json()
  }

  return response.text()
}