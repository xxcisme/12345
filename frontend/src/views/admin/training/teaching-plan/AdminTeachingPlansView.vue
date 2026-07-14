<script setup>
import { useTable } from '@/utils/composables/useTable'
import { getAdminTeachingPlans, publishAdminTeachingPlan, deleteAdminTeachingPlan } from '@/api/admin/training'
import { useConfirm } from '@/utils/composables/useConfirm'
import { usePublish } from '@/utils/composables/usePublish'

const { list, total, loading, pageNo, pageSize, params, handleSizeChange, handleCurrentChange, loadData } = useTable(getAdminTeachingPlans, {
  name: '',
  semester: '',
  status: undefined,
  teacherId: undefined
})

const { handleDelete } = useConfirm(deleteAdminTeachingPlan, () => {
  handleCurrentChange(pageNo.value)
})

const { handlePublish } = usePublish(publishAdminTeachingPlan, null, loadData)
</script>