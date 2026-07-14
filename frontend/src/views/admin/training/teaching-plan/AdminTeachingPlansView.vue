<template>
  <page-container>
    <base-search>
      <template #left>
        <el-input v-model="params.name" placeholder="计划名称" clearable style="width: 180px;" />
        <el-input v-model="params.semester" placeholder="学期" clearable style="width: 130px;" />
        <el-select v-model="params.status" placeholder="状态" clearable style="width: 120px;">
          <el-option label="草稿" :value="0" />
          <el-option label="已发布" :value="1" />
        </el-select>
        <el-select v-model="params.teacherId" placeholder="教师" clearable style="width: 150px;">
          <el-option label="张老师" :value="1" />
          <el-option label="李老师" :value="2" />
        </el-select>
        <el-button type="primary" @click="loadData()">查询</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </template>
      <template #right>
        <el-button type="primary" @click="handleAdd">新增计划</el-button>
      </template>
    </base-search>

    <el-table :data="list" border style="width:100%; margin-top:10px;" v-loading="loading">
      <el-table-column type="index" label="序号" width="60" align="center" />
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="计划名称" min-width="180" show-overflow-tooltip />
      <el-table-column prop="semester" label="学期" width="130" />
      <el-table-column prop="courseName" label="课程名称" min-width="150" show-overflow-tooltip />
      <el-table-column prop="teacherName" label="教师" width="100" />
      <el-table-column prop="experimentCount" label="实验数" width="80" align="center" />
      <el-table-column prop="status" label="状态" width="100" align="center">
        <template #default="{ row }">
          <el-tag v-if="row.status === 0">草稿</el-tag>
          <el-tag v-else type="success">已发布</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="160" />
      <el-table-column label="操作" width="220" fixed="right" align="center">
        <template #default="{ row }">
          <el-button link type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
          <el-button link type="success" size="small" @click="handlePublish(row)" v-if="row.status === 0">发布</el-button>
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
import { useRouter } from 'vue-router'
import { useTable } from '@/utils/composables/useTable'
import { useConfirm } from '@/utils/composables/useConfirm'
import { usePublish } from '@/utils/composables/usePublish'
import { getAdminTeachingPlans, publishAdminTeachingPlan, deleteAdminTeachingPlan } from '@/api/admin/training'
import PageContainer from '@/components/PageContainer/index.vue'
import BaseSearch from '@/components/BaseSearch/index.vue'

const router = useRouter()

const { list, total, loading, pageNo, pageSize, params, handleSizeChange, handleCurrentChange, loadData } = useTable(getAdminTeachingPlans, {
  name: '',
  semester: '',
  status: undefined,
  teacherId: undefined
})

const { handleDelete } = useConfirm(deleteAdminTeachingPlan, () => {
  handleCurrentChange(pageNo.value)
})

const { handlePublish } = usePublish(publishAdminTeachingPlan, null, loadData)

const resetSearch = () => {
  params.value = { name: '', semester: '', status: undefined, teacherId: undefined }
  loadData()
}

const handleAdd = () => { router.push('/admin/training/teaching-plans/new') }
const handleEdit = (row) => { router.push(`/admin/training/teaching-plans/edit/${row.id}`) }
</script>

<style scoped lang="scss">
</style>