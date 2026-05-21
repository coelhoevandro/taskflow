<template>
  <div>
    <h1 class="text-h5 font-weight-bold mb-6">Profile</h1>

    <v-row justify="center">
      <v-col cols="12" md="7" lg="5">
        <v-card elevation="0" border class="mb-4">
          <v-card-text class="pa-6 text-center">
            <UserAvatar :name="profile?.name ?? auth.user?.email ?? ''" :size="80" class="mb-3" />
            <p class="text-h6 font-weight-bold mb-0">{{ profile?.name ?? '—' }}</p>
            <p class="text-body-2 text-medium-emphasis">{{ profile?.jobTitle ?? '' }}</p>
            <v-chip size="small" color="primary" variant="tonal" class="mt-2">
              {{ auth.user?.role === 'ROLE_ADMIN' ? 'Admin' : 'User' }}
            </v-chip>
          </v-card-text>
        </v-card>

        <v-card elevation="0" border>
          <v-card-text class="pa-6">
            <p class="text-body-1 font-weight-semibold mb-4">Account details</p>

            <div class="d-flex flex-column gap-3">
              <div>
                <p class="text-caption text-disabled mb-1">Email</p>
                <p class="text-body-2">{{ auth.user?.email }}</p>
              </div>
              <div>
                <p class="text-caption text-disabled mb-1">Department</p>
                <p class="text-body-2">{{ profile?.department ?? '—' }}</p>
              </div>
              <div>
                <p class="text-caption text-disabled mb-1">User ID</p>
                <p class="text-body-2 text-medium-emphasis font-mono" style="font-size: 11px; word-break: break-all;">
                  {{ auth.user?.id }}
                </p>
              </div>
            </div>

            <v-divider class="my-5" />

            <v-btn color="error" variant="outlined" prepend-icon="mdi-logout" block @click="auth.logout">
              Sign out
            </v-btn>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useAuthStore } from '@/stores/auth'
import { userService } from '@/services/userService'
import UserAvatar from '@/components/common/UserAvatar.vue'

const auth = useAuthStore()
const profile = ref(null)

onMounted(async () => {
  if (auth.user?.id) {
    try {
      const { data } = await userService.getById(auth.user.id)
      profile.value = data
    } catch {
      // Profile might not be loaded yet — not a fatal error
    }
  }
})
</script>
