import { createSlice } from '@reduxjs/toolkit'

const INACTIVITY_TIMEOUT_MS = 30 * 60 * 1000

export function recordUserActivity() {
  localStorage.setItem('roomchef_last_activity', String(Date.now()))
}

export function isTokenExpired(token) {
  if (!token) return true
  try {
    const lastActivity = Number(localStorage.getItem('roomchef_last_activity') || 0)
    if (lastActivity > 0 && Date.now() - lastActivity >= INACTIVITY_TIMEOUT_MS) {
      return true
    }
    const parts = token.split('.')
    if (parts.length < 2) return true
    const base64Url = parts[1]
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/')
    const jsonPayload = decodeURIComponent(
      atob(base64)
        .split('')
        .map((c) => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
        .join('')
    )
    const payload = JSON.parse(jsonPayload)
    if (!payload.exp) return false
    return Date.now() >= payload.exp * 1000
  } catch {
    return true
  }
}

const savedUser = (() => {
  try {
    const token = localStorage.getItem('roomchef_token')
    if (!token || isTokenExpired(token)) {
      localStorage.removeItem('roomchef_token')
      localStorage.removeItem('roomchef_user')
      return null
    }
    const userStr = localStorage.getItem('roomchef_user')
    return userStr ? JSON.parse(userStr) : null
  } catch {
    return null
  }
})()

const initialState = {
  user: savedUser,
  token: localStorage.getItem('roomchef_token') || null,
  isAuthenticated: !!savedUser,
}

const authSlice = createSlice({
  name: 'auth',
  initialState,
  reducers: {
    setCredentials(state, action) {
      const { user, token } = action.payload
      state.user = user
      state.token = token
      state.isAuthenticated = true
      localStorage.setItem('roomchef_token', token)
      localStorage.setItem('roomchef_user', JSON.stringify(user))
      recordUserActivity()
    },
    logout(state) {
      state.user = null
      state.token = null
      state.isAuthenticated = false
      localStorage.removeItem('roomchef_token')
      localStorage.removeItem('roomchef_user')
      localStorage.removeItem('roomchef_last_activity')
    },
  },
})

export const { setCredentials, logout } = authSlice.actions
export default authSlice.reducer
