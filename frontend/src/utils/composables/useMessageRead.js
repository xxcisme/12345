import { ref } from 'vue'
import { ElMessage } from 'element-plus'

export function useMessageRead(markApi, loadMessages) {
    const markLoading = ref(false)

    const markRead = async (ids) => {
        markLoading.value = true
        try {
            const res = await markApi({ messageIds: ids })
            ElMessage.success(`已标记 ${res.data?.updatedCount || 0} 条为已读`)
            loadMessages()
        } finally {
            markLoading.value = false
        }
    }

    return { markRead, markLoading }
}