import { defineStore } from 'pinia'
import { ref } from 'vue'
import { notificationService } from '@/services/notificationService'
import { useAuthStore } from './auth'

export const useNotificationStore = defineStore('notifications', () => {
  const notifications = ref([])
  const unreadCount = ref(0)

  async function fetchNotifications() {
    const auth = useAuthStore()
    if (!auth.user?.id) return
    const { data } = await notificationService.getForUser(auth.user.id)
    notifications.value = data
  }

  async function fetchUnreadCount() {
    const auth = useAuthStore()
    if (!auth.user?.id) return
    const { data } = await notificationService.getUnreadCount(auth.user.id)
    unreadCount.value = data.count
  }

  async function markAsRead(id) {
    await notificationService.markAsRead(id)
    const n = notifications.value.find(n => n.id === id)
    if (n) {
      n.read = true
      unreadCount.value = Math.max(0, unreadCount.value - 1)
    }
  }

  async function markAllAsRead() {
    const auth = useAuthStore()
    await notificationService.markAllAsRead(auth.user.id)
    notifications.value.forEach(n => n.read = true)
    unreadCount.value = 0
  }

  return { notifications, unreadCount, fetchNotifications, fetchUnreadCount, markAsRead, markAllAsRead }
})
