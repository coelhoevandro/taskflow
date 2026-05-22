import { ref, onMounted } from 'vue'
import { userService } from '@/services/userService'

// Shared composable so multiple components don't each fetch the user list separately
export function useUsers() {
  const users = ref([])
  const loading = ref(false)

  async function fetchUsers() {
    loading.value = true
    try {
      const { data } = await userService.list()
      users.value = data
    } finally {
      loading.value = false
    }
  }

  function getUserById(id) {
    return users.value.find(u => u.id === id) ?? null
  }

  function getUserName(id) {
    return getUserById(id)?.name ?? 'Unknown'
  }

  onMounted(fetchUsers)

  return { users, loading, getUserById, getUserName, fetchUsers }
}
