import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { authService } from '@/services/authService'
import router from '@/router'

export const useAuthStore = defineStore('auth', () => {
  const user = ref(null)
  const accessToken = ref(null)
  const refreshToken = ref(null)

  const isAuthenticated = computed(() => !!accessToken.value)

  function initFromStorage() {
    const storedUser = localStorage.getItem('user')
    const storedToken = localStorage.getItem('accessToken')
    if (storedUser && storedToken) {
      user.value = JSON.parse(storedUser)
      accessToken.value = storedToken
      refreshToken.value = localStorage.getItem('refreshToken')
    }
  }

  async function login(email, password) {
    const { data } = await authService.login(email, password)
    setSession(data)
  }

  function setSession(data) {
    user.value = { id: data.userId, email: data.email, role: data.role }
    accessToken.value = data.accessToken
    refreshToken.value = data.refreshToken

    localStorage.setItem('user', JSON.stringify(user.value))
    localStorage.setItem('accessToken', data.accessToken)
    localStorage.setItem('refreshToken', data.refreshToken)
  }

  function logout() {
    user.value = null
    accessToken.value = null
    refreshToken.value = null
    localStorage.removeItem('user')
    localStorage.removeItem('accessToken')
    localStorage.removeItem('refreshToken')
    router.push('/login')
  }

  return { user, accessToken, isAuthenticated, initFromStorage, login, logout, setSession }
})
