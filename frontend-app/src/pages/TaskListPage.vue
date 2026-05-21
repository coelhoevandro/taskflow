<template>
  <div>
    <!-- Header -->
    <div class="d-flex align-center justify-space-between mb-6">
      <h1 class="text-h5 font-weight-bold">Tasks</h1>
      <v-btn color="primary" prepend-icon="mdi-plus" :to="{ name: 'task-create' }">
        New Task
      </v-btn>
    </div>

    <!-- Filters -->
    <v-card elevation="0" border class="mb-4">
      <v-card-text class="pa-4">
        <v-row dense>
          <v-col cols="12" sm="4">
            <v-select
              v-model="filters.status"
              label="Status"
              :items="statusOptions"
              item-title="label"
              item-value="value"
              clearable
              @update:model-value="applyFilters"
            />
          </v-col>
          <v-col cols="12" sm="4">
            <v-select
              v-model="filters.assigneeId"
              label="Assignee"
              :items="users"
              item-title="name"
              item-value="id"
              clearable
              @update:model-value="applyFilters"
            />
          </v-col>
          <v-col cols="12" sm="4" class="d-flex align-center">
            <v-btn variant="text" prepend-icon="mdi-filter-off" @click="clearFilters">
              Clear filters
            </v-btn>
          </v-col>
        </v-row>
      </v-card-text>
    </v-card>

    <!-- Task list -->
    <LoadingState v-if="taskStore.loading" />

    <template v-else>
      <EmptyState
        v-if="taskStore.tasks.length === 0"
        icon="mdi-check-all"
        title="No tasks found"
        subtitle="Try adjusting your filters or create a new task"
      >
        <v-btn color="primary" class="mt-4" :to="{ name: 'task-create' }">Create task</v-btn>
      </EmptyState>

      <div v-else class="d-flex flex-column gap-3 mb-4">
        <TaskCard
          v-for="task in taskStore.tasks"
          :key="task.id"
          :task="task"
          :assignee-name="getUserName(task.assigneeId)"
        />
      </div>

      <!-- Pagination -->
      <div v-if="taskStore.pagination.totalPages > 1" class="d-flex justify-center">
        <v-pagination
          v-model="page"
          :length="taskStore.pagination.totalPages"
          @update:model-value="loadPage"
        />
      </div>
    </template>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useTaskStore } from '@/stores/tasks'
import { useUsers } from '@/composables/useUsers'
import TaskCard from '@/components/tasks/TaskCard.vue'
import LoadingState from '@/components/common/LoadingState.vue'
import EmptyState from '@/components/common/EmptyState.vue'

const taskStore = useTaskStore()
const { users, getUserName } = useUsers()

const page = ref(1)
const filters = reactive({ status: null, assigneeId: null })

const statusOptions = [
  { label: 'To Do',       value: 'TODO' },
  { label: 'In Progress', value: 'IN_PROGRESS' },
  { label: 'In Review',   value: 'IN_REVIEW' },
  { label: 'Done',        value: 'DONE' },
  { label: 'Cancelled',   value: 'CANCELLED' },
]

function buildParams() {
  return {
    page: page.value - 1,
    size: 20,
    ...(filters.status ? { status: filters.status } : {}),
    ...(filters.assigneeId ? { assigneeId: filters.assigneeId } : {}),
  }
}

async function applyFilters() {
  page.value = 1
  await taskStore.fetchTasks(buildParams())
}

async function loadPage(p) {
  page.value = p
  await taskStore.fetchTasks(buildParams())
}

function clearFilters() {
  filters.status = null
  filters.assigneeId = null
  applyFilters()
}

onMounted(() => taskStore.fetchTasks(buildParams()))
</script>
