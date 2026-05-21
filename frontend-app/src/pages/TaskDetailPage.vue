<template>
  <div v-if="taskStore.currentTask">
    <!-- Breadcrumb + actions -->
    <div class="d-flex align-center justify-space-between mb-6">
      <v-breadcrumbs :items="breadcrumbs" density="compact" class="pa-0" />
      <div class="d-flex gap-2">
        <v-btn variant="outlined" prepend-icon="mdi-pencil"
          :to="{ name: 'task-edit', params: { id: task.id } }">
          Edit
        </v-btn>
      </div>
    </div>

    <v-row>
      <!-- Main content -->
      <v-col cols="12" md="8">
        <v-card elevation="0" border class="mb-4">
          <v-card-text class="pa-6">
            <!-- Status + Priority row -->
            <div class="d-flex align-center gap-2 mb-4">
              <TaskStatusBadge :status="task.status" />
              <TaskPriorityChip :priority="task.priority" />
              <v-chip v-if="task.overdue" color="error" size="small" variant="flat">
                Overdue
              </v-chip>
            </div>

            <h2 class="text-h5 font-weight-bold mb-3">{{ task.title }}</h2>
            <p v-if="task.description" class="text-body-1 text-medium-emphasis" style="white-space: pre-wrap;">
              {{ task.description }}
            </p>
            <p v-else class="text-body-2 text-disabled font-italic">No description</p>
          </v-card-text>
        </v-card>

        <!-- Comments -->
        <v-card elevation="0" border>
          <v-card-text class="pa-6">
            <p class="text-body-1 font-weight-semibold mb-4">
              Comments ({{ task.comments?.length ?? 0 }})
            </p>

            <div v-if="task.comments?.length === 0" class="mb-4">
              <p class="text-body-2 text-disabled">No comments yet. Be the first to comment.</p>
            </div>

            <div v-else class="d-flex flex-column gap-4 mb-6">
              <div v-for="comment in task.comments" :key="comment.id" class="d-flex gap-3">
                <UserAvatar :name="getUserName(comment.authorId)" :size="32" class="flex-shrink-0 mt-1" />
                <div class="flex-grow-1">
                  <div class="d-flex align-center gap-2 mb-1">
                    <span class="text-body-2 font-weight-medium">{{ getUserName(comment.authorId) }}</span>
                    <span class="text-caption text-disabled">{{ timeAgo(comment.createdAt) }}</span>
                  </div>
                  <p class="text-body-2 mb-0" style="white-space: pre-wrap;">{{ comment.content }}</p>
                </div>
              </div>
            </div>

            <!-- Add comment -->
            <div class="d-flex gap-3 align-start">
              <UserAvatar :name="auth.user?.email ?? ''" :size="32" class="flex-shrink-0 mt-2" />
              <div class="flex-grow-1">
                <v-textarea
                  v-model="newComment"
                  placeholder="Write a comment..."
                  rows="2"
                  auto-grow
                  hide-details
                  class="mb-2"
                />
                <v-btn
                  color="primary"
                  size="small"
                  :loading="submittingComment"
                  :disabled="!newComment.trim()"
                  @click="submitComment"
                >
                  Comment
                </v-btn>
              </div>
            </div>
          </v-card-text>
        </v-card>
      </v-col>

      <!-- Sidebar -->
      <v-col cols="12" md="4">
        <v-card elevation="0" border class="mb-4">
          <v-card-text class="pa-5">
            <p class="text-body-2 font-weight-semibold text-medium-emphasis mb-4 text-uppercase" style="letter-spacing: .05em; font-size: 11px;">
              Details
            </p>

            <div class="d-flex flex-column gap-4">
              <div>
                <p class="text-caption text-disabled mb-1">Assignee</p>
                <div class="d-flex align-center gap-2">
                  <UserAvatar v-if="assigneeName" :name="assigneeName" :size="28" />
                  <span class="text-body-2">{{ assigneeName ?? 'Unassigned' }}</span>
                </div>
              </div>

              <div>
                <p class="text-caption text-disabled mb-1">Reporter</p>
                <div class="d-flex align-center gap-2">
                  <UserAvatar :name="reporterName" :size="28" />
                  <span class="text-body-2">{{ reporterName }}</span>
                </div>
              </div>

              <div>
                <p class="text-caption text-disabled mb-1">Due date</p>
                <span class="text-body-2">{{ formatDate(task.dueDate) }}</span>
              </div>

              <div>
                <p class="text-caption text-disabled mb-1">Created</p>
                <span class="text-body-2">{{ formatDateTime(task.createdAt) }}</span>
              </div>
            </div>
          </v-card-text>
        </v-card>

        <!-- Assign section -->
        <v-card elevation="0" border>
          <v-card-text class="pa-5">
            <p class="text-body-2 font-weight-semibold mb-3">Assign task</p>
            <v-select
              v-model="selectedAssignee"
              :items="users"
              item-title="name"
              item-value="id"
              label="Select assignee"
              density="compact"
              class="mb-3"
            />
            <v-btn
              color="primary"
              block
              :loading="assigning"
              :disabled="!selectedAssignee"
              @click="handleAssign"
            >
              Assign
            </v-btn>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </div>

  <LoadingState v-else-if="taskStore.loading" />
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { useTaskStore } from '@/stores/tasks'
import { useUsers } from '@/composables/useUsers'
import { useFormatters } from '@/composables/useFormatters'
import TaskStatusBadge from '@/components/common/TaskStatusBadge.vue'
import TaskPriorityChip from '@/components/common/TaskPriorityChip.vue'
import UserAvatar from '@/components/common/UserAvatar.vue'
import LoadingState from '@/components/common/LoadingState.vue'

const route = useRoute()
const auth = useAuthStore()
const taskStore = useTaskStore()
const { users, getUserName } = useUsers()
const { formatDate, formatDateTime, timeAgo } = useFormatters()

const newComment = ref('')
const submittingComment = ref(false)
const selectedAssignee = ref(null)
const assigning = ref(false)

const task = computed(() => taskStore.currentTask)
const assigneeName = computed(() => getUserName(task.value?.assigneeId))
const reporterName = computed(() => getUserName(task.value?.reporterId))

const breadcrumbs = computed(() => [
  { title: 'Tasks', to: '/tasks' },
  { title: task.value?.title ?? '...', disabled: true },
])

async function submitComment() {
  if (!newComment.value.trim()) return
  submittingComment.value = true
  try {
    await taskStore.addComment(task.value.id, newComment.value, auth.user.id)
    newComment.value = ''
  } finally {
    submittingComment.value = false
  }
}

async function handleAssign() {
  if (!selectedAssignee.value) return
  assigning.value = true
  try {
    await taskStore.assignTask(task.value.id, selectedAssignee.value)
    selectedAssignee.value = null
  } finally {
    assigning.value = false
  }
}

onMounted(() => taskStore.fetchTask(route.params.id))
</script>
