import api from './api'

export const notificationService = {
  getForUser(userId) {
    return api.get(`/notifications/user/${userId}`)
  },
  getUnread(userId) {
    return api.get(`/notifications/user/${userId}/unread`)
  },
  getUnreadCount(userId) {
    return api.get(`/notifications/user/${userId}/unread-count`)
  },
  markAsRead(id) {
    return api.patch(`/notifications/${id}/read`)
  },
  markAllAsRead(userId) {
    return api.post(`/notifications/user/${userId}/read-all`)
  },
}
