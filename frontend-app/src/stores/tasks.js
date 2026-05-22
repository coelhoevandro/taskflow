import { defineStore } from 'pinia'
import { ref } from 'vue'
import { taskService } from '@/services/taskService'

export const useTaskStore = defineStore('tasks', () => {
  const tasks = ref([])
  const currentTask = ref(null)
  const stats = ref(null)
  const overdue = ref([])
  const loading = ref(false)
  const pagination = ref({ page: 0, totalPages: 0, totalElements: 0 })

  async function fetchTasks(params = {}) {
    loading.value = true
    try {
      const { data } = await taskService.list(params)
      tasks.value = data.content
      pagination.value = {
        page: data.number,
        totalPages: data.totalPages,
        totalElements: data.totalElements,
      }
    } finally {
      loading.value = false
    }
  }

  async function fetchTask(id) {
    loading.value = true
    try {
      const { data } = await taskService.getById(id)
      currentTask.value = data
      return data
    } finally {
      loading.value = false
    }
  }

  async function createTask(payload) {
    const { data } = await taskService.create(payload)
    return data
  }

  async function updateTask(id, payload) {
    const { data } = await taskService.update(id, payload)
    if (currentTask.value?.id === id) currentTask.value = data
    // Atualiza inline na lista para o card refletir sem recarregar
    const idx = tasks.value.findIndex(t => t.id === id)
    if (idx !== -1) tasks.value[idx] = data
    return data
  }

  async function assignTask(id, assigneeId) {
    const { data } = await taskService.assign(id, assigneeId)
    if (currentTask.value?.id === id) currentTask.value = data
    return data
  }

  async function addComment(taskId, content, authorId) {
    const { data } = await taskService.addComment(taskId, content, authorId)
    if (currentTask.value?.id === taskId) {
      currentTask.value.comments.push(data)
    }
    return data
  }

  async function fetchStats() {
    const { data } = await taskService.getStats()
    stats.value = data
  }

  async function fetchOverdue() {
    const { data } = await taskService.getOverdue()
    overdue.value = data
  }

  return {
    tasks, currentTask, stats, overdue, loading, pagination,
    fetchTasks, fetchTask, createTask, updateTask, assignTask, addComment,
    fetchStats, fetchOverdue,
  }
})
