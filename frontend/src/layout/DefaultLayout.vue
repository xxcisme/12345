<template>
  <div class="default-layout">
    <header class="header">
      <div class="logo">实训平台</div>
      <nav class="nav">
        <router-link to="/">首页</router-link>
        <router-link to="/resources/media">资源中心</router-link>
        <router-link to="/resources/laboratories">实训中心</router-link>
        <router-link to="/news">新闻公告</router-link>
      </nav>
      <div class="user-actions">
        <template v-if="!userStore.isLoggedIn">
          <el-button type="primary" size="small" @click="goLogin">登录</el-button>
          <el-button size="small" @click="goRegister">注册</el-button>
        </template>
        <template v-else>
          <el-dropdown @command="handleUserCommand">
            <span class="username">{{ userStore.username }}</span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">个人中心</el-dropdown-item>
                <el-dropdown-item command="logout">退出</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </template>
      </div>
    </header>
    <main class="main-content">
      <router-view />
    </main>
    <footer class="footer">
      © 2026 实训平台 All Rights Reserved.
    </footer>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()

const goLogin = () => router.push('/login')
const goRegister = () => router.push('/register')
const handleUserCommand = (cmd) => {
  if (cmd === 'logout') {
    userStore.logout()
    router.push('/login')
  } else {
    router.push('/user/profile')
  }
}
</script>

<style scoped lang="scss">
@import '@/styles/variables.scss';

.default-layout {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: $header-height;
  padding: 0 40px;
  background: #fff;
  border-bottom: 1px solid $border-color;
  .logo {
    font-size: 20px;
    font-weight: 600;
    color: $primary-color;
  }
  .nav {
    display: flex;
    gap: 30px;
    a {
      text-decoration: none;
      color: $text-color;
      font-weight: 500;
      &.router-link-active {
        color: $primary-color;
      }
    }
  }
  .user-actions {
    display: flex;
    gap: 10px;
    .username {
      cursor: pointer;
    }
  }
}

.main-content {
  flex: 1;
  padding: 20px;
  background-color: $bg-color;
}

.footer {
  text-align: center;
  padding: 20px;
  border-top: 1px solid $border-color;
  background: #fff;
  color: #909399;
  font-size: 14px;
}
</style>