<script setup>
import { useTable } from '@/utils/composables/useTable'
import { getAdminExperiments, publishAdminExperiment, deleteAdminExperiment } from '@/api/admin/training'
import { useConfirm } from '@/utils/composables/useConfirm'
import { ElMessage } from 'element-plus'

const { list, total, loading, pageNo, pageSize, params, handleSizeChange, handleCurrentChange, loadData } = useTable(getAdminExperiments, {
  name: '',
  number: '',
  courseId: undefined,
  category: ''
})

const { handleDelete } = useConfirm(deleteAdminExperiment, () => {
  handleCurrentChange(pageNo.value)
})

const handlePublish = async (id) => {
  await publishAdminExperiment(id)
  ElMessage.success('发布成功')
  loadData()
}
</script>