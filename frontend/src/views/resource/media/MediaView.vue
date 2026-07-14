<template>
  <page-container>
    <base-search>
      <template #left>
        <el-input v-model="params.name" placeholder="资源名称" clearable style="width: 180px;" />
        <el-select v-model="params.type" placeholder="资源类型" clearable style="width: 140px;">
          <el-option label="视频" :value="1" />
          <el-option label="音频" :value="2" />
          <el-option label="文档" :value="3" />
        </el-select>
        <el-input v-model="params.category" placeholder="专业大类" clearable style="width: 150px;" />
        <el-select v-model="params.isShared" placeholder="共享状态" clearable style="width: 120px;">
          <el-option label="开放共享" :value="true" />
          <el-option label="非共享" :value="false" />
        </el-select>
        <el-select v-model="params.sortBy" placeholder="排序" style="width: 140px;">
          <el-option label="最新发布" value="createTime" />
          <el-option label="评分最高" value="score" />
        </el-select>
        <el-button type="primary" @click="loadData()">查询</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </template>
    </base-search>

    <el-row :gutter="20" style="margin-top:10px;">
      <el-col :xs="24" :sm="12" :md="8" :lg="6" v-for="item in list" :key="item.id" style="margin-bottom:20px;">
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
import { useRouter } from 'vue-router'
import { useTable } from '@/utils/composables/useTable'
import { getResources } from '@/api/resource'
import { VideoPlay, Headset, Document } from '@element-plus/icons-vue'
import PageContainer from '@/components/PageContainer/index.vue'
import BaseSearch from '@/components/BaseSearch/index.vue'

const router = useRouter()

const { list, total, loading, pageNo, pageSize, params, handleSizeChange, handleCurrentChange, loadData } = useTable(getResources, {
  name: '',
  type: undefined,
  category: '',
  isShared: undefined,
  sortBy: 'createTime',
  order: 'desc'
})

const resetSearch = () => {
  params.value = {
    name: '',
    type: undefined,
    category: '',
    isShared: undefined,
    sortBy: 'createTime',
    order: 'desc'
  }
  loadData()
}

const handleDetail = (row) => {
  router.push(`/resources/media/${row.id}`)
}
</script>

<style scoped lang="scss">
</style>