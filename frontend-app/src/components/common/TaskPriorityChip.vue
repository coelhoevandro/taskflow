<template>
  <v-chip :color="color" size="small" variant="outlined" :prepend-icon="icon" class="font-weight-medium">
    {{ label }}
  </v-chip>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  priority: { type: String, required: true },
})

const map = {
  LOW:      { label: 'Low',      color: 'success', icon: 'mdi-arrow-down' },
  MEDIUM:   { label: 'Medium',   color: 'warning', icon: 'mdi-minus' },
  HIGH:     { label: 'High',     color: 'error',   icon: 'mdi-arrow-up' },
  CRITICAL: { label: 'Critical', color: 'error',   icon: 'mdi-alert' },
}

const color = computed(() => map[props.priority]?.color ?? 'grey')
const label = computed(() => map[props.priority]?.label ?? props.priority)
const icon  = computed(() => map[props.priority]?.icon ?? 'mdi-minus')
</script>
