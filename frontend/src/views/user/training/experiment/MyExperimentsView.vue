<script setup>
import { useTable } from '@/utils/composables/useTable'
import { getMyExperiments } from '@/api/user'

const { list, total, loading, pageNo, pageSize, params, handleSizeChange, handleCurrentChange } = useTable(getMyExperiments, {
  status: undefined,
  courseId: undefined
})
</script>