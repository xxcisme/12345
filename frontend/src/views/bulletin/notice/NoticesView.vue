<script setup>
import { useTable } from '@/utils/composables/useTable'
import { getNoticesList } from '@/api/bulletin'

const { list, total, loading, pageNo, pageSize, params, handleSizeChange, handleCurrentChange } = useTable(getNoticesList)
</script>