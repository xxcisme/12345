<script setup>
import { useTable } from '@/utils/composables/useTable'
import { getAdminNewsList, deleteAdminNews } from '@/api/admin/bulletin'
import { useConfirm } from '@/utils/composables/useConfirm'

const { list, total, loading, pageNo, pageSize, params, handleSizeChange, handleCurrentChange } = useTable(getAdminNewsList, {
  title: ''
})

const { handleDelete } = useConfirm(deleteAdminNews, () => {
  handleCurrentChange(pageNo.value)
})
</script>