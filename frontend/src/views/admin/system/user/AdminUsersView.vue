<template>
  <page-container>
    <base-search>
      <template #left>
        <el-input v-model="searchForm.username" placeholder="用户名" clearable style="width:150px" />
        <el-input v-model="searchForm.realName" placeholder="真实姓名" clearable style="width:150px" />
        <el-input v-model="searchForm.phone" placeholder="手机号" clearable style="width:150px" />
        <el-select v-model="searchForm.role" placeholder="角色" clearable style="width:120px">
          <el-option label="学生" :value="1" />
          <el-option label="老师" :value="2" />
          <el-option label="社会人士" :value="3" />
          <el-option label="管理员" :value="4" />
        </el-select>
        <el-select v-model="searchForm.status" placeholder="状态" clearable style="width:100px">
          <el-option label="启用" :value="1" />
          <el-option label="停用" :value="0" />
        </el-select>
        <el-button type="primary" @click="handleSearch">搜索</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </template>
      <template #right>
        <el-button type="primary" @click="handleAdd" v-if="isAdmin">新增用户</el-button>
        <el-button @click="handleImport" v-if="isAdmin">批量导入</el-button>
      </template>
    </base-search>

    <el-table :data="list" border style="width:100%; margin-top: 10px;" v-loading="loading">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="username" label="用户名" min-width="120" />
      <el-table-column prop="realName" label="真实姓名" min-width="100" />
      <el-table-column prop="phone" label="手机号" min-width="130" />
      <el-table-column prop="role" label="角色" min-width="100">
        <template #default="{ row }">
          {{ roleMap[row.role] }}
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" min-width="80">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'danger'">
            {{ row.status === 1 ? '启用' : '停用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" min-width="170" />
      <el-table-column label="操作" width="220" fixed="right">
        <template #default="{ row }">
          <el-button type="text" @click="handleEdit(row)" v-if="isAdmin">编辑</el-button>
          <el-button type="text" @click="toggleStatus(row)" v-if="isAdmin">
            {{ row.status === 1 ? '停用' : '启用' }}
          </el-button>
          <el-button type="text" danger @click="handleDelete(row)" v-if="isAdmin">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      v-model:current-page="pageNo"
      v-model:page-size="pageSize"
      :page-sizes="[10, 20, 50]"
      layout="total, sizes, prev, pager, next"
      :total="total"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      style="margin-top:16px; justify-content: flex-end;"
    />
  </page-container>
</template>

<script setup>
import { ref, reactive, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useTable } from '@/utils/composables/useTable'
import { getAdminUsers, toggleAdminUserStatus, deleteAdminUser } from '@/api/admin/system'
import { useConfirm } from '@/utils/composables/useConfirm'
import { useUserStore } from '@/stores/user'
import PageContainer from '@/components/PageContainer/index.vue'
import BaseSearch from '@/components/BaseSearch/index.vue'

const router = useRouter()
const userStore = useUserStore()
const isAdmin = computed(() => userStore.role === 'admin')

const roleMap = { 1: '学生', 2: '老师', 3: '社会人士', 4: '管理员' }

const searchForm = reactive({
  username: '',
  realName: '',
  phone: '',
  role: null,
  status: null
})

const { list, total, loading, pageNo, pageSize, params, handleSizeChange, handleCurrentChange, loadData } = useTable(getAdminUsers, {
  username: '',
  realName: '',
  phone: '',
  role: undefined,
  status: undefined
})

const { handleDelete } = useConfirm(deleteAdminUser, () => {
  handleCurrentChange(pageNo.value)
})

const handleSearch = () => {
  params.username = searchForm.username
  params.realName = searchForm.realName
  params.phone = searchForm.phone
  params.role = searchForm.role
  params.status = searchForm.status
  handleCurrentChange(1)
}

const resetSearch = () => {
  searchForm.username = ''
  searchForm.realName = ''
  searchForm.phone = ''
  searchForm.role = null
  searchForm.status = null
  params.username = ''
  params.realName = ''
  params.phone = ''
  params.role = undefined
  params.status = undefined
  handleCurrentChange(1)
}

const handleAdd = () => {
  router.push('/admin/system/users/new')
}

const handleImport = () => {
}

const handleEdit = (row) => {
  router.push(`/admin/system/users/edit/${row.id}`)
}

const toggleStatus = async (row) => {
  const newStatus = row.status === 1 ? 0 : 1
  await toggleAdminUserStatus(row.id, { status: newStatus })
  ElMessage.success(`已${newStatus === 1 ? '启用' : '停用'}用户 ${row.username}`)
  loadData()
}
</script>