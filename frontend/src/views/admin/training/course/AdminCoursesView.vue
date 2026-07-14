<template>
  <page-container>
    <base-search>
      <template #left>
        <el-input v-model="searchForm.courseName" placeholder="课程名称" clearable style="width: 180px;" />
        <el-input v-model="searchForm.courseCode" placeholder="课程编号" clearable style="width: 150px;" />
        <el-input v-model="searchForm.courseType" placeholder="课程类型" clearable style="width: 130px;" />
        <el-select v-model="searchForm.status" placeholder="状态" clearable style="width: 120px;">
          <el-option label="草稿" :value="0" />
          <el-option label="已发布" :value="1" />
          <el-option label="已下架" :value="2" />
        </el-select>
        <el-select v-model="searchForm.teacherId" placeholder="教师" clearable style="width: 150px;">
          <el-option label="张老师" :value="1" />
          <el-option label="李老师" :value="2" />
          <el-option label="王老师" :value="3" />
        </el-select>
        <el-button type="primary" @click="handleSearch">查询</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </template>
      <template #right>
        <el-button type="primary" @click="handleAdd" v-if="isAdmin || isTeacher">新增课程</el-button>
        <el-button type="success" @click="handleExport" v-if="isAdmin || isTeacher">导出</el-button>
      </template>
    </base-search>

    <el-table :data="list" border style="width:100%; margin-top:10px;" v-loading="loading">
      <el-table-column type="index" label="序号" width="60" align="center" />
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="courseCode" label="课程编号" width="130" />
      <el-table-column prop="courseName" label="课程名称" min-width="180" show-overflow-tooltip />
      <el-table-column prop="courseType" label="课程类型" width="120" />
      <el-table-column prop="teacherName" label="授课教师" width="100" />
      <el-table-column prop="status" label="状态" width="100" align="center">
        <template #default="{ row }">
          <el-tag v-if="row.status === 0">草稿</el-tag>
          <el-tag v-else-if="row.status === 1" type="success">已发布</el-tag>
          <el-tag v-else type="info">已下架</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="160" />
      <el-table-column label="操作" width="280" fixed="right" align="center">
        <template #default="{ row }">
          <el-button link type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
          <el-button link type="success" size="small" @click="handlePublish(row)" v-if="row.status === 0">发布</el-button>
          <el-button link type="warning" size="small" @click="handleUnpublish(row)" v-if="row.status === 1">下架</el-button>
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
  </page-container>
</template>

<script setup>
import { ref, reactive, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useTable } from '@/utils/composables/useTable'
import { getAdminCourses, publishAdminCourse, unpublishAdminCourse, deleteAdminCourse } from '@/api/admin/training'
import { useConfirm } from '@/utils/composables/useConfirm'
import { useUserStore } from '@/stores/user'
import PageContainer from '@/components/PageContainer/index.vue'
import BaseSearch from '@/components/BaseSearch/index.vue'

const router = useRouter()
const userStore = useUserStore()
const isAdmin = computed(() => userStore.role === 'admin')
const isTeacher = computed(() => userStore.role === 'teacher')

const searchForm = reactive({
  courseName: '',
  courseCode: '',
  courseType: '',
  status: null,
  teacherId: null
})

const { list, total, loading, pageNo, pageSize, params, handleSizeChange, handleCurrentChange, loadData } = useTable(getAdminCourses, {
  courseName: '',
  courseCode: '',
  courseType: '',
  status: undefined,
  teacherId: undefined
})

const { handleDelete } = useConfirm(deleteAdminCourse, () => {
  handleCurrentChange(pageNo.value)
})

const handleSearch = () => {
  params.courseName = searchForm.courseName
  params.courseCode = searchForm.courseCode
  params.courseType = searchForm.courseType
  params.status = searchForm.status
  params.teacherId = searchForm.teacherId
  handleCurrentChange(1)
}

const resetSearch = () => {
  searchForm.courseName = ''
  searchForm.courseCode = ''
  searchForm.courseType = ''
  searchForm.status = null
  searchForm.teacherId = null
  params.courseName = ''
  params.courseCode = ''
  params.courseType = ''
  params.status = undefined
  params.teacherId = undefined
  handleCurrentChange(1)
}

const handleAdd = () => {
  router.push('/admin/training/courses/new')
}

const handleEdit = (row) => {
  router.push(`/admin/training/courses/edit/${row.id}`)
}

const handlePublish = async (row) => {
  ElMessageBox.confirm(`确定发布课程「${row.courseName}」吗？`, '提示', { type: 'warning' }).then(async () => {
    await publishAdminCourse(row.id)
    ElMessage.success('发布成功')
    loadData()
  }).catch(() => {})
}

const handleUnpublish = async (row) => {
  ElMessageBox.confirm(`确定下架课程「${row.courseName}」吗？`, '提示', { type: 'warning' }).then(async () => {
    await unpublishAdminCourse(row.id)
    ElMessage.success('下架成功')
    loadData()
  }).catch(() => {})
}

const handleExport = () => {
  ElMessage.success('正在导出课程数据...')
}
</script>