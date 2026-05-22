<template>
  <div>
    <div class="d-flex align-center justify-space-between mb-6">
      <h1 class="text-h5 font-weight-bold">Notifications</h1>
      <v-btn
        v-if="notificationStore.notifications.some(n => !n.read)"
        variant="outlined"
        prepend-icon="mdi-check-all"
        @click="notificationStore.markAllAsRead()"
      >
        Mark all as read
      </v-btn>
    </div>

    <v-card elevation="0" border>
      <LoadingState v-if="loading" />

      <EmptyState
        v-else-if="notificationStore.notifications.length === 0"
        icon="mdi-bell-outline"
        title="No notifications"
        subtitle="You're all caught up"
      />

      <v-list v-else lines="two" class="pa-2">
        <template v-for="(n, i) in notificationStore.notifications" :key="n.id">
          <NotificationItem :notification="n" @read="notificationStore.markAsRead" />
          <v-divider v-if="i < notificationStore.notifications.length - 1" class="my-1" />
        </template>
      </v-list>
    </v-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useNotificationStore } from '@/stores/notifications'
import NotificationItem from '@/components/notifications/NotificationItem.vue'
import LoadingState from '@/components/common/LoadingState.vue'
import EmptyState from '@/components/common/EmptyState.vue'

const notificationStore = useNotificationStore()
const loading = ref(false)

onMounted(async () => {
  loading.value = true
  try {
    await notificationStore.fetchNotifications()
  } finally {
    loading.value = false
  }
})
</script>
