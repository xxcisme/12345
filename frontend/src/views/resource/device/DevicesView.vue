<script setup>
import { useTable } from '@/utils/composables/useTable'
import { getDevices } from '@/api/resource'
import { deleteAdminDevice } from '@/api/admin/resource'
import { useConfirm } from '@/utils/composables/useConfirm'
import { useRoute } from 'vue-router'

const route = useRoute()
const isAdmin = route.path.startsWith('/admin')

const { list, total, loading, pageNo, pageSize, params, handleSizeChange, handleCurrentChange } = useTable(getDevices, {
  name: '',
  number: '',
  laboratoryId: undefined,
  type: '',
  status: undefined
})

const { handleDelete } = useConfirm(deleteAdminDevice, () => {
  handleCurrentChange(pageNo.value)
})
</script>