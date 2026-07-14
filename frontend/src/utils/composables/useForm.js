import { reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'

export function useForm(createApi, updateApi, onSuccess) {
    const form = reactive({})
    const formRef = ref(null)
    const submitting = ref(false)
    const isEdit = ref(false)

    const resetForm = () => {
        Object.keys(form).forEach(key => delete form[key])
        isEdit.value = false
    }

    const setFormData = (data) => {
        Object.assign(form, data)
        isEdit.value = !!data.id
    }

    const submit = async () => {
        if (formRef.value) {
            await formRef.value.validate()
        }
        submitting.value = true
        try {
            const api = isEdit.value ? updateApi : createApi
            const res = await api(form)
            ElMessage.success(isEdit.value ? '修改成功' : '新增成功')
            onSuccess && onSuccess(res)
            return res
        } catch (e) {
            // 错误已处理
        } finally {
            submitting.value = false
        }
    }

    return {
        form,
        formRef,
        submitting,
        isEdit,
        resetForm,
        setFormData,
        submit
    }
}