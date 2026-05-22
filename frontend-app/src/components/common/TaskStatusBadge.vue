<template>
  <v-chip :color="color" size="small" variant="tonal" class="font-weight-medium">
    {{ label }}
  </v-chip>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  status: { type: String, required: true },
})

const map = {
  TODO:        { label: 'To Do',      color: 'secondary' },
  IN_PROGRESS: { label: 'In Progress', color: 'info' },
  IN_REVIEW:   { label: 'In Review',  color: 'warning' },
  DONE:        { label: 'Done',       color: 'success' },
  CANCELLED:   { label: 'Cancelled',  color: 'error' },
}

const color = computed(() => map[props.status]?.color ?? 'grey')
const label = computed(() => map[props.status]?.label ?? props.status)
</script>
