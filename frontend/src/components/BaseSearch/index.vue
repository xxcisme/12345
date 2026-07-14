<template>
  <div class="base-search">
    <div class="search-left">
      <slot name="left"></slot>
    </div>
    <div class="search-right">
      <slot name="right"></slot>
    </div>
  </div>
</template>

<style scoped lang="scss">
.base-search {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 12px;
  margin-bottom: 16px;
  .search-left {
    display: flex;
    align-items: center;
    flex-wrap: wrap;
    gap: 10px;
  }
  .search-right {
    display: flex;
    align-items: center;
    gap: 10px;
  }
}
</style>