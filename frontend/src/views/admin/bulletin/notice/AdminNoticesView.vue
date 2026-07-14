<script setup>
import { useTable } from '@/utils/composables/useTable'
import { getAdminNoticesList, deleteAdminNotice } from '@/api/admin/bulletin'
import { useConfirm } from '@/utils/composables/useConfirm'

const { list, total, loading, pageNo, pageSize, params, handleSizeChange, handleCurrentChange } = useTable(getAdminNoticesList, {
  title: ''
})

const { handleDelete } = useConfirm(deleteAdminNotice, () => {
  handleCurrentChange(pageNo.value)
})
</script>