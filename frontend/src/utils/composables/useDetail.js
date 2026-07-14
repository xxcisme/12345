import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'

export function useDetail(fetchApi, _errorMsg, { autoLoad = true } = {}) {
    const route = useRoute()
    const detail = ref(null)
    const loading = ref(false)

    const loadDetail = async () => {
        loading.value = true
        try {
            const res = await fetchApi(route.params.id)
            detail.value = res.data
            return res.data
        } catch {
            // 错误已由拦截器处理
        } finally {
            loading.value = false
        }
    }

    if (autoLoad) {
        onMounted(loadDetail)
    }

    return { detail, loading, loadDetail }
}