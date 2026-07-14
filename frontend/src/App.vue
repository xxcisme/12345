<template>
  <component :is="layoutComponent">
    <router-view />
  </component>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import AdminLayout from '@/layout/AdminLayout.vue'
import DefaultLayout from '@/layout/DefaultLayout.vue'

const route = useRoute()
const layoutComponent = computed(() => {
  const layout = route.meta.layout || 'default'
  return layout === 'admin' ? AdminLayout : DefaultLayout
})
</script>