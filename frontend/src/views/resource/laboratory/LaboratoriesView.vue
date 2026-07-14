<script setup>
import { useTable } from '@/utils/composables/useTable'
import { getLaboratories } from '@/api/resource'

const { list, total, loading, pageNo, pageSize, params, handleSizeChange, handleCurrentChange } = useTable(getLaboratories, {
  name: '',
  number: '',
  address: '',
  minStation: undefined
})
</script>

<template>
  <div class="page-container">
    <div class="page-header">
      <h2>实验室</h2>
    </div>

    <div class="search-bar">
      <el-input v-model="params.name" placeholder="搜索实验室名称" clearable style="width: 240px" @keyup.enter="handleCurrentChange(1)" />
      <el-button type="primary" @click="handleCurrentChange(1)">搜索</el-button>
    </div>

    <div v-loading="loading">
      <div v-if="list.length" class="lab-grid">
        <div v-for="item in list" :key="item.id" class="lab-card">
          <router-link :to="`/resources/laboratories/${item.id}`" class="card-link">
            <div class="card-cover">
              <el-icon :size="48"><OfficeBuilding /></el-icon>
            </div>
            <div class="card-info">
              <h4>{{ item.name }}</h4>
              <p class="card-desc">{{ item.address }}</p>
              <div class="card-meta">
                <span>编号：{{ item.number }}</span>
                <span>工位：{{ item.minStation }}</span>
              </div>
            </div>
          </router-link>
        </div>
      </div>
      <el-empty v-else description="暂无实验室" />
    </div>

    <div class="pagination-wrap" v-if="total > pageSize">
      <el-pagination
        v-model:current-page="pageNo"
        v-model:page-size="pageSize"
        :total="total"
        :page-sizes="[10, 20, 50]"
        layout="total, sizes, prev, pager, next"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
  </div>
</template>

<style scoped>
.page-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 40px 20px;
}
.page-header {
  margin-bottom: 24px;
}
.page-header h2 {
  font-size: 24px;
  color: #303133;
}
.search-bar {
  display: flex;
  gap: 12px;
  margin-bottom: 24px;
}
.lab-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
}
.lab-card {
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);
  transition: box-shadow 0.3s, transform 0.3s;
}
.lab-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.12);
  transform: translateY(-2px);
}
.card-link {
  text-decoration: none;
  color: inherit;
}
.card-cover {
  height: 140px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f5f7fa;
  color: #409eff;
}
.card-info {
  padding: 16px;
}
.card-info h4 {
  font-size: 16px;
  margin-bottom: 8px;
  color: #303133;
}
.card-desc {
  font-size: 13px;
  color: #909399;
  margin-bottom: 8px;
}
.card-meta {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
  color: #c0c4cc;
}
.pagination-wrap {
  display: flex;
  justify-content: center;
  margin-top: 32px;
}
@media (max-width: 768px) {
  .lab-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}
</style>