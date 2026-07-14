<template>
  <el-container class="admin-container">
    <!-- 侧边栏 -->
    <el-aside :width="sidebarWidth" class="sidebar">
      <div class="logo">
        <span>实训管理后台</span>
      </div>
      <el-menu
        :default-active="$route.path"
        router
        background-color="#ffffff"
        text-color="#303133"
        active-text-color="#409EFF"
      >
        <!-- 用户中心 -> 用户管理 -->
        <el-menu-item index="/admin/system/users">
          <el-icon><User /></el-icon>
          <span>用户中心</span>
        </el-menu-item>
        <!-- 资源中心 -> 媒体资源管理 (默认) -->
        <el-menu-item index="/admin/resource/media">
          <el-icon><FolderOpened /></el-icon>
          <span>资源中心</span>
        </el-menu-item>
        <!-- 实训中心 -> 课程管理 -->
        <el-menu-item index="/admin/training/courses">
          <el-icon><Reading /></el-icon>
          <span>实训中心</span>
        </el-menu-item>
        <!-- 新闻公告 -> 新闻管理 -->
        <el-menu-item index="/admin/bulletin/news">
          <el-icon><Document /></el-icon>
          <span>新闻公告</span>
        </el-menu-item>
        <!-- 系统管理 -> 日志管理 -->
        <el-menu-item index="/admin/system/logs">
          <el-icon><Setting /></el-icon>
          <span>系统管理</span>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <!-- 主内容区 -->
    <el-container>
      <el-header height="60px" class="admin-header">
        <div class="header-left">
          <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>{{ $route.meta.title || '页面' }}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <div class="header-right">
          <el-dropdown @command="handleCommand">
            <span class="user-info">
              {{ userStore.username || '管理员' }}
              <el-icon><CaretBottom /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">个人信息</el-dropdown-item>
                <el-dropdown-item command="logout" divided>退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <el-main class="admin-main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()
const sidebarWidth = ref('220px')

const handleCommand = (cmd) => {
  if (cmd === 'logout') {
    userStore.logout()
    router.push('/login')
  } else if (cmd === 'profile') {
    router.push('/user/profile')
  }
}
</script>

<style scoped lang="scss">
@import '@/styles/variables.scss';

.admin-container {
  height: 100vh;
}
.sidebar {
  background-color: #ffffff;
  border-right: 1px solid #eaeefb;
  .logo {
    height: $header-height;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 18px;
    font-weight: 600;
    color: $primary-color;
    border-bottom: 1px solid #eaeefb;
  }
}
.admin-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: #fff;
  border-bottom: 1px solid #eaeefb;
  padding: 0 20px;
  .user-info {
    cursor: pointer;
    display: flex;
    align-items: center;
    gap: 4px;
  }
}
.admin-main {
  background-color: $bg-color;
  padding: 20px;
}
</style>