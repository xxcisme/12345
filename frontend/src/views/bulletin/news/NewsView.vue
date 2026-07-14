<script setup>
import { useTable } from '@/utils/composables/useTable'
import { getNewsList } from '@/api/bulletin'

const { list, total, loading, pageNo, pageSize, params, handleSizeChange, handleCurrentChange } = useTable(getNewsList)
</script>