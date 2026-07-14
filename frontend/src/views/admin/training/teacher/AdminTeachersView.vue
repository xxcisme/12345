<script setup>
import { useTable } from '@/utils/composables/useTable'
import { getAdminTeachers, deleteAdminTeacher } from '@/api/admin/training'
import { useConfirm } from '@/utils/composables/useConfirm'

const { list, total, loading, pageNo, pageSize, params, handleSizeChange, handleCurrentChange } = useTable(getAdminTeachers, {
  name: '',
  teacherId: '',
  company: '',
  onJob: undefined
})

const { handleDelete } = useConfirm(deleteAdminTeacher, () => {
  handleCurrentChange(pageNo.value)
})
</script>