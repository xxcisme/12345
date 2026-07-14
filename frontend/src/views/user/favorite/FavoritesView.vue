<template>
  <page-container>
    <base-search>
      <template #left>
        <el-select v-model="searchForm.resourceType" placeholder="资源类型" clearable style="width: 140px;">
          <el-option label="视频" :value="1" />
          <el-option label="音频" :value="2" />
          <el-option label="文档" :value="3" />
        </el-select>
        <el-button type="primary" @click="handleSearch">查询</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </template>
    </base-search>

    <el-table :data="list" border style="width:100%; margin-top:10px;" v-loading="loading">
      <el-table-column type="index" label="序号" width="60" align="center" />
      <el-table-column prop="resourceName" label="资源名称" min-width="200" show-overflow-tooltip />
      <el-table-column prop="resourceType" label="类型" width="100">
        <template #default="{ row }">
          <el-tag v-if="row.resourceType === 1" type="danger">视频</el-tag>
          <el-tag v-else-if="row.resourceType === 2" type="warning">音频</el-tag>
          <el-tag v-else type="info">文档</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="category" label="专业大类" width="120" />
      <el-table-column prop="score" label="评分" width="100" align="center">
        <template #default="{ row }">
          <el-rate v-model="row.score" disabled show-score text-color="#ff9900" />
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="收藏时间" width="160" />
      <el-table-column label="操作" width="150" fixed="right" align="center">
        <template #default="{ row }">
          <el-button link type="primary" size="small" @click="handleView(row)">查看</el-button>
          <el-button link type="danger" size="small" @click="handleCancel(row)">取消收藏</el-button>
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
import { getFavorites, cancelFavorite } from '@/api/user'
import { useConfirm } from '@/utils/composables/useConfirm'
import PageContainer from '@/components/PageContainer/index.vue'
import BaseSearch from '@/components/BaseSearch/index.vue'

const router = useRouter()

const searchForm = reactive({
  resourceType: null
})

const { list, total, loading, pageNo, pageSize, params, handleSizeChange, handleCurrentChange } = useTable(getFavorites, {
  resourceType: undefined
})

const { handleDelete } = useConfirm(
  (id) => cancelFavorite(id),
  () => handleCurrentChange(pageNo.value)
)

const handleSearch = () => {
  params.resourceType = searchForm.resourceType
  handleCurrentChange(1)
}

const resetSearch = () => {
  searchForm.resourceType = null
  params.resourceType = undefined
  handleCurrentChange(1)
}

const handleView = (row) => {
  router.push(`/resources/media/${row.id}`)
}

const handleCancel = (row) => {
  handleDelete(row)
}
</script>

<style scoped lang="scss">
</style>