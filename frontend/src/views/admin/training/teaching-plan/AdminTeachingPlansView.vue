<script setup>
import { useTable } from '@/utils/composables/useTable'
import { getAdminTeachingPlans, publishAdminTeachingPlan, deleteAdminTeachingPlan } from '@/api/admin/training'
import { useConfirm } from '@/utils/composables/useConfirm'
import { ElMessage } from 'element-plus'

const { list, total, loading, pageNo, pageSize, params, handleSizeChange, handleCurrentChange, loadData } = useTable(getAdminTeachingPlans, {
  name: '',
  semester: '',
  status: undefined,
  teacherId: undefined
})

const { handleDelete } = useConfirm(deleteAdminTeachingPlan, () => {
  handleCurrentChange(pageNo.value)
})

const handlePublish = async (id) => {
  await publishAdminTeachingPlan(id)
  ElMessage.success('发布成功')
  loadData()
}
</script>