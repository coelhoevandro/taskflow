<template>
  <div class="d-flex flex-column align-center justify-center py-12 text-center">
    <v-icon :size="56" color="grey-lighten-1" class="mb-4">{{ icon }}</v-icon>
    <p class="text-h6 text-medium-emphasis mb-1">{{ title }}</p>
    <p v-if="subtitle" class="text-body-2 text-disabled">{{ subtitle }}</p>
    <slot />
  </div>
</template>

<script setup>
defineProps({
  icon: { type: String, default: 'mdi-inbox-outline' },
  title: { type: String, required: true },
  subtitle: { type: String, default: '' },
})
</script>
