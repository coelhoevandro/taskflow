<template>
  <v-avatar :size="size" :title="name">
    <v-img :src="src" :alt="name" />
  </v-avatar>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  name: { type: String, default: '?' },
  size: { type: [Number, String], default: 36 },
})

const src = computed(() => {
  const encoded = encodeURIComponent(props.name)
  return `https://ui-avatars.com/api/?name=${encoded}&background=3B5BDB&color=fff&size=${props.size * 2}`
})
</script>

<style scoped>
.v-avatar {
  margin-right: 5px;
}
</style>
