<script setup>
import { RouterLink, RouterView } from 'vue-router'
import { ref, computed } from 'vue'
import { getUser, clearAuth } from '@/utils/local_storage'
import { useRouter } from 'vue-router'
import { ROLE_MAP } from '@/utils/constants'
import { logout } from '@/api/auth'

const router = useRouter()
const user = ref(getUser())

const isLoggedIn = computed(() => !!user.value)
const isAdmin = computed(() => user.value && ROLE_MAP[user.value.role] === 'admin')
const isTeacher = computed(() => user.value && ROLE_MAP[user.value.role] === 'teacher')

const handleLogout = async () => {
  try {
    await logout()
  } catch {
    // 即使 logout 失败也清除本地登录状态
  }
  clearAuth()
  user.value = null
  router.push('/')
}

router.afterEach(() => {
  user.value = getUser()
})

const navModules = computed(() => {
  const modules = [
    {
      label: '新闻公告',
      items: [
        { label: '新闻', path: isAdmin.value ? '/admin/bulletin/news' : '/news' },
        { label: '公告', path: isAdmin.value ? '/admin/bulletin/notice' : '/notices' }
      ]
    },
    {
      label: '资源中心',
      items: [
        { label: '媒体资源', path: (isAdmin.value || isTeacher.value) ? '/admin/resource/media' : '/resources/media' },
        { label: '实验室', path: isAdmin.value ? '/admin/resource/laboratories' : '/resources/laboratories' },
        { label: '设备', path: isAdmin.value ? '/admin/resource/devices' : '/resources/devices' },
        ...(isAdmin.value ? [{ label: '实验室申请审批', path: '/admin/resource/lab-applications' }] : [])
      ]
    }
  ]

  if (isLoggedIn.value) {
    const trainingItems = []
    if (isTeacher.value) {
      trainingItems.push({ label: '教学计划', path: '/admin/training/teaching-plans' })
    }
    trainingItems.push({ label: '课程', path: (isAdmin.value || isTeacher.value) ? '/admin/training/courses' : '/user/courses' })
    trainingItems.push({ label: '实验', path: (isAdmin.value || isTeacher.value) ? '/admin/training/experiments' : '/user/experiments' })
    if (isTeacher.value) {
      trainingItems.push({ label: '成绩评定', path: '/admin/training/grades' })
    }
    if (isAdmin.value) {
      trainingItems.push({ label: '师资', path: '/admin/training/teachers' })
    }
    modules.push({
      label: '实训中心',
      items: trainingItems
    })

    const personalItems = [
      { label: '个人信息', path: '/user/profile' },
      { label: '通知', path: '/user/messages' }
    ]
    if (!isAdmin.value) {
      personalItems.push(
        { label: '收藏', path: '/user/favorites' },
        { label: '我的申请', path: '/user/lab-applications' },
        { label: '我的课程', path: '/user/courses' },
        { label: '我的实验', path: '/user/experiments' }
      )
    }
    modules.push({
      label: '个人中心',
      items: personalItems
    })
  }

  if (isAdmin.value) {
    modules.push({
      label: '系统管理',
      items: [
        { label: '系统日志', path: '/admin/system/logs' },
        { label: '用户管理', path: '/admin/system/users' }
      ]
    })
  }

  return modules
})
</script>

<template>
  <div class="app-wrapper">
    <header class="app-header">
      <div class="header-left">
        <RouterLink to="/" class="logo">虚拟实训平台</RouterLink>
      </div>
      <nav class="header-nav">
        <div v-for="mod in navModules" :key="mod.label" class="nav-item">
          <span class="nav-label">{{ mod.label }}</span>
          <span class="nav-arrow">▾</span>
          <div class="nav-dropdown">
            <RouterLink
              v-for="item in mod.items"
              :key="item.path"
              :to="item.path"
              class="nav-dropdown-item"
            >
              {{ item.label }}
            </RouterLink>
          </div>
        </div>
      </nav>
      <div class="header-right">
        <template v-if="isLoggedIn">
          <RouterLink to="/user/profile" class="header-link">{{ user?.username }}</RouterLink>
          <RouterLink to="/user/messages" class="header-link">消息</RouterLink>
          <a class="header-link" @click="handleLogout">退出</a>
        </template>
        <template v-else>
          <RouterLink to="/login" class="header-link">登录</RouterLink>
          <RouterLink to="/register" class="header-link">注册</RouterLink>
        </template>
      </div>
    </header>
    <main class="app-main">
      <RouterView />
    </main>
  </div>
</template>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}
body {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
}
</style>

<style scoped>
.app-wrapper {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}
.app-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 40px;
  height: 56px;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);
  position: sticky;
  top: 0;
  z-index: 100;
}
.header-left {
  display: flex;
  align-items: center;
  flex-shrink: 0;
}
.logo {
  font-size: 18px;
  font-weight: 600;
  color: #409eff;
  text-decoration: none;
}
.header-nav {
  display: flex;
  align-items: center;
  gap: 4px;
  flex: 1;
  justify-content: center;
}
.nav-item {
  position: relative;
  padding: 0 14px;
  height: 56px;
  display: flex;
  align-items: center;
  gap: 4px;
  cursor: pointer;
  border-radius: 4px;
  transition: background 0.2s;
}
.nav-item:hover {
  background: #f0f5ff;
}
.nav-label {
  font-size: 14px;
  color: #303133;
  font-weight: 500;
}
.nav-arrow {
  font-size: 10px;
  color: #909399;
  transition: transform 0.2s;
}
.nav-item:hover .nav-arrow {
  transform: rotate(180deg);
}
.nav-dropdown {
  display: none;
  position: absolute;
  top: 100%;
  left: 0;
  min-width: 140px;
  background: #fff;
  border-radius: 6px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.12);
  padding: 6px 0;
  z-index: 200;
}
.nav-item:hover .nav-dropdown {
  display: block;
}
.nav-dropdown-item {
  display: block;
  padding: 10px 20px;
  font-size: 14px;
  color: #606266;
  text-decoration: none;
  transition: all 0.2s;
  white-space: nowrap;
}
.nav-dropdown-item:hover {
  background: #f0f5ff;
  color: #409eff;
}
.header-right {
  display: flex;
  align-items: center;
  gap: 20px;
  flex-shrink: 0;
}
.header-link {
  font-size: 14px;
  color: #606266;
  text-decoration: none;
  cursor: pointer;
  transition: color 0.2s;
  white-space: nowrap;
}
.header-link:hover {
  color: #409eff;
}
.app-main {
  flex: 1;
}
</style>