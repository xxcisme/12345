<template>
  <page-container>
    <base-search>
      <template #left>
        <el-select v-model="searchForm.type" placeholder="通知类型" clearable style="width: 140px;">
          <el-option label="审核通知" :value="1" />
          <el-option label="课程提醒" :value="2" />
          <el-option label="成绩通知" :value="3" />
        </el-select>
        <el-select v-model="searchForm.isRead" placeholder="阅读状态" clearable style="width: 120px;">
          <el-option label="已读" :value="true" />
          <el-option label="未读" :value="false" />
        </el-select>
        <el-button type="primary" @click="handleSearch">查询</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </template>
      <template #right>
        <el-button type="primary" @click="handleReadAll">全部标记已读</el-button>
      </template>
    </base-search>

    <el-table :data="tableData" border style="width:100%; margin-top:10px;" v-loading="loading" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="title" label="标题" min-width="200" show-overflow-tooltip>
        <template #default="{ row }">
          <el-badge is-dot :hidden="row.isRead" style="margin-right:8px;" />
          <span :style="{ fontWeight: row.isRead ? 'normal' : 'bold' }">{{ row.title }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="type" label="类型" width="100">
        <template #default="{ row }">
          <el-tag v-if="row.type === 1">审核通知</el-tag>
          <el-tag v-else-if="row.type === 2" type="success">课程提醒</el-tag>
          <el-tag v-else type="warning">成绩通知</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="content" label="内容" min-width="250" show-overflow-tooltip />
      <el-table-column prop="createTime" label="发送时间" width="160" />
      <el-table-column prop="isRead" label="状态" width="80" align="center">
        <template #default="{ row }">
          <el-tag v-if="row.isRead" type="info">已读</el-tag>
          <el-tag v-else type="danger">未读</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="120" fixed="right" align="center">
        <template #default="{ row }">
          <el-button link type="primary" size="small" @click="handleRead(row)" v-if="!row.isRead">标记已读</el-button>
          <span v-else style="color:#999;">-</span>
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
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import PageContainer from '@/components/PageContainer/index.vue'
import BaseSearch from '@/components/BaseSearch/index.vue'

const loading = ref(false)
const pageNo = ref(1)
const pageSize = ref(10)
const total = ref(0)
const selectedIds = ref([])

const searchForm = reactive({
  type: null,
  isRead: null
})

const tableData = ref([])

const fetchData = () => {
  loading.value = true
  setTimeout(() => {
    tableData.value = [
      { id: 1, title: '实验室申请已通过', type: 1, content: '您的实验室使用申请（计算机基础实验室）已通过审批。', createTime: '2026-07-14 09:00:00', isRead: false },
      { id: 2, title: 'Python课程即将开始', type: 2, content: '您选修的Python程序设计课程将于明天08:00开始。', createTime: '2026-07-13 18:00:00', isRead: true },
      { id: 3, title: '实验成绩已发布', type: 3, content: '您的「Python基础语法实验」成绩已发布，得分：85分。', createTime: '2026-07-12 10:30:00', isRead: false },
      { id: 4, title: '资源审核被驳回', type: 1, content: '您上传的资源「数据结构算法视频」未通过审核，原因：视频清晰度不足。', createTime: '2026-07-10 14:00:00', isRead: true }
    ]
    total.value = 4
    loading.value = false
  }, 500)
}

const handleSearch = () => { pageNo.value = 1; fetchData() }
const resetSearch = () => { Object.assign(searchForm, { type: null, isRead: null }); handleSearch() }

const handleSelectionChange = (selection) => {
  selectedIds.value = selection.map(item => item.id)
}

const handleRead = (row) => {
  row.isRead = true
  ElMessage.success('已标记为已读')
}

const handleReadAll = () => {
  ElMessageBox.confirm('确定将所有消息标记为已读吗？', '提示', { type: 'warning' }).then(() => {
    tableData.value.forEach(item => item.isRead = true)
    ElMessage.success('全部标记已读')
  }).catch(() => {})
}

const handleSizeChange = (val) => { pageSize.value = val; fetchData() }
const handleCurrentChange = (val) => { pageNo.value = val; fetchData() }

onMounted(() => { fetchData() })
</script>

<style scoped lang="scss">
</style>