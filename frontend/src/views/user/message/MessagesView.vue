<script setup>
import { useTable } from '@/utils/composables/useTable'
import { getMessages, markMessagesRead } from '@/api/user'
import { useMessageRead } from '@/utils/composables/useMessageRead'

const { list, total, loading, pageNo, pageSize, params, handleSizeChange, handleCurrentChange, loadData } = useTable(getMessages, {
  type: undefined,
  isRead: undefined
})

const { markRead } = useMessageRead(markMessagesRead, loadData)

const markAll = () => markRead([])
</script>