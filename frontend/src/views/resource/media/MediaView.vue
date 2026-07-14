<script setup>
import { useTable } from '@/utils/composables/useTable'
import { getResources } from '@/api/resource'

const { list, total, loading, pageNo, pageSize, params, handleSizeChange, handleCurrentChange, resetParams } = useTable(getResources, {
  name: '',
  type: undefined,
  category: '',
  isShared: undefined,
  sortBy: 'createTime',
  order: 'desc'
})
</script>