<template>
  <div class="page-container">
    <slot></slot>
  </div>
</template>

<style scoped lang="scss">
@import '@/styles/variables.scss';

.page-container {
  background: #fff;
  padding: $spacing-large;
  border-radius: 8px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);
  min-height: calc(100vh - 140px); // 根据header和边距调整
}
</style>