<script setup>
import { useTable } from '@/utils/composables/useTable'
import { getMyLabApplications } from '@/api/user'

const { list, total, loading, pageNo, pageSize, params, handleSizeChange, handleCurrentChange } = useTable(getMyLabApplications, {
  status: undefined
})
</script>