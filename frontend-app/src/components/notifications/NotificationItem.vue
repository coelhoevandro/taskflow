<template>
  <v-list-item
    :class="['notification-item', { 'unread': !notification.read }]"
    rounded="lg"
    @click="handleClick"
  >
    <template #prepend>
      <v-avatar :color="iconColor" size="40" variant="tonal">
        <v-icon size="20">{{ icon }}</v-icon>
      </v-avatar>
    </template>

    <v-list-item-title class="text-body-2 font-weight-medium mb-1">
      {{ notification.title }}
    </v-list-item-title>
    <v-list-item-subtitle class="text-caption">
      {{ notification.message }}
    </v-list-item-subtitle>

    <template #append>
      <div class="d-flex flex-column align-end gap-1">
        <span class="text-caption text-disabled">{{ timeAgo(notification.createdAt) }}</span>
        <v-icon v-if="!notification.read" color="primary" size="8">mdi-circle</v-icon>
      </div>
    </template>
  </v-list-item>
</template>

<script setup>
import { computed } from 'vue'
import { useFormatters } from '@/composables/useFormatters'

const props = defineProps({
  notification: { type: Object, required: true },
})
const emit = defineEmits(['read'])

const { timeAgo } = useFormatters()

const typeConfig = {
  TASK_ASSIGNED:  { icon: 'mdi-account-check-outline', color: 'primary' },
  TASK_UPDATED:   { icon: 'mdi-pencil-outline', color: 'info' },
  TASK_COMMENTED: { icon: 'mdi-comment-outline', color: 'secondary' },
  TASK_COMPLETED: { icon: 'mdi-check-circle-outline', color: 'success' },
}

const icon = computed(() => typeConfig[props.notification.type]?.icon ?? 'mdi-bell-outline')
const iconColor = computed(() => typeConfig[props.notification.type]?.color ?? 'grey')

function handleClick() {
  if (!props.notification.read) {
    emit('read', props.notification.id)
  }
}
</script>

<style scoped>
.notification-item.unread {
  background: rgba(59, 91, 219, 0.04);
}
</style>
