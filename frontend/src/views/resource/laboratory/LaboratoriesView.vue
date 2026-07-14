<script setup>
import { useTable } from '@/utils/composables/useTable'
import { getLaboratories } from '@/api/resource'
import { useConfirm } from '@/utils/composables/useConfirm'
import { deleteAdminLaboratory } from '@/api/admin/resource'
import { useRoute } from 'vue-router'

// 通过路由判断是否后台管理
const route = useRoute()
const isAdmin = route.path.startsWith('/admin')

const { list, total, loading, pageNo, pageSize, params, handleSizeChange, handleCurrentChange } = useTable(getLaboratories, {
  name: '',
  number: '',
  address: '',
  minStation: undefined
})

// 删除（仅后台）
const { handleDelete } = useConfirm(deleteAdminLaboratory, () => {
  // 重新加载
  handleCurrentChange(pageNo.value)
})
</script>