<script setup>
import { computed } from 'vue'
import { useTable } from '@/utils/composables/useTable'
import { getAdminResources, deleteAdminResource, auditAdminResource } from '@/api/admin/resource'
import { useConfirm } from '@/utils/composables/useConfirm'
import { getUser } from '@/utils/local_storage'
import { ROLE_MAP } from '@/utils/constants'
import { ElMessage, ElMessageBox } from 'element-plus'

const user = getUser()
const isAdmin = computed(() => user && ROLE_MAP[user.role] === 'admin')
const isTeacher = computed(() => user && ROLE_MAP[user.role] === 'teacher')

const { list, total, loading, pageNo, pageSize, params, handleSizeChange, handleCurrentChange, loadData } = useTable(getAdminResources, {
  name: '',
  type: undefined,
  status: undefined,
  category: ''
})

const { handleDelete } = useConfirm(deleteAdminResource, loadData)

const handleAudit = async (id, status) => {
  let auditRemark = ''
  if (status === 1) {
    try {
      const { value } = await ElMessageBox.prompt('请输入驳回原因', '驳回', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPattern: /.+/,
        inputErrorMessage: '请输入驳回原因'
      })
      auditRemark = value
    } catch {
      return
    }
  }
  await auditAdminResource(id, { status, auditRemark })
  ElMessage.success('审核完成')
  loadData()
}
</script>

<template>
  <div class="page-container">
    <div class="page-header">
      <h2>媒体资源管理</h2>
      <router-link v-if="isTeacher" to="/admin/resource/media/new">
        <el-button type="primary">新增资源</el-button>
      </router-link>
    </div>

    <div class="search-bar">
      <el-input v-model="params.name" placeholder="搜索资源名称" clearable style="width: 200px" @keyup.enter="handleCurrentChange(1)" />
      <el-select v-model="params.type" placeholder="资源类型" clearable style="width: 140px" @change="handleCurrentChange(1)">
        <el-option label="视频" :value="1" />
        <el-option label="音频" :value="2" />
        <el-option label="文档" :value="3" />
      </el-select>
      <el-select v-model="params.status" placeholder="审核状态" clearable style="width: 140px" @change="handleCurrentChange(1)">
        <el-option label="待审核" :value="0" />
        <el-option label="已驳回" :value="1" />
        <el-option label="已发布" :value="2" />
      </el-select>
      <el-input v-model="params.category" placeholder="专业大类" clearable style="width: 160px" @keyup.enter="handleCurrentChange(1)" />
      <el-button type="primary" @click="handleCurrentChange(1)">搜索</el-button>
    </div>

    <div v-loading="loading">
      <el-table v-if="list.length" :data="list" stripe style="width: 100%">
        <el-table-column label="名称" min-width="180" show-overflow-tooltip>
          <template #default="{ row }">
            <router-link :to="`/resources/media/${row.id}`" class="resource-name-link">{{ row.name }}</router-link>
          </template>
        </el-table-column>
        <el-table-column label="类型" width="100">
          <template #default="{ row }">
            <el-tag size="small">{{ row.type === 1 ? '视频' : row.type === 2 ? '音频' : '文档' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="category" label="分类" width="120" />
        <el-table-column prop="school" label="学校" width="120" />
        <el-table-column prop="uploaderName" label="上传者" width="120" />
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 0 ? 'warning' : row.status === 1 ? 'danger' : 'success'" size="small">
              {{ row.status === 0 ? '待审核' : row.status === 1 ? '已驳回' : '已发布' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <template v-if="isAdmin">
              <template v-if="row.status === 0">
                <el-button type="success" link size="small" @click="handleAudit(row.id, 2)">通过</el-button>
                <el-button type="danger" link size="small" @click="handleAudit(row.id, 1)">驳回</el-button>
              </template>
              <span v-else style="color: #c0c4cc">已审核</span>
            </template>
            <template v-else-if="isTeacher">
              <el-button type="primary" link size="small" @click="$router.push(`/admin/resource/media/edit/${row.id}`)">编辑</el-button>
              <el-button type="danger" link size="small" @click="handleDelete(row.id, row.name)">删除</el-button>
            </template>
          </template>
        </el-table-column>
      </el-table>
      <el-empty v-else description="暂无资源" />
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
  padding: 20px;
}
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}
.page-header h2 {
  font-size: 20px;
  color: #303133;
}
.search-bar {
  display: flex;
  gap: 12px;
  margin-bottom: 20px;
  flex-wrap: wrap;
}
.pagination-wrap {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}
.resource-name-link {
  color: #409eff;
  text-decoration: none;
}
.resource-name-link:hover {
  color: #66b1ff;
}
</style>