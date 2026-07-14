<script setup>
import { useTable } from '@/utils/composables/useTable'
import { getAdminLogs, exportAdminLogs, cleanAdminLogs } from '@/api/admin/system'
import { ElMessage } from 'element-plus'

const { list, total, loading, pageNo, pageSize, params, handleSizeChange, handleCurrentChange, loadData } = useTable(getAdminLogs, {
  username: '',
  action: '',
  userRole: '',
  startTime: '',
  endTime: ''
})

const handleExport = async () => {
  const res = await exportAdminLogs(params)
  // 下载文件
  const blob = new Blob([res.data], { type: 'application/vnd.ms-excel' })
  const link = document.createElement('a')
  link.href = URL.createObjectURL(blob)
  link.download = `logs_${Date.now()}.xls`
  link.click()
}

const handleClean = async (daysToKeep) => {
  await cleanAdminLogs({ daysToKeep })
  ElMessage.success('清理成功')
  loadData()
}
</script>