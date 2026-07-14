<script setup>
import { useTable } from '@/utils/composables/useTable'
import { getMyCourses } from '@/api/user'

const { list, total, loading, pageNo, pageSize, params, handleSizeChange, handleCurrentChange } = useTable(getMyCourses, {
  status: undefined
})
</script>