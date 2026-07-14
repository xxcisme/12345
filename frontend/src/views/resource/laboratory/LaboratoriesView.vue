<template>
  <page-container>
    <base-search>
      <template #left>
        <el-input v-model="params.name" placeholder="实验室名称" clearable style="width: 180px;" />
        <el-input v-model="params.number" placeholder="实验室编号" clearable style="width: 150px;" />
        <el-input v-model="params.address" placeholder="地址" clearable style="width: 180px;" />
        <el-input-number v-model="params.minStation" placeholder="最小工位数" :min="0" style="width: 140px;" />
        <el-button type="primary" @click="loadData()">查询</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </template>
      <template #right>
        <el-button type="primary" @click="handleAdd">新增实验室</el-button>
      </template>
    </base-search>

    <el-table :data="list" border style="width:100%; margin-top:10px;" v-loading="loading">
      <el-table-column type="index" label="序号" width="60" align="center" />
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="number" label="编号" width="130" />
      <el-table-column prop="name" label="实验室名称" min-width="180" show-overflow-tooltip />
      <el-table-column prop="stationNum" label="工位数" width="90" align="center" />
      <el-table-column prop="area" label="面积(㎡)" width="100" align="center" />
      <el-table-column prop="address" label="地址" min-width="180" show-overflow-tooltip />
      <el-table-column label="操作" width="180" fixed="right" align="center">
        <template #default="{ row }">
          <el-button link type="primary" size="small" @click="handleDetail(row)">详情</el-button>
          <el-button link type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
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
import { getLaboratories } from '@/api/resource'
import { deleteAdminLaboratory } from '@/api/admin/resource'
import PageContainer from '@/components/PageContainer/index.vue'
import BaseSearch from '@/components/BaseSearch/index.vue'

const router = useRouter()

const { list, total, loading, pageNo, pageSize, params, handleSizeChange, handleCurrentChange, loadData } = useTable(getLaboratories, {
  name: '',
  number: '',
  address: '',
  minStation: undefined
})

const { handleDelete } = useConfirm(deleteAdminLaboratory, () => handleCurrentChange(pageNo.value))

const resetSearch = () => {
  params.value = { name: '', number: '', address: '', minStation: undefined }
  loadData()
}

const handleDetail = (row) => { router.push(`/resources/laboratories/${row.id}`) }
const handleEdit = (row) => { router.push(`/admin/resource/laboratory/edit/${row.id}`) }
const handleAdd = () => { router.push('/admin/resource/laboratory/new') }
</script>

<style scoped lang="scss">
</style>