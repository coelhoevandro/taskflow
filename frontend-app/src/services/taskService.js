import api from './api'

export const taskService = {
  list(params = {}) {
    return api.get('/tasks', { params })
  },
  getById(id) {
    return api.get(`/tasks/${id}`)
  },
  create(data) {
    return api.post('/tasks', data)
  },
  update(id, data) {
    return api.put(`/tasks/${id}`, data)
  },
  assign(id, assigneeId) {
    return api.post(`/tasks/${id}/assign`, { assigneeId })
  },
  addComment(id, content, authorId) {
    return api.post(`/tasks/${id}/comments`, { content, authorId })
  },
  getStats() {
    return api.get('/tasks/stats')
  },
  getOverdue() {
    return api.get('/tasks/overdue')
  },
}
