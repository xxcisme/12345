import { ref } from 'vue'
import { ElMessage } from 'element-plus'

export function useMessageRead(markApi, loadMessages) {
    const markLoading = ref(false)

    const markRead = async (ids) => {
        markLoading.value = true
        try {
            const res = await markApi({ messageIds: ids })
            const updatedCount = res.data?.updatedCount ?? res.updatedCount ?? ids.length
            ElMessage.success(`已标记 ${updatedCount} 条为已读`)
            loadMessages()
        } finally {
            markLoading.value = false
        }
    }

    return { markRead, markLoading }
}