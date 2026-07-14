<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getProfile, updateProfile } from '@/api/user'

const profile = ref({})
const editMode = ref(false)
const form = ref({ phone: '', email: '', realName: '' })

onMounted(async () => {
  try {
    const res = await getProfile()
    profile.value = res.data
    Object.assign(form.value, res.data)
  } catch (error) {
    ElMessage.error('加载个人信息失败')
  }
})

const handleEdit = () => { editMode.value = true }

const handleSave = async () => {
  try {
    await updateProfile(form.value)
    ElMessage.success('更新成功')
    const res = await getProfile()
    profile.value = res.data
    editMode.value = false
  } catch (error) {
    ElMessage.error('更新失败')
  }
}
</script>