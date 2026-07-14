<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { useTable } from '@/utils/composables/useTable'
import { getDevices } from '@/api/resource'
import { deleteAdminDevice } from '@/api/admin/resource'
import { useConfirm } from '@/utils/composables/useConfirm'
import { getUser } from '@/utils/local_storage'
import { ROLE_MAP } from '@/utils/constants'

const router = useRouter()
const isAdmin = computed(() => {
  const user = getUser()
  return user && ROLE_MAP[user.role] === 'admin'
})

const { list, total, loading, pageNo, pageSize, params, handleSizeChange, handleCurrentChange, loadData } = useTable(getDevices, {
  name: '',
  number: '',
  laboratoryId: undefined,
  type: '',
  status: undefined
})

const { handleDelete } = useConfirm(deleteAdminDevice, loadData)
</script>

<template>
  <div class="page-container">
    <div class="page-header">
      <h2>设备资源</h2>
      <router-link v-if="isAdmin" to="/admin/resource/device/new">
        <el-button type="primary">新增设备</el-button>
      </router-link>
    </div>

    <div class="search-bar">
      <el-input v-model="params.name" placeholder="搜索设备名称" clearable style="width: 240px" @keyup.enter="handleCurrentChange(1)" />
      <el-select v-model="params.status" placeholder="设备状态" clearable style="width: 160px" @change="handleCurrentChange(1)">
        <el-option label="空闲" :value="0" />
        <el-option label="使用中" :value="1" />
        <el-option label="保修" :value="2" />
        <el-option label="损坏" :value="3" />
      </el-select>
      <el-button type="primary" @click="handleCurrentChange(1)">搜索</el-button>
    </div>

    <div v-loading="loading">
      <el-table v-if="list.length" :data="list" stripe style="width: 100%">
        <el-table-column label="名称" min-width="200">
          <template #default="{ row }">
            <router-link :to="`/resources/devices/${row.id}`" class="table-link">{{ row.name }}</router-link>
          </template>
        </el-table-column>
        <el-table-column prop="number" label="编号" width="120" />
        <el-table-column prop="type" label="类型" width="120" />
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 0 ? 'success' : row.status === 1 ? 'warning' : row.status === 2 ? 'info' : 'danger'" size="small">
              {{ row.status === 0 ? '空闲' : row.status === 1 ? '使用中' : row.status === 2 ? '保修' : '损坏' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column v-if="isAdmin" label="操作" width="160" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="router.push(`/admin/resource/device/edit/${row.id}`)">编辑</el-button>
            <el-button type="danger" link size="small" @click="handleDelete(row.id, row.name)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-empty v-else description="暂无设备" />
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
  flex-wrap: wrap;
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