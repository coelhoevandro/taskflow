<template>
  <v-dialog v-model="model" max-width="420" persistent>
    <v-card>
      <v-card-title class="text-h6 pt-5 px-6">{{ title }}</v-card-title>
      <v-card-text class="px-6 text-medium-emphasis">{{ message }}</v-card-text>
      <v-card-actions class="px-6 pb-5">
        <v-spacer />
        <v-btn variant="text" @click="emit('cancel')">Cancel</v-btn>
        <v-btn :color="confirmColor" variant="flat" @click="emit('confirm')">{{ confirmLabel }}</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup>
const model = defineModel()
defineProps({
  title:        { type: String, default: 'Are you sure?' },
  message:      { type: String, default: '' },
  confirmLabel: { type: String, default: 'Confirm' },
  confirmColor: { type: String, default: 'primary' },
})
const emit = defineEmits(['confirm', 'cancel'])
</script>
