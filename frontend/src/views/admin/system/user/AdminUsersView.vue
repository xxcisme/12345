<script setup>
import { useTable } from '@/utils/composables/useTable'
import { getAdminUsers, toggleAdminUserStatus, deleteAdminUser } from '@/api/admin/system'
import { useConfirm } from '@/utils/composables/useConfirm'
import { ElMessage } from 'element-plus'

const { list, total, loading, pageNo, pageSize, params, handleSizeChange, handleCurrentChange, loadData } = useTable(getAdminUsers, {
  username: '',
  realName: '',
  phone: '',
  role: undefined,
  status: undefined
})

const { handleDelete } = useConfirm(deleteAdminUser, () => {
  handleCurrentChange(pageNo.value)
})

const handleToggleStatus = async (id, currentStatus) => {
  const newStatus = currentStatus === 1 ? 0 : 1
  await toggleAdminUserStatus(id, { status: newStatus })
  ElMessage.success('状态切换成功')
  loadData()
}
</script>