<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { useTable } from '@/utils/composables/useTable'
import { getLaboratories } from '@/api/resource'
import { deleteAdminLaboratory } from '@/api/admin/resource'
import { useConfirm } from '@/utils/composables/useConfirm'
import { getUser } from '@/utils/local_storage'
import { ROLE_MAP } from '@/utils/constants'

const router = useRouter()
const isAdmin = computed(() => {
  const user = getUser()
  return user && ROLE_MAP[user.role] === 'admin'
})

const { list, total, loading, pageNo, pageSize, params, handleSizeChange, handleCurrentChange, loadData } = useTable(getLaboratories, {
  name: '',
  number: '',
  address: '',
  minStation: undefined
})

const { handleDelete } = useConfirm(deleteAdminLaboratory, loadData)
</script>

<template>
  <div class="page-container">
    <div class="page-header">
      <h2>实验室</h2>
      <router-link v-if="isAdmin" to="/admin/resource/laboratory/new">
        <el-button type="primary">新增实验室</el-button>
      </router-link>
    </div>

    <div class="search-bar">
      <el-input v-model="params.name" placeholder="搜索实验室名称" clearable style="width: 240px" @keyup.enter="handleCurrentChange(1)" />
      <el-button type="primary" @click="handleCurrentChange(1)">搜索</el-button>
    </div>

    <div v-loading="loading">
      <el-table v-if="list.length" :data="list" stripe style="width: 100%">
        <el-table-column label="名称" min-width="200">
          <template #default="{ row }">
            <router-link :to="`/resources/laboratories/${row.id}`" class="table-link">{{ row.name }}</router-link>
          </template>
        </el-table-column>
        <el-table-column prop="number" label="编号" width="120" />
        <el-table-column prop="address" label="地址" min-width="180" show-overflow-tooltip />
        <el-table-column prop="stationNum" label="工位数量" width="100" />
        <el-table-column v-if="isAdmin" label="操作" width="160" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="router.push(`/admin/resource/laboratory/edit/${row.id}`)">编辑</el-button>
            <el-button type="danger" link size="small" @click="handleDelete(row.id, row.name)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
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
  display: flex;
  justify-content: space-between;
  align-items: center;
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
.table-link {
  color: #409eff;
  text-decoration: none;
}
.table-link:hover {
  color: #66b1ff;
}
.pagination-wrap {
  display: flex;
  justify-content: center;
  margin-top: 32px;
}
</style>