<template>
  <page-container>
    <base-search>
      <template #left>
        <el-input v-model="searchForm.title" placeholder="公告标题" clearable style="width:200px" />
        <el-button type="primary" @click="handleSearch">搜索</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </template>
      <template #right>
        <el-button type="primary" @click="handleAdd">新增公告</el-button>
      </template>
    </base-search>

    <el-table :data="list" border style="width:100%; margin-top:10px;" v-loading="loading">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="title" label="标题" min-width="200" show-overflow-tooltip />
      <el-table-column prop="origin" label="来源" min-width="120" />
      <el-table-column prop="publishTime" label="发布时间" min-width="170" />
      <el-table-column label="操作" width="180" fixed="right">
        <template #default="{ row }">
          <el-button type="text" @click="handleEdit(row)">编辑</el-button>
          <el-button type="text" danger @click="handleDelete(row)">删除</el-button>
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
import { reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useTable } from '@/utils/composables/useTable'
import { getAdminNoticesList, deleteAdminNotice } from '@/api/admin/bulletin'
import { useConfirm } from '@/utils/composables/useConfirm'
import PageContainer from '@/components/PageContainer/index.vue'
import BaseSearch from '@/components/BaseSearch/index.vue'

const router = useRouter()

const searchForm = reactive({ title: '' })

const { list, total, loading, pageNo, pageSize, params, handleSizeChange, handleCurrentChange } = useTable(getAdminNoticesList, {
  title: ''
})

const { handleDelete } = useConfirm(deleteAdminNotice, () => {
  handleCurrentChange(pageNo.value)
})

const handleSearch = () => {
  params.title = searchForm.title
  handleCurrentChange(1)
}

const resetSearch = () => {
  searchForm.title = ''
  params.title = ''
  handleCurrentChange(1)
}

const handleAdd = () => {
  router.push('/admin/bulletin/notice/new')
}

const handleEdit = (row) => {
  router.push(`/admin/bulletin/notice/edit/${row.id}`)
}
</script>