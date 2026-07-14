<template>
  <page-container>
    <base-search>
      <template #left>
        <el-select v-model="searchForm.status" placeholder="申请状态" clearable style="width: 140px;">
          <el-option label="待审批" :value="0" />
          <el-option label="已通过" :value="1" />
          <el-option label="已拒绝" :value="2" />
        </el-select>
        <el-select v-model="searchForm.labId" placeholder="选择实验室" clearable style="width: 180px;">
          <el-option label="计算机基础实验室" :value="1" />
          <el-option label="电子电路实验室" :value="2" />
          <el-option label="机械加工实验室" :value="3" />
        </el-select>
        <el-button type="primary" @click="handleSearch">查询</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </template>
    </base-search>

    <el-table :data="list" border style="width:100%; margin-top:10px;" v-loading="loading">
      <el-table-column type="index" label="序号" width="60" align="center" />
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="labName" label="实验室名称" min-width="150" show-overflow-tooltip />
      <el-table-column prop="applicantName" label="申请人" width="100" />
      <el-table-column prop="contactPhone" label="联系电话" width="130" />
      <el-table-column prop="name" label="实验名称" min-width="150" show-overflow-tooltip />
      <el-table-column prop="startTime" label="开始时间" width="160" />
      <el-table-column prop="endTime" label="结束时间" width="160" />
      <el-table-column prop="status" label="状态" width="100" align="center">
        <template #default="{ row }">
          <el-tag v-if="row.status === 0" type="warning">待审批</el-tag>
          <el-tag v-else-if="row.status === 1" type="success">已通过</el-tag>
          <el-tag v-else type="danger">已拒绝</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="申请时间" width="160" />
      <el-table-column label="操作" width="150" fixed="right" align="center">
        <template #default="{ row }">
          <el-button link type="primary" size="small" @click="handleAudit(row)" v-if="row.status === 0">审批</el-button>
          <el-button link type="info" size="small" @click="handleView(row)">查看</el-button>
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

    <el-dialog v-model="auditDialogVisible" title="审批实验室申请" width="500px">
      <el-form :model="auditForm" label-width="100px">
        <el-form-item label="实验室">
          <span>{{ auditForm.labName }}</span>
        </el-form-item>
        <el-form-item label="申请人">
          <span>{{ auditForm.applicantName }}</span>
        </el-form-item>
        <el-form-item label="实验名称">
          <span>{{ auditForm.name }}</span>
        </el-form-item>
        <el-form-item label="使用时间">
          <span>{{ auditForm.startTime }} 至 {{ auditForm.endTime }}</span>
        </el-form-item>
        <el-form-item label="审批结果" prop="status" required>
          <el-radio-group v-model="auditForm.status">
            <el-radio :label="1">通过</el-radio>
            <el-radio :label="2">拒绝</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="审核备注">
          <el-input v-model="auditForm.auditRemark" type="textarea" :rows="3" placeholder="请输入审核备注或拒绝原因" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="auditDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitAudit">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="viewDialogVisible" title="申请详情" width="600px">
      <el-descriptions :column="1" border>
        <el-descriptions-item label="实验室">{{ viewForm.labName }}</el-descriptions-item>
        <el-descriptions-item label="申请人">{{ viewForm.applicantName }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ viewForm.contactPhone }}</el-descriptions-item>
        <el-descriptions-item label="实验名称">{{ viewForm.name }}</el-descriptions-item>
        <el-descriptions-item label="实验类型">{{ viewForm.experimentType || '-' }}</el-descriptions-item>
        <el-descriptions-item label="申请说明">{{ viewForm.purpose || '-' }}</el-descriptions-item>
        <el-descriptions-item label="开始时间">{{ viewForm.startTime }}</el-descriptions-item>
        <el-descriptions-item label="结束时间">{{ viewForm.endTime }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag v-if="viewForm.status === 0" type="warning">待审批</el-tag>
          <el-tag v-else-if="viewForm.status === 1" type="success">已通过</el-tag>
          <el-tag v-else type="danger">已拒绝</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="审核备注">{{ viewForm.auditRemark || '-' }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </page-container>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { useTable } from '@/utils/composables/useTable'
import { getAdminLabApplications, auditAdminLabApplication } from '@/api/admin/resource'
import PageContainer from '@/components/PageContainer/index.vue'
import BaseSearch from '@/components/BaseSearch/index.vue'

const searchForm = reactive({
  status: null,
  labId: null
})

const { list, total, loading, pageNo, pageSize, params, handleSizeChange, handleCurrentChange, loadData } = useTable(getAdminLabApplications, {
  status: 0,
  labId: undefined
})

const auditDialogVisible = ref(false)
const auditForm = reactive({
  id: null,
  labName: '',
  applicantName: '',
  name: '',
  startTime: '',
  endTime: '',
  status: 1,
  auditRemark: ''
})

const viewDialogVisible = ref(false)
const viewForm = reactive({
  labName: '',
  applicantName: '',
  contactPhone: '',
  name: '',
  experimentType: '',
  purpose: '',
  startTime: '',
  endTime: '',
  status: 0,
  auditRemark: ''
})

const handleSearch = () => {
  params.status = searchForm.status
  params.labId = searchForm.labId
  handleCurrentChange(1)
}

const resetSearch = () => {
  searchForm.status = null
  searchForm.labId = null
  params.status = 0
  params.labId = undefined
  handleCurrentChange(1)
}

const handleAudit = (row) => {
  auditForm.id = row.id
  auditForm.labName = row.labName
  auditForm.applicantName = row.applicantName
  auditForm.name = row.name
  auditForm.startTime = row.startTime
  auditForm.endTime = row.endTime
  auditForm.status = 1
  auditForm.auditRemark = ''
  auditDialogVisible.value = true
}

const submitAudit = async () => {
  try {
    await auditAdminLabApplication(auditForm.id, { status: auditForm.status, auditRemark: auditForm.auditRemark })
    ElMessage.success('审批完成')
    auditDialogVisible.value = false
    loadData()
  } catch (error) {
    ElMessage.error('审批失败')
  }
}

const handleView = (row) => {
  Object.assign(viewForm, row)
  viewDialogVisible.value = true
}
</script>