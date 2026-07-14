<script setup>
import { useTable } from '@/utils/composables/useTable'
import { getAdminLabApplications, auditAdminLabApplication } from '@/api/admin/resource'
import { ElMessage } from 'element-plus'

const { list, total, loading, pageNo, pageSize, params, handleSizeChange, handleCurrentChange, loadData } = useTable(getAdminLabApplications, {
  status: 0, // 默认待审批
  labId: undefined
})

const handleAudit = async (id, status, auditRemark) => {
  await auditAdminLabApplication(id, { status, auditRemark })
  ElMessage.success('审批完成')
  loadData()
}
</script>