import { createRouter, createWebHistory } from 'vue-router'
import { ROLE_MAP } from '@/utils/constants'

const routes = [
  // 认证
  {
    path: '/login',
    name: '登录',
    component: () => import('@/views/auth/LoginView.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/register',
    name: '注册',
    component: () => import('@/views/auth/RegisterView.vue'),
    meta: { requiresAuth: false }
  },

  // 首页
  {
    path: '/',
    name: '首页',
    component: () => import('@/views/home/HomeView.vue'),
    meta: { requiresAuth: false }
  },

  // 前台新闻
  {
    path: '/news',
    name: '新闻',
    component: () => import('@/views/bulletin/news/NewsView.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/news/:id',
    name: '新闻详情',
    component: () => import('@/views/bulletin/news/NewsDetailView.vue'),
    meta: { requiresAuth: false }
  },
  // 前台公告
  {
    path: '/notices',
    name: '公告',
    component: () => import('@/views/bulletin/notice/NoticesView.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/notices/:id',
    name: '公告详情',
    component: () => import('@/views/bulletin/notice/NoticeDetailView.vue'),
    meta: { requiresAuth: false }
  },

  // 前台媒体资源
  {
    path: '/resources/media',
    name: '媒体资源',
    component: () => import('@/views/resource/media/MediaView.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/resources/media/:id',
    name: '媒体资源详情',
    component: () => import('@/views/resource/media/MediumDetailView.vue'),
    meta: { requiresAuth: false }
  },
  //前台实验室
  {
    path: '/resources/laboratories',
    name: '实验室',
    component: () => import('@/views/resource/laboratory/LaboratoriesView.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/resources/laboratories/:id',
    name: '实验室详情',
    component: () => import('@/views/resource/laboratory/LaboratoryDetailView.vue'),
    meta: { requiresAuth: false }
  },
  //前台设备
  {
    path: '/resources/devices',
    name: '设备',
    component: () => import('@/views/resource/device/DevicesView.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/resources/devices/:id',
    name: '设备详情',
    component: () => import('@/views/resource/device/DeviceDetailView.vue'),
    meta: { requiresAuth: false }
  },

  // 用户中心
  {
    path: '/user/profile',
    name: '个人信息',
    component: () => import('@/views/user/profile/ProfileView.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/user/password',
    name: '修改密码',
    component: () => import('@/views/user/profile/setting/ChangePasswordView.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/user/favorites',
    name: '收藏',
    component: () => import('@/views/user/favorite/FavoritesView.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/user/messages',
    name: '通知',
    component: () => import('@/views/user/message/MessagesView.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/user/courses',
    name: '我的课程',
    component: () => import('@/views/user/training/course/MyCoursesView.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/user/courses/:id',
    name: '我的课程详情',
    component: () => import('@/views/user/training/course/MyCourseDetailView.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/user/experiments',
    name: '我的实验',
    component: () => import('@/views/user/training/experiment/MyExperimentsView.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/user/experiments/:id',
    name: '我的实验详情',
    component: () => import('@/views/user/training/experiment/MyExperimentDetailView.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/user/lab-applications',
    name: '实验室申请',
    component: () => import('@/views/user/resource/laboratory/lab-application/LabApplicationsView.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/user/lab-applications/new',
    name: '实验室申请表',
    component: () => import('@/views/user/resource/laboratory/lab-application/LabApplicationFormView.vue'),
    meta: { requiresAuth: true }
  },


  // 后台管理
  {
    path: '/admin',
    redirect: '/admin/bulletin/news'
  },

  // 新闻管理
  {
    path: '/admin/bulletin/news',
    name: '管理新闻',
    component: () => import('@/views/admin/bulletin/news/AdminNewsView.vue'),
    meta: { requiresAuth: true, roles: ['admin'] }
  },
  {
    path: '/admin/bulletin/news/new',
    name: '添加新闻',
    component: () => import('@/views/admin/bulletin/news/AdminNewsFormView.vue'),
    meta: { requiresAuth: true, roles: ['admin'] }
  },
  {
    path: '/admin/bulletin/news/edit/:id',
    name: '编辑新闻',
    component: () => import('@/views/admin/bulletin/news/AdminNewsFormView.vue'),
    meta: { requiresAuth: true, roles: ['admin'] }
  },
  // 公告管理
  {
    path: '/admin/bulletin/notice',
    name: '管理公告',
    component: () => import('@/views/admin/bulletin/notice/AdminNoticesView.vue'),
    meta: { requiresAuth: true, roles: ['admin'] }
  },
  {
    path: '/admin/bulletin/notice/new',
    name: '添加公告',
    component: () => import('@/views/admin/bulletin/notice/AdminNoticeFormView.vue'),
    meta: { requiresAuth: true, roles: ['admin'] }
  },
  {
    path: '/admin/bulletin/notice/edit/:id',
    name: '编辑公告',
    component: () => import('@/views/admin/bulletin/notice/AdminNoticeFormView.vue'),
    meta: { requiresAuth: true, roles: ['admin'] }
  },

  // 媒体资源管理
  {
    path: '/admin/resource/media',
    name: '管理媒体资源',
    component: () => import('@/views/admin/resource/media/AdminMediaView.vue'),
    meta: { requiresAuth: true, roles: ['admin', 'teacher'] }
  },
  {
    path: '/admin/resource/media/new',
    name: '添加媒体资源',
    component: () => import('@/views/admin/resource/media/AdminMediumFormView.vue'),
    meta: { requiresAuth: true, roles: ['admin', 'teacher'] }
  },
  {
    path: '/admin/resource/media/edit/:id',
    name: '编辑媒体资源信息',
    component: () => import('@/views/admin/resource/media/AdminMediumFormView.vue'),
    meta: { requiresAuth: true, roles: ['admin', 'teacher'] }
  },

  // 实验室管理
  {
    path: '/admin/resource/laboratories',
    name: '管理实验室',
    component: () => import('@/views/resource/laboratory/LaboratoriesView.vue'),
    meta: { requiresAuth: true, roles: ['admin'] }
  },
  {
    path: '/admin/resource/laboratory/new',
    name: '添加实验室',
    component: () => import('@/views/admin/resource/laboratory/AdminLaboratoryFormView.vue'),
    meta: { requiresAuth: true, roles: ['admin'] }
  },
  {
    path: '/admin/resource/laboratory/edit/:id',
    name: '编辑实验室信息',
    component: () => import('@/views/admin/resource/laboratory/AdminLaboratoryFormView.vue'),
    meta: { requiresAuth: true, roles: ['admin'] }
  },
  {
    path: '/admin/resource/lab-applications',
    name: '管理实验室申请',
    component: () => import('@/views/admin/resource/laboratory/lab-application/AdminLabApplicationsView.vue'),
    meta: { requiresAuth: true, roles: ['admin'] }
  },
  // 设备管理
  {
    path: '/admin/resource/devices',
    name: '管理设备',
    component: () => import('@/views/resource/device/DevicesView.vue'),
    meta: { requiresAuth: true, roles: ['admin'] }
  },
  {
    path: '/admin/resource/device/new',
    name: '添加设备',
    component: () => import('@/views/admin/resource/device/AdminDeviceFormView.vue'),
    meta: { requiresAuth: true, roles: ['admin'] }
  },
  {
    path: '/admin/resource/device/edit/:id',
    name: '编辑设备信息',
    component: () => import('@/views/admin/resource/device/AdminDeviceFormView.vue'),
    meta: { requiresAuth: true, roles: ['admin'] }
  },

  // 系统日志
  {
    path: '/admin/system/logs',
    name: '系统日志',
    component: () => import('@/views/admin/system/log/AdminLogsView.vue'),
    meta: { requiresAuth: true, roles: ['admin'] }
  },
  // 用户管理
  {
    path: '/admin/system/users',
    name: '管理用户',
    component: () => import('@/views/admin/system/user/AdminUsersView.vue'),
    meta: { requiresAuth: true, roles: ['admin'] }
  },
  {
    path: '/admin/system/users/new',
    name: '添加用户',
    component: () => import('@/views/admin/system/user/AdminUserFormView.vue'),
    meta: { requiresAuth: true, roles: ['admin'] }
  },
  {
    path: '/admin/system/users/edit/:id',
    name: '编辑用户信息',
    component: () => import('@/views/admin/system/user/AdminUserFormView.vue'),
    meta: { requiresAuth: true, roles: ['admin'] }
  },

  // 课程管理
  {
    path: '/admin/training/courses',
    name: '管理课程',
    component: () => import('@/views/admin/training/course/AdminCoursesView.vue'),
    meta: { requiresAuth: true, roles: ['admin', 'teacher'] }
  },
  {
    path: '/admin/training/courses/new',
    name: '添加课程',
    component: () => import('@/views/admin/training/course/AdminCourseFormView.vue'),
    meta: { requiresAuth: true, roles: ['admin', 'teacher'] }
  },
  {
    path: '/admin/training/courses/edit/:id',
    name: '编辑课程信息',
    component: () => import('@/views/admin/training/course/AdminCourseFormView.vue'),
    meta: { requiresAuth: true, roles: ['admin', 'teacher'] }
  },
  // 实验管理
  {
    path: '/admin/training/experiments',
    name: '管理实验',
    component: () => import('@/views/admin/training/experiment/AdminExperimentsView.vue'),
    meta: { requiresAuth: true, roles: ['admin', 'teacher'] }
  },
  {
    path: '/admin/training/experiments/new',
    name: '添加实验',
    component: () => import('@/views/admin/training/experiment/AdminExperimentFormView.vue'),
    meta: { requiresAuth: true, roles: ['admin', 'teacher'] }
  },
  {
    path: '/admin/training/experiments/edit/:id',
    name: '编辑实验信息',
    component: () => import('@/views/admin/training/experiment/AdminExperimentFormView.vue'),
    meta: { requiresAuth: true, roles: ['admin', 'teacher'] }
  },
  // 成绩管理
  {
    path: '/admin/training/grades',
    name: '管理成绩',
    component: () => import('@/views/admin/training/grade/AdminGradesView.vue'),
    meta: { requiresAuth: true, roles: ['admin', 'teacher'] }
  },
  // 教师管理
  {
    path: '/admin/training/teachers',
    name: '管理教师',
    component: () => import('@/views/admin/training/teacher/AdminTeachersView.vue'),
    meta: { requiresAuth: true, roles: ['admin'] }
  },
  {
    path: '/admin/training/teachers/new',
    name: '添加教师',
    component: () => import('@/views/admin/training/teacher/AdminTeacherFormView.vue'),
    meta: { requiresAuth: true, roles: ['admin'] }
  },
  {
    path: '/admin/training/teachers/edit/:id',
    name: '编辑教师信息',
    component: () => import('@/views/admin/training/teacher/AdminTeacherFormView.vue'),
    meta: { requiresAuth: true, roles: ['admin'] }
  },
  {
    path: '/admin/training/teachers/:id',
    name: '教师详情',
    component: () => import('@/views/admin/training/teacher/AdminTeacherDetailView.vue'),
    meta: { requiresAuth: true, roles: ['admin'] }
  },
  // 教学计划管理
  {
    path: '/admin/training/teaching-plans',
    name: '管理教学计划',
    component: () => import('@/views/admin/training/teaching-plan/AdminTeachingPlansView.vue'),
    meta: { requiresAuth: true, roles: ['admin', 'teacher'] }
  },
  {
    path: '/admin/training/teaching-plans/new',
    name: '添加教学计划',
    component: () => import('@/views/admin/training/teaching-plan/AdminTeachingPlanFormView.vue'),
    meta: { requiresAuth: true, roles: ['admin', 'teacher'] }
  },
  {
    path: '/admin/training/teaching-plans/edit/:id',
    name: '编辑教学计划',
    component: () => import('@/views/admin/training/teaching-plan/AdminTeachingPlanFormView.vue'),
    meta: { requiresAuth: true, roles: ['admin', 'teacher'] }
  }

  // // 错误页面 暂不实现，留作备用
  // // 404
  // {
  //   path: '/:pathMatch(.*)*',
  //   name: 'NotFound',
  //   component: () => import('@/views/error/NotFoundView.vue'),
  //   meta: { requiresAuth: false }
  // }
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})

// 路由守卫：检查登录状态与角色权限
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  const user = JSON.parse(localStorage.getItem('user') || 'null')

  if (to.meta.requiresAuth && !token) {
    next({ name: '登录', query: { redirect: to.fullPath } })
    return
  }

  if (to.meta.roles && user) {
    const userRole = ROLE_MAP[user.role] || ''
    if (!to.meta.roles.includes(userRole)) {
      next({ name: '首页' })
      return
    }
  }

  next()
})

export default router