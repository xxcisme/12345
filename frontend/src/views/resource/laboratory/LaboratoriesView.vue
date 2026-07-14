<script setup>
import { useTable } from '@/utils/composables/useTable'
import { getLaboratories } from '@/api/resource'
import { deleteAdminLaboratory } from '@/api/admin/resource'
import { useConfirm } from '@/utils/composables/useConfirm'

const { list, total, loading, pageNo, pageSize, params, handleSizeChange, handleCurrentChange } = useTable(getLaboratories, {
  name: '',
  number: '',
  address: '',
  minStation: undefined
})

const { handleDelete } = useConfirm(deleteAdminLaboratory, () => handleCurrentChange(pageNo.value))
</script>