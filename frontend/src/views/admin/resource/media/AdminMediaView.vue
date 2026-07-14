<template>
  <page-container>
    <base-search>
      <template #left>
        <el-input v-model="searchForm.name" placeholder="资源名称" clearable style="width: 180px;" />
        <el-select v-model="searchForm.type" placeholder="资源类型" clearable style="width: 140px;">
          <el-option label="视频" :value="1" />
          <el-option label="音频" :value="2" />
          <el-option label="文档" :value="3" />
        </el-select>
        <el-select v-model="searchForm.status" placeholder="状态" clearable style="width: 140px;">
          <el-option label="待审核" :value="0" />
          <el-option label="已驳回" :value="1" />
          <el-option label="已发布" :value="2" />
        </el-select>
        <el-input v-model="searchForm.category" placeholder="专业大类" clearable style="width: 150px;" />
        <el-button type="primary" @click="handleSearch">查询</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </template>
      <template #right>
        <el-button type="primary" @click="handleAdd" v-if="isAdmin || isTeacher">新增资源</el-button>
      </template>
    </base-search>

    <el-table :data="tableData" border style="width:100%; margin-top:10px;" v-loading="loading">
      <el-table-column type="index" label="序号" width="60" align="center" />
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="资源名称" min-width="180" show-overflow-tooltip />
      <el-table-column prop="type" label="类型" width="100">
        <template #default="{ row }">
          <el-tag v-if="row.type === 1" type="danger">视频</el-tag>
          <el-tag v-else-if="row.type === 2" type="warning">音频</el-tag>
          <el-tag v-else type="info">文档</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="category" label="专业大类" width="120" />
      <el-table-column prop="school" label="所属学校" width="150" show-overflow-tooltip />
      <el-table-column prop="leader" label="负责人" width="100" />
      <el-table-column prop="isShared" label="共享" width="80" align="center">
        <template #default="{ row }">
          <el-tag v-if="row.isShared" type="success">是</el-tag>
          <el-tag v-else type="info">否</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="100" align="center">
        <template #default="{ row }">
          <el-tag v-if="row.status === 0" type="warning">待审核</el-tag>
          <el-tag v-else-if="row.status === 1" type="danger">已驳回</el-tag>
          <el-tag v-else type="success">已发布</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="160" />
      <el-table-column label="操作" width="220" fixed="right" align="center">
        <template #default="{ row }">
          <el-button link type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
          <el-button link type="success" size="small" @click="handleAudit(row)" v-if="isAdmin && row.status === 0">审核</el-button>
          <el-button link type="danger" size="small" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      v-model:current-page="pageNo"
      v-model:page-size="pageSize"
      :page-sizes="[10, 20, 50]"
      :total="total"
      layout="total, sizes, prev, pager, next, jumper"
      style="margin-top: 15px; justify-content: flex-end;"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />

    <!-- 审核弹窗 -->
    <el-dialog v-model="auditDialogVisible" title="资源审核" width="500px">
      <el-form :model="auditForm" label-width="100px">
        <el-form-item label="资源名称">
          <span>{{ auditForm.name }}</span>
        </el-form-item>
        <el-form-item label="审核结果" prop="status" required>
          <el-radio-group v-model="auditForm.status">
            <el-radio :label="2">通过</el-radio>
            <el-radio :label="1">驳回</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="审核备注" v-if="auditForm.status === 1">
          <el-input v-model="auditForm.auditRemark" type="textarea" :rows="3" placeholder="驳回原因（必填）" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="auditDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitAudit">确定</el-button>
      </template>
    </el-dialog>
  </page-container>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import PageContainer from '@/components/PageContainer/index.vue'
import BaseSearch from '@/components/BaseSearch/index.vue'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()
const isAdmin = computed(() => userStore.role === 'admin')
const isTeacher = computed(() => userStore.role === 'teacher')

const loading = ref(false)
const pageNo = ref(1)
const pageSize = ref(10)
const total = ref(0)

const searchForm = reactive({
  name: '',
  type: null,
  status: null,
  category: ''
})

const tableData = ref([])

const auditDialogVisible = ref(false)
const auditForm = reactive({
  id: null,
  name: '',
  status: 2,
  auditRemark: ''
})

const fetchData = () => {
  loading.value = true
  setTimeout(() => {
    tableData.value = [
      {
        id: 1,
        name: 'Python基础编程视频教程',
        type: 1,
        category: '计算机类',
        school: 'XX大学',
        leader: '张老师',
        isShared: true,
        status: 2,
        createTime: '2026-07-01 10:00:00'
      },
      {
        id: 2,
        name: '电路分析实验指导音频',
        type: 2,
        category: '电子信息类',
        school: 'XX学院',
        leader: '李老师',
        isShared: false,
        status: 0,
        createTime: '2026-07-05 14:30:00'
      },
      {
        id: 3,
        name: '机械设计基础文档',
        type: 3,
        category: '机械类',
        school: 'XX工业大学',
        leader: '王老师',
        isShared: true,
        status: 1,
        createTime: '2026-07-08 09:15:00'
      },
      {
        id: 4,
        name: '数据结构算法视频',
        type: 1,
        category: '计算机类',
        school: 'XX大学',
        leader: '赵老师',
        isShared: true,
        status: 0,
        createTime: '2026-07-10 16:45:00'
      },
      {
        id: 5,
        name: '自动控制原理音频讲解',
        type: 2,
        category: '自动化类',
        school: 'XX科技大学',
        leader: '刘老师',
        isShared: false,
        status: 2,
        createTime: '2026-07-12 11:20:00'
      }
    ]
    total.value = 5
    loading.value = false
  }, 500)
}

const handleSearch = () => {
  pageNo.value = 1
  fetchData()
}

const resetSearch = () => {
  searchForm.name = ''
  searchForm.type = null
  searchForm.status = null
  searchForm.category = ''
  handleSearch()
}

const handleAdd = () => {
  router.push('/admin/resource/media/new')
}

const handleEdit = (row) => {
  router.push(`/admin/resource/media/edit/${row.id}`)
}

const handleAudit = (row) => {
  auditForm.id = row.id
  auditForm.name = row.name
  auditForm.status = 2
  auditForm.auditRemark = ''
  auditDialogVisible.value = true
}

const submitAudit = () => {
  if (auditForm.status === 1 && !auditForm.auditRemark) {
    ElMessage.warning('驳回时必须填写审核备注')
    return
  }
  ElMessage.success('审核操作成功')
  auditDialogVisible.value = false
  fetchData()
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`确定删除资源「${row.name}」吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    ElMessage.success('删除成功')
    fetchData()
  }).catch(() => {})
}

const handleSizeChange = (val) => {
  pageSize.value = val
  fetchData()
}

const handleCurrentChange = (val) => {
  pageNo.value = val
  fetchData()
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped lang="scss">
</style>