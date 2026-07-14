<script setup>
import { useTable } from '@/utils/composables/useTable'
import { getAdminLogs, exportAdminLogs, cleanAdminLogs } from '@/api/admin/system'
import { downloadExcel } from '@/utils/download'
import { ElMessage } from 'element-plus'

const { list, total, loading, pageNo, pageSize, params, handleSizeChange, handleCurrentChange, loadData } = useTable(getAdminLogs, {
  username: '',
  action: '',
  userRole: '',
  startTime: '',
  endTime: ''
})

const handleExport = async () => {
  await downloadExcel(exportAdminLogs, params.value, `logs_${Date.now()}.xls`)
}

const handleClean = async (daysToKeep) => {
  await cleanAdminLogs({ daysToKeep })
  ElMessage.success('清理成功')
  loadData()
}
</script>