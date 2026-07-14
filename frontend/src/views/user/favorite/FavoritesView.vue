<script setup>
import { useTable } from '@/utils/composables/useTable'
import { getFavorites } from '@/api/user'
import { cancelFavorite } from '@/api/user'
import { useConfirm } from '@/utils/composables/useConfirm'

const { list, total, loading, pageNo, pageSize, params, handleSizeChange, handleCurrentChange } = useTable(getFavorites, {
  resourceType: undefined
})

const { handleDelete } = useConfirm(
  (id) => cancelFavorite(id),
  () => handleCurrentChange(pageNo.value)
)
</script>