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

    <el-table :data="tableData" border style="width:100%; margin-top: 10px;" v-loading="loading">
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
      @size-change="handleSearch"
      @current-change="handleSearch"
      style="margin-top:16px; justify-content: flex-end;"
    />
  </page-container>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import PageContainer from '@/components/PageContainer/index.vue'
import BaseSearch from '@/components/BaseSearch/index.vue'
// 模拟用户 store，实际从 pinia 获取
const userStore = { role: 'admin' } // 演示用
const isAdmin = computed(() => userStore.role === 'admin')

// 角色映射
const roleMap = { 1: '学生', 2: '老师', 3: '社会人士', 4: '管理员' }

// 搜索表单
const searchForm = reactive({
  username: '',
  realName: '',
  phone: '',
  role: null,
  status: null
})

const pageNo = ref(1)
const pageSize = ref(10)
const total = ref(0)
const tableData = ref([])
const loading = ref(false)

// 模拟数据加载
const fetchData = async () => {
  loading.value = true
  try {
    // 调用 /api/v1/admin/users 接口
    // 模拟响应
    const mockRes = {
      code: '000',
      data: [
        { id: 1, username: 'admin', realName: '系统管理员', phone: '13800001111', role: 4, status: 1, createTime: '2026-01-01 10:00' },
        { id: 2, username: 'teacher01', realName: '张老师', phone: '13800002222', role: 2, status: 1, createTime: '2026-02-01 11:00' },
        { id: 3, username: 'student01', realName: '李同学', phone: '13800003333', role: 1, status: 0, createTime: '2026-03-01 09:00' },
      ],
      total: 3
    }
    tableData.value = mockRes.data
    total.value = mockRes.total
  } finally {
    loading.value = false
  }
}

const handleSearch = () => { fetchData() }
const resetSearch = () => {
  Object.assign(searchForm, { username: '', realName: '', phone: '', role: null, status: null })
  fetchData()
}

const handleAdd = () => { /* 跳转新增页 */ }
const handleImport = () => { /* 打开导入弹窗 */ }
const handleEdit = (row) => { /* 跳转编辑页 */ }

const toggleStatus = async (row) => {
  // 调用切换状态接口
  ElMessage.success(`已${row.status === 1 ? '停用' : '启用'}用户 ${row.username}`)
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`确定删除用户 ${row.username} 吗？`, '提示', { type: 'warning' }).then(() => {
    // 调用删除接口
    ElMessage.success('删除成功')
  })
}

onMounted(() => { fetchData() })
</script>

<style scoped lang="scss">
// 此页面无特殊样式，全部由组件提供
</style>