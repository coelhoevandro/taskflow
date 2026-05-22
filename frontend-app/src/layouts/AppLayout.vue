<template>
  <v-layout class="fill-height">
    <!-- Sidebar -->
    <v-navigation-drawer v-model="drawer" :rail="rail" permanent color="surface" border="end">
      <!-- Brand -->
      <div class="d-flex align-center px-4 py-5 gap-3">
        <v-icon color="primary" size="28">mdi-check-circle</v-icon>
        <span v-if="!rail" class="text-h6 font-weight-bold text-primary">TaskFlow</span>
      </div>

      <v-divider />

      <v-list nav density="compact" class="mt-2">
        <v-list-item
          v-for="item in navItems"
          :key="item.to"
          :to="item.to"
          :prepend-icon="item.icon"
          :title="item.label"
          rounded="lg"
          active-color="primary"
          class="mb-1"
        />
      </v-list>

      <template #append>
        <v-divider />
        <div class="pa-3">
          <v-list-item
            :prepend-avatar="avatarUrl"
            :title="auth.user?.email"
            nav
          >
            <template #append>
              <v-btn icon="mdi-logout" variant="text" size="small" @click="auth.logout" />
            </template>
          </v-list-item>
        </div>
      </template>
    </v-navigation-drawer>

    <!-- Main content -->
    <v-main style="background: #F4F6FA;">
      <!-- Top bar -->
      <v-app-bar flat color="surface" border="bottom" height="64">
        <v-btn :icon="rail ? 'mdi-menu-open' : 'mdi-menu'" variant="text" @click="rail = !rail" />
        <v-app-bar-title>
          <span class="text-body-1 font-weight-medium text-medium-emphasis">
            {{ currentPageTitle }}
          </span>
        </v-app-bar-title>

        <template #append>
          <!-- Notification bell -->
          <v-btn icon variant="text" :to="{ name: 'notifications' }" class="mr-1">
            <v-badge
              v-if="notificationStore.unreadCount > 0"
              :content="notificationStore.unreadCount"
              color="error"
            >
              <v-icon>mdi-bell-outline</v-icon>
            </v-badge>
            <v-icon v-else>mdi-bell-outline</v-icon>
          </v-btn>
        </template>
      </v-app-bar>

      <v-container fluid class="pa-6">
        <router-view />
      </v-container>
    </v-main>
  </v-layout>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { useNotificationStore } from '@/stores/notifications'

const auth = useAuthStore()
const notificationStore = useNotificationStore()
const route = useRoute()

const drawer = ref(true)
const rail = ref(false)

const navItems = [
  { to: '/dashboard', icon: 'mdi-view-dashboard-outline', label: 'Dashboard' },
  { to: '/tasks', icon: 'mdi-check-all', label: 'Tasks' },
  { to: '/notifications', icon: 'mdi-bell-outline', label: 'Notifications' },
  { to: '/profile', icon: 'mdi-account-outline', label: 'Profile' },
]

const pageTitles = {
  dashboard: 'Dashboard',
  tasks: 'Tasks',
  'task-create': 'New Task',
  'task-detail': 'Task Details',
  'task-edit': 'Edit Task',
  notifications: 'Notifications',
  profile: 'Profile',
}

const currentPageTitle = computed(() => pageTitles[route.name] ?? 'TaskFlow')

const avatarUrl = computed(() => {
  const email = auth.user?.email ?? ''
  return `https://ui-avatars.com/api/?name=${encodeURIComponent(email)}&background=3B5BDB&color=fff&size=32`
})

onMounted(() => {
  notificationStore.fetchUnreadCount()
})
</script>
