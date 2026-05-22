<template>
  <v-app style="background: #F4F6FA;">
    <v-main>
      <v-container fluid class="fill-height">
        <v-row justify="center" align="center" class="fill-height">
          <v-col cols="12" sm="8" md="5" lg="4">
            <!-- Brand -->
            <div class="d-flex align-center justify-center gap-3 mb-8">
              <v-icon color="primary" size="36">mdi-check-circle</v-icon>
              <span class="text-h5 font-weight-bold text-primary">TaskFlow</span>
            </div>

            <v-card elevation="0" border class="pa-2">
              <v-card-text class="pa-6">
                <p class="text-h6 font-weight-semibold mb-1">Sign in</p>
                <p class="text-body-2 text-medium-emphasis mb-6">Enter your credentials to continue</p>

                <v-form @submit.prevent="handleLogin" :disabled="loading">
                  <v-text-field
                    v-model="email"
                    label="Email"
                    type="email"
                    prepend-inner-icon="mdi-email-outline"
                    autocomplete="email"
                    class="mb-3"
                    :error-messages="errors.email"
                  />
                  <v-text-field
                    v-model="password"
                    label="Password"
                    :type="showPassword ? 'text' : 'password'"
                    prepend-inner-icon="mdi-lock-outline"
                    :append-inner-icon="showPassword ? 'mdi-eye-off' : 'mdi-eye'"
                    @click:append-inner="showPassword = !showPassword"
                    autocomplete="current-password"
                    class="mb-4"
                    :error-messages="errors.password"
                  />

                  <v-alert v-if="errorMsg" type="error" variant="tonal" density="compact" class="mb-4">
                    {{ errorMsg }}
                  </v-alert>

                  <v-btn
                    type="submit"
                    color="primary"
                    size="large"
                    block
                    :loading="loading"
                  >
                    Sign in
                  </v-btn>
                </v-form>
              </v-card-text>
            </v-card>

            <!-- Demo accounts hint -->
            <v-card elevation="0" border class="mt-4 pa-4">
              <p class="text-caption text-medium-emphasis font-weight-medium mb-2">Demo accounts</p>
              <div v-for="account in demoAccounts" :key="account.email" class="mb-1">
                <v-btn
                  variant="text"
                  size="small"
                  density="compact"
                  color="primary"
                  class="text-caption"
                  @click="fillDemo(account)"
                >
                  {{ account.label }}
                </v-btn>
                <span class="text-caption text-disabled ml-1">{{ account.email }}</span>
              </div>
            </v-card>
          </v-col>
        </v-row>
      </v-container>
    </v-main>
  </v-app>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const router = useRouter()
const route = useRoute()
const auth = useAuthStore()

const email = ref('')
const password = ref('')
const showPassword = ref(false)
const loading = ref(false)
const errorMsg = ref('')
const errors = reactive({ email: '', password: '' })

const demoAccounts = [
  { label: 'Admin', email: 'admin@taskflow.dev', password: 'admin123' },
  { label: 'Sarah (PM)', email: 'sarah.pm@taskflow.dev', password: 'user123' },
  { label: 'Carlos (Dev)', email: 'carlos.dev@taskflow.dev', password: 'user123' },
  { label: 'Julia (QA)', email: 'julia.qa@taskflow.dev', password: 'user123' },
]

function fillDemo(account) {
  email.value = account.email
  password.value = account.password
}

async function handleLogin() {
  errorMsg.value = ''
  if (!email.value) { errors.email = 'Required'; return }
  if (!password.value) { errors.password = 'Required'; return }

  loading.value = true
  try {
    await auth.login(email.value, password.value)
    const redirect = route.query.redirect || '/dashboard'
    router.push(redirect)
  } catch (err) {
    errorMsg.value = err.response?.data?.message ?? 'Login failed. Check your credentials.'
  } finally {
    loading.value = false
  }
}
</script>
