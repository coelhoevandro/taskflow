<template>
  <div>
    <div class="d-flex align-center justify-space-between mb-6">
      <div>
        <h1 class="text-h5 font-weight-bold">Dashboard</h1>
        <p class="text-body-2 text-medium-emphasis mt-1">
          Good {{ greeting }}, {{ firstName }}
        </p>
      </div>
      <v-btn color="primary" prepend-icon="mdi-plus" :to="{ name: 'task-create' }">
        New Task
      </v-btn>
    </div>

    <!-- Stats cards -->
    <v-row class="mb-6">
      <v-col v-for="stat in statsCards" :key="stat.label" cols="12" sm="6" lg="3">
        <v-card elevation="0" border>
          <v-card-text class="pa-5">
            <div class="d-flex align-center justify-space-between mb-3">
              <span class="text-body-2 text-medium-emphasis">{{ stat.label }}</span>
              <v-avatar :color="stat.color" size="36" variant="tonal">
                <v-icon size="20">{{ stat.icon }}</v-icon>
              </v-avatar>
            </div>
            <p class="text-h4 font-weight-bold mb-0">{{ stat.value }}</p>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>

    <v-row>
      <!-- Recent tasks -->
      <v-col cols="12" md="7">
        <v-card elevation="0" border>
          <v-card-text class="pa-5">
            <div class="d-flex align-center justify-space-between mb-5">
              <p class="text-body-1 font-weight-semibold mb-0">Recent Tasks</p>
              <v-btn variant="text" size="small" :to="{ name: 'tasks' }">View all</v-btn>
            </div>

            <LoadingState v-if="taskStore.loading" />

            <div v-else-if="taskStore.tasks.length === 0">
              <EmptyState icon="mdi-check-all" title="No tasks yet" subtitle="Create your first task to get started" />
            </div>

            <div v-else class="d-flex flex-column gap-3">
              <TaskCard
                v-for="task in taskStore.tasks.slice(0, 5)"
                :key="task.id"
                :task="task"
                :assignee-name="getUserName(task.assigneeId)"
              />
            </div>
          </v-card-text>
        </v-card>
      </v-col>

      <!-- Right column -->
      <v-col cols="12" md="5">
        <!-- Overdue tasks -->
        <v-card elevation="0" border class="mb-4">
          <v-card-text class="pa-5">
            <div class="d-flex align-center gap-3 mb-5">
              <v-icon color="error" size="20">mdi-clock-alert-outline</v-icon>
              <p class="text-body-1 font-weight-semibold mb-0">Overdue</p>
              <v-chip v-if="taskStore.overdue.length" color="error" size="x-small" variant="flat">
                {{ taskStore.overdue.length }}
              </v-chip>
            </div>

            <EmptyState
              v-if="taskStore.overdue.length === 0"
              icon="mdi-check-circle-outline"
              title="No overdue tasks"
              subtitle="Great job staying on top of things"
            />

            <div v-else class="d-flex flex-column gap-1">
              <v-list-item
                v-for="task in taskStore.overdue.slice(0, 4)"
                :key="task.id"
                :to="{ name: 'task-detail', params: { id: task.id } }"
                rounded="lg"
                class="px-3 py-2"
              >
                <v-list-item-title class="text-body-2">{{ task.title }}</v-list-item-title>
                <template #append>
                  <span class="text-caption text-error">{{ formatDate(task.dueDate) }}</span>
                </template>
              </v-list-item>
            </div>
          </v-card-text>
        </v-card>

        <!-- Recent notifications -->
        <v-card elevation="0" border>
          <v-card-text class="pa-5">
            <div class="d-flex align-center justify-space-between mb-4">
              <p class="text-body-1 font-weight-semibold mb-0">Notifications</p>
              <v-btn variant="text" size="small" :to="{ name: 'notifications' }">View all</v-btn>
            </div>

            <EmptyState
              v-if="notificationStore.notifications.length === 0"
              icon="mdi-bell-outline"
              title="No notifications"
            />

            <div v-else class="d-flex flex-column gap-1">
              <NotificationItem
                v-for="n in notificationStore.notifications.slice(0, 4)"
                :key="n.id"
                :notification="n"
                @read="notificationStore.markAsRead"
              />
            </div>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </div>
</template>

<script setup>
import { computed, onMounted } from 'vue'
import { useAuthStore } from '@/stores/auth'
import { useTaskStore } from '@/stores/tasks'
import { useNotificationStore } from '@/stores/notifications'
import { useUsers } from '@/composables/useUsers'
import { useFormatters } from '@/composables/useFormatters'
import TaskCard from '@/components/tasks/TaskCard.vue'
import NotificationItem from '@/components/notifications/NotificationItem.vue'
import LoadingState from '@/components/common/LoadingState.vue'
import EmptyState from '@/components/common/EmptyState.vue'

const auth = useAuthStore()
const taskStore = useTaskStore()
const notificationStore = useNotificationStore()
const { getUserName } = useUsers()
const { formatDate } = useFormatters()

const hour = new Date().getHours()
const greeting = hour < 12 ? 'morning' : hour < 18 ? 'afternoon' : 'evening'
const firstName = computed(() => auth.user?.email?.split('@')[0] ?? 'there')

const statsCards = computed(() => {
  const s = taskStore.stats
  if (!s) return []
  return [
    { label: 'Total Tasks',  value: s.totalTasks,  icon: 'mdi-format-list-checks',   color: 'primary' },
    { label: 'In Progress',  value: s.inProgress,  icon: 'mdi-progress-clock',       color: 'info' },
    { label: 'In Review',    value: s.inReview,    icon: 'mdi-eye-check-outline',    color: 'warning' },
    { label: 'Overdue',      value: s.overdue,     icon: 'mdi-clock-alert-outline',  color: 'error' },
  ]
})

onMounted(async () => {
  await Promise.all([
    taskStore.fetchTasks({ size: 10 }),
    taskStore.fetchStats(),
    taskStore.fetchOverdue(),
    notificationStore.fetchNotifications(),
  ])
})
</script>
