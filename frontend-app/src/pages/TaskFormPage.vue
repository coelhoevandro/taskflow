<template>
  <div>
    <div class="d-flex align-center gap-3 mb-6">
      <v-btn icon="mdi-arrow-left" variant="text" @click="router.back()" />
      <h1 class="text-h5 font-weight-bold">{{ isEdit ? 'Edit Task' : 'New Task' }}</h1>
    </div>

    <v-row justify="center">
      <v-col cols="12" md="8" lg="6">
        <v-card elevation="0" border>
          <v-card-text class="pa-6">
            <v-form @submit.prevent="handleSubmit" :disabled="loading">
              <v-text-field
                v-model="form.title"
                label="Title"
                placeholder="What needs to be done?"
                :error-messages="errors.title"
                class="mb-5"
              />

              <v-textarea
                v-model="form.description"
                label="Description"
                placeholder="Add more context..."
                rows="4"
                auto-grow
                class="mb-5"
              />

              <v-row>
                <v-col cols="12" sm="6">
                  <v-select
                    v-model="form.priority"
                    label="Priority"
                    :items="priorityOptions"
                    item-title="label"
                    item-value="value"
                    class="mb-2"
                  />
                </v-col>
                <v-col cols="12" sm="6">
                  <v-select
                    v-if="isEdit"
                    v-model="form.status"
                    label="Status"
                    :items="statusOptions"
                    item-title="label"
                    item-value="value"
                    class="mb-2"
                  />
                </v-col>
              </v-row>

              <v-text-field
                v-model="form.dueDate"
                label="Due date"
                type="date"
                class="mb-7"
              />

              <div class="d-flex gap-3">
                <v-btn variant="outlined" @click="router.back()">Cancel</v-btn>
                <v-btn type="submit" color="primary" :loading="loading">
                  {{ isEdit ? 'Save changes' : 'Create task' }}
                </v-btn>
              </div>
            </v-form>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { useTaskStore } from '@/stores/tasks'

const route = useRoute()
const router = useRouter()
const auth = useAuthStore()
const taskStore = useTaskStore()

const isEdit = computed(() => !!route.params.id)
const loading = ref(false)
const errors = reactive({ title: '' })

const form = reactive({
  title: '',
  description: '',
  priority: 'MEDIUM',
  status: 'TODO',
  dueDate: '',
})

const priorityOptions = [
  { label: 'Low', value: 'LOW' },
  { label: 'Medium', value: 'MEDIUM' },
  { label: 'High', value: 'HIGH' },
  { label: 'Critical', value: 'CRITICAL' },
]

const statusOptions = [
  { label: 'To Do', value: 'TODO' },
  { label: 'In Progress', value: 'IN_PROGRESS' },
  { label: 'In Review', value: 'IN_REVIEW' },
  { label: 'Done', value: 'DONE' },
  { label: 'Cancelled', value: 'CANCELLED' },
]

async function handleSubmit() {
  errors.title = ''
  if (!form.title.trim()) {
    errors.title = 'Title is required'
    return
  }

  loading.value = true
  try {
    const payload = {
      title: form.title,
      description: form.description || null,
      priority: form.priority,
      dueDate: form.dueDate || null,
      ...(isEdit.value ? { status: form.status } : { reporterId: auth.user.id }),
    }

    if (isEdit.value) {
      await taskStore.updateTask(route.params.id, payload)
      router.push({ name: 'task-detail', params: { id: route.params.id } })
    } else {
      const created = await taskStore.createTask(payload)
      router.push({ name: 'task-detail', params: { id: created.id } })
    }
  } finally {
    loading.value = false
  }
}

onMounted(async () => {
  if (isEdit.value) {
    const task = await taskStore.fetchTask(route.params.id)
    form.title = task.title
    form.description = task.description ?? ''
    form.priority = task.priority
    form.status = task.status
    form.dueDate = task.dueDate ?? ''
  }
})
</script>
