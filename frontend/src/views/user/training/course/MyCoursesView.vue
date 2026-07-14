<template>
  <page-container>
    <base-search>
      <template #left>
        <el-select v-model="searchForm.status" placeholder="课程状态" clearable style="width: 140px;">
          <el-option label="已发布" :value="1" />
          <el-option label="已下架" :value="2" />
        </el-select>
        <el-button type="primary" @click="handleSearch">查询</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </template>
    </base-search>

    <el-table :data="list" border style="width:100%; margin-top:10px;" v-loading="loading">
      <el-table-column type="index" label="序号" width="60" align="center" />
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="courseCode" label="课程编号" width="120" />
      <el-table-column prop="courseName" label="课程名称" min-width="180" show-overflow-tooltip />
      <el-table-column prop="courseType" label="课程类型" width="120" />
      <el-table-column prop="teacherName" label="授课教师" width="100" />
      <el-table-column prop="status" label="状态" width="100" align="center">
        <template #default="{ row }">
          <el-tag v-if="row.status === 1" type="success">已发布</el-tag>
          <el-tag v-else type="info">已下架</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="enrollTime" label="选课时间" width="160" />
      <el-table-column label="操作" width="120" fixed="right" align="center">
        <template #default="{ row }">
          <el-button link type="primary" size="small" @click="handleDetail(row)">详情</el-button>
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
import { reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useTable } from '@/utils/composables/useTable'
import { getMyCourses } from '@/api/user'
import PageContainer from '@/components/PageContainer/index.vue'
import BaseSearch from '@/components/BaseSearch/index.vue'

const router = useRouter()

const searchForm = reactive({
  status: null
})

const { list, total, loading, pageNo, pageSize, params, handleSizeChange, handleCurrentChange } = useTable(getMyCourses, {
  status: undefined
})

const handleSearch = () => {
  params.status = searchForm.status
  handleCurrentChange(1)
}

const resetSearch = () => {
  searchForm.status = null
  params.status = undefined
  handleCurrentChange(1)
}

const handleDetail = (row) => {
  router.push(`/user/courses/${row.id}`)
}
</script>

<style scoped lang="scss">
</style>