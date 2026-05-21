import api from './api'

export const authService = {
  login(email, password) {
    return api.post('/auth/login', { email, password })
  },
  register(name, email, password) {
    return api.post('/auth/register', { name, email, password })
  },
  refresh(refreshToken) {
    return api.post('/auth/refresh', { refreshToken })
  },
}
