<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useTable } from '@/utils/composables/useTable'
import { getFavorites } from '@/api/user'
import { cancelFavorite } from '@/api/user'
import { useConfirm } from '@/utils/composables/useConfirm'

const router = useRouter()

// 初始化时把 resourceType 与 keyword 作为查询参数传入 useTable
const { list, total, loading, pageNo, pageSize, params, handleSizeChange, handleCurrentChange } = useTable(getFavorites, {
  resourceType: undefined,
  keyword: undefined
})

// 本地绑定的控件值（便于模板双向绑定）
const localType = ref(undefined)
const keyword = ref('')

const { handleDelete } = useConfirm(
  (id) => cancelFavorite(id),
  () => handleCurrentChange(pageNo.value)
)

// 当筛选/搜索时，更新 params 并回到第一页
function applyFilter() {
  params.value.resourceType = localType.value === 'all' ? undefined : localType.value
  params.value.keyword = keyword.value ? keyword.value.trim() : undefined
  handleCurrentChange(1)
}

function clearFilter() {
  localType.value = 'all'
  keyword.value = ''
  applyFilter()
}

// 打开资源详情（请根据项目实际路由调整路径）
function openDetail(row) {
  // 假设详情路由为 /resource/:id 或根据类型区分：/resource/video/:id 等
  router.push({ path: `/resource/${row.resourceId}` }).catch(()=>{})
}
</script>

<template>
  <div class="page-container">
    <div class="page-header">
      <h2>我的收藏</h2>
    </div>

    <!-- 筛选与搜索 -->
    <div class="controls" style="margin-bottom:16px; display:flex; gap:8px; align-items:center">
      <el-select v-model="localType" placeholder="资源类型" style="width:160px">
        <el-option label="全部" value="all" />
        <el-option label="视频" :value="1" />
        <el-option label="音频" :value="2" />
        <el-option label="文档" :value="3" />
      </el-select>

      <el-input
        v-model="keyword"
        placeholder="搜索资源名称/描述"
        clearable
        @clear="applyFilter"
        @keyup.enter="applyFilter"
        style="width:320px"
      >
        <template #append>
          <el-button type="primary" @click="applyFilter">搜索</el-button>
        </template>
      </el-input>

      <el-button type="text" @click="clearFilter">重置</el-button>
    </div>

    <div v-loading="loading">
      <el-table v-if="list && list.length" :data="list" stripe style="width: 100%">
        <el-table-column label="资源名称" min-width="200">
          <template #default="{ row }">
            <el-link type="primary" underline @click="openDetail(row)">{{ row.resourceName || '—' }}</el-link>
          </template>
        </el-table-column>

        <el-table-column label="资源类型" width="120">
          <template #default="{ row }">
            <el-tag size="small">{{ row.resourceType === 1 ? '视频' : row.resourceType === 2 ? '音频' : '文档' }}</el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="createTime" label="收藏时间" width="180" />

        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button type="danger" link size="small" @click="handleDelete(row.resourceId, row.resourceName)">
              取消收藏
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-empty v-else description="暂无收藏" />
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
  max-width: 1000px;
  margin: 0 auto;
  padding: 40px 20px;
}
.page-header {
  margin-bottom: 24px;
}
.page-header h2 {
  font-size: 24px;
  color: #303133;
}
.pagination-wrap {
  display: flex;
  justify-content: center;
  margin-top: 24px;
}
.controls {
  align-items: center;
}
</style>
