<template>
  <v-card hover class="task-card" :to="{ name: 'task-detail', params: { id: task.id } }">
    <v-card-text class="pa-5">

      <!-- Priority + Status -->
      <div class="d-flex align-center gap-3 mb-4">
        <TaskPriorityChip :priority="task.priority" />
        <TaskStatusBadge :status="task.status" />
        <v-chip v-if="task.overdue" color="error" size="small" variant="flat" prepend-icon="mdi-clock-alert">
          Overdue
        </v-chip>
      </div>

      <!-- Title -->
      <p class="text-body-1 font-weight-medium mb-3" style="line-height: 1.4;">
        {{ task.title }}
      </p>

      <!-- Assignee + due date -->
      <div class="d-flex align-center justify-space-between mt-4">
        <div class="d-flex align-center gap-2">
          <UserAvatar v-if="assigneeName" :name="assigneeName" :size="24" />
          <span class="text-caption text-medium-emphasis">
            {{ assigneeName ?? 'Unassigned' }}
          </span>
        </div>
        <span v-if="task.dueDate" class="text-caption text-medium-emphasis">
          <v-icon size="14" class="mr-1">mdi-calendar-outline</v-icon>
          {{ formatDate(task.dueDate) }}
        </span>
      </div>

      <!-- Quick actions — botões contextuais por status -->
      <div
        class="d-flex justify-end gap-2 mt-4 pt-4 quick-actions"
        @click.prevent.stop
      >
        <v-btn
          v-for="action in transitions"
          :key="action.status"
          size="x-small"
          variant="tonal"
          :color="action.color"
          :prepend-icon="action.icon"
          :loading="saving === action.status"
          :disabled="saving !== null"
          @click.prevent.stop="changeStatus(action.status)"
        >
          {{ action.label }}
        </v-btn>
      </div>

    </v-card-text>
  </v-card>
</template>

<script setup>
import { ref, computed } from 'vue'
import TaskPriorityChip from '@/components/common/TaskPriorityChip.vue'
import TaskStatusBadge from '@/components/common/TaskStatusBadge.vue'
import UserAvatar from '@/components/common/UserAvatar.vue'
import { useFormatters } from '@/composables/useFormatters'
import { useTaskStore } from '@/stores/tasks'

const props = defineProps({
  task: { type: Object, required: true },
  assigneeName: { type: String, default: null },
})

const { formatDate } = useFormatters()
const taskStore = useTaskStore()

const saving = ref(null)

const TRANSITIONS = {
  TODO:        [
    { status: 'IN_PROGRESS', label: 'Start',       icon: 'mdi-play',    color: 'info' },
    { status: 'DONE',        label: 'Done',         icon: 'mdi-check',   color: 'success' },
    { status: 'CANCELLED',   label: 'Cancel',       icon: 'mdi-close',   color: 'error' },
  ],
  IN_PROGRESS: [
    { status: 'IN_REVIEW',   label: 'Review',       icon: 'mdi-eye',     color: 'warning' },
    { status: 'DONE',        label: 'Done',         icon: 'mdi-check',   color: 'success' },
    { status: 'CANCELLED',   label: 'Cancel',       icon: 'mdi-close',   color: 'error' },
  ],
  IN_REVIEW:   [
    { status: 'IN_PROGRESS', label: 'In Progress',  icon: 'mdi-play',    color: 'info' },
    { status: 'DONE',        label: 'Done',         icon: 'mdi-check',   color: 'success' },
    { status: 'CANCELLED',   label: 'Cancel',       icon: 'mdi-close',   color: 'error' },
  ],
  DONE:        [
    { status: 'TODO',        label: 'Reopen',       icon: 'mdi-refresh', color: 'secondary' },
  ],
  CANCELLED:   [
    { status: 'TODO',        label: 'Reopen',       icon: 'mdi-refresh', color: 'secondary' },
  ],
}

const transitions = computed(() => TRANSITIONS[props.task.status] ?? [])

async function changeStatus(newStatus) {
  saving.value = newStatus
  try {
    await taskStore.updateTask(props.task.id, {
      title: props.task.title,
      description: props.task.description,
      priority: props.task.priority,
      dueDate: props.task.dueDate,
      status: newStatus,
    })
  } finally {
    saving.value = null
  }
}
</script>

<style scoped>
.task-card {
  transition: box-shadow 0.15s ease;
}
.task-card:hover {
  box-shadow: 0 4px 16px rgba(59, 91, 219, 0.12) !important;
}
.quick-actions {
  border-top: 1px solid rgba(0, 0, 0, 0.06);
}
.v-btn.v-btn--density-default {
  margin-left: 5px;
}
</style>
