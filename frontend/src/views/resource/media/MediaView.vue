<template>
  <page-container>
    <base-search>
      <template #left>
        <el-input v-model="searchForm.name" placeholder="资源名称" clearable style="width: 180px;" />
        <el-select v-model="searchForm.type" placeholder="资源类型" clearable style="width: 140px;">
          <el-option label="视频" :value="1" />
          <el-option label="音频" :value="2" />
          <el-option label="文档" :value="3" />
        </el-select>
        <el-input v-model="searchForm.category" placeholder="专业大类" clearable style="width: 150px;" />
        <el-select v-model="searchForm.isShared" placeholder="共享状态" clearable style="width: 120px;">
          <el-option label="开放共享" :value="true" />
          <el-option label="非共享" :value="false" />
        </el-select>
        <el-select v-model="searchForm.sortBy" placeholder="排序" style="width: 140px;">
          <el-option label="最新发布" value="createTime" />
          <el-option label="评分最高" value="score" />
        </el-select>
        <el-button type="primary" @click="handleSearch">查询</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </template>
    </base-search>

    <el-row :gutter="20" style="margin-top:10px;">
      <el-col :xs="24" :sm="12" :md="8" :lg="6" v-for="item in tableData" :key="item.id" style="margin-bottom:20px;">
        <el-card :body-style="{ padding: '0px' }" shadow="hover" @click="handleDetail(item)" style="cursor:pointer;">
          <div style="height:120px; background:linear-gradient(135deg, #667eea 0%, #764ba2 100%); display:flex; align-items:center; justify-content:center;">
            <el-icon size="48" color="#fff"><video-play v-if="item.type === 1" /><headset v-else-if="item.type === 2" /><document /></el-icon>
          </div>
          <div style="padding:14px;">
            <div style="font-weight:bold; margin-bottom:8px; overflow:hidden; text-overflow:ellipsis; white-space:nowrap;">{{ item.name }}</div>
            <div style="display:flex; justify-content:space-between; align-items:center; color:#999; font-size:12px;">
              <span>{{ item.category }}</span>
              <span>
                <el-rate v-model="item.score" disabled size="small" />
                {{ item.score }}
              </span>
            </div>
            <div style="margin-top:8px; display:flex; justify-content:space-between; align-items:center;">
              <el-tag v-if="item.isShared" type="success" size="small">共享</el-tag>
              <el-tag v-else type="info" size="small">非共享</el-tag>
              <span style="color:#999; font-size:12px;">{{ item.school }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-pagination
      v-model:current-page="pageNo"
      v-model:page-size="pageSize"
      :page-sizes="[12, 24, 48]"
      :total="total"
      layout="total, sizes, prev, pager, next, jumper"
      style="margin-top: 15px; justify-content: flex-end;"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
  </page-container>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { VideoPlay, Headset, Document } from '@element-plus/icons-vue'
import PageContainer from '@/components/PageContainer/index.vue'
import BaseSearch from '@/components/BaseSearch/index.vue'

const router = useRouter()

const loading = ref(false)
const pageNo = ref(1)
const pageSize = ref(12)
const total = ref(0)

const searchForm = reactive({
  name: '',
  type: null,
  category: '',
  isShared: null,
  sortBy: 'createTime',
  order: 'desc'
})

const tableData = ref([])

const fetchData = () => {
  loading.value = true
  setTimeout(() => {
    tableData.value = [
      { id: 1, name: 'Python基础编程视频教程', type: 1, category: '计算机类', school: 'XX大学', score: 4.5, isShared: true },
      { id: 2, name: '电路分析实验指导音频', type: 2, category: '电子信息类', school: 'XX学院', score: 3.8, isShared: false },
      { id: 3, name: '机械设计基础文档', type: 3, category: '机械类', school: 'XX工业大学', score: 4.2, isShared: true },
      { id: 4, name: '数据结构算法视频', type: 1, category: '计算机类', school: 'XX大学', score: 4.8, isShared: true },
      { id: 5, name: '自动控制原理音频讲解', type: 2, category: '自动化类', school: 'XX科技大学', score: 4.0, isShared: false },
      { id: 6, name: '嵌入式系统设计文档', type: 3, category: '电子信息类', school: 'XX学院', score: 4.3, isShared: true }
    ]
    total.value = 6
    loading.value = false
  }, 500)
}

const handleSearch = () => { pageNo.value = 1; fetchData() }
const resetSearch = () => {
  Object.assign(searchForm, { name: '', type: null, category: '', isShared: null, sortBy: 'createTime' })
  handleSearch()
}

const handleDetail = (row) => {
  router.push(`/resources/media/${row.id}`)
}

const handleSizeChange = (val) => { pageSize.value = val; fetchData() }
const handleCurrentChange = (val) => { pageNo.value = val; fetchData() }

onMounted(() => { fetchData() })
</script>

<style scoped lang="scss">
</style>