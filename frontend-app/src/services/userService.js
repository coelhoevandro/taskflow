import api from './api'

export const userService = {
  list() {
    return api.get('/users')
  },
  getById(id) {
    return api.get(`/users/${id}`)
  },
  update(id, data) {
    return api.put(`/users/${id}`, data)
  },
}
