import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  // 认证
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/auth/LoginView.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/auth/RegisterView.vue'),
    meta: { requiresAuth: false }
  },

  // 首页
  {
    path: '/',
    name: 'Home',
    component: () => import('@/views/home/HomeView.vue'),
    meta: { requiresAuth: false }
  },

  // 前台新闻
  {
    path: '/news',
    name: 'News',
    component: () => import('@/views/bulletin/news/NewsView.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/news/:id',
    name: 'NewsDetail',
    component: () => import('@/views/bulletin/news/NewsDetailView.vue'),
    meta: { requiresAuth: false }
  },
  // 前台公告
  {
    path: '/notices',
    name: 'Notices',
    component: () => import('@/views/bulletin/notice/NoticesView.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/notices/:id',
    name: 'NoticeDetail',
    component: () => import('@/views/bulletin/notice/NoticeDetailView.vue'),
    meta: { requiresAuth: false }
  },

  // 前台媒体资源
  {
    path: '/resources/media',
    name: 'Media',
    component: () => import('@/views/resource/media/MediaView.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/resources/media/:id',
    name: 'MediumDetail',
    component: () => import('@/views/resource/media/MediumDetailView.vue'),
    meta: { requiresAuth: false }
  },
  //前台实验室
  {
    path: '/resources/laboratories',
    name: 'Laboratories',
    component: () => import('@/views/resource/laboratory/LaboratoriesView.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/resources/laboratories/:id',
    name: 'LaboratoryDetail',
    component: () => import('@/views/resource/laboratory/LaboratoryDetailView.vue'),
    meta: { requiresAuth: false }
  },
  //前台设备
  {
    path: '/resources/devices',
    name: 'Devices',
    component: () => import('@/views/resource/device/DevicesView.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/resources/devices/:id',
    name: 'DeviceDetail',
    component: () => import('@/views/resource/device/DeviceDetailView.vue'),
    meta: { requiresAuth: false }
  },

  // 用户中心
  {
    path: '/user/profile',
    name: 'Profile',
    component: () => import('@/views/user/profile/ProfileView.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/user/password',
    name: 'ChangePassword',
    component: () => import('@/views/user/profile/setting/ChangePasswordView.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/user/favorites',
    name: 'Favorites',
    component: () => import('@/views/user/favorite/FavoritesView.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/user/messages',
    name: 'Messages',
    component: () => import('@/views/user/message/MessagesView.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/user/courses',
    name: 'MyCourses',
    component: () => import('@/views/user/training/course/MyCoursesView.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/user/courses/:id',
    name: 'MyCourseDetail',
    component: () => import('@/views/user/training/course/MyCourseDetailView.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/user/experiments',
    name: 'MyExperiments',
    component: () => import('@/views/user/training/experiment/MyExperimentsView.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/user/experiments/:id',
    name: 'MyExperimentDetail',
    component: () => import('@/views/user/training/experiment/MyExperimentDetailView.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/user/lab-applications',
    name: 'MyLabApplications',
    component: () => import('@/views/user/resource/laboratory/lab-application/MyLabApplicationsView.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/user/lab-applications/new',
    name: 'LabApplicationForm',
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
    name: 'AdminNews',
    component: () => import('@/views/admin/bulletin/news/AdminNewsView.vue'),
    meta: { requiresAuth: true, roles: ['admin'] }
  },
  {
    path: '/admin/bulletin/news/new',
    name: 'AdminNewsForm',
    component: () => import('@/views/admin/bulletin/news/AdminNewsFormView.vue'),
    meta: { requiresAuth: true, roles: ['admin'] }
  },
  {
    path: '/admin/bulletin/news/edit/:id',
    name: 'AdminNewsEdit',
    component: () => import('@/views/admin/bulletin/news/AdminNewsFormView.vue'),
    meta: { requiresAuth: true, roles: ['admin'] }
  },
  // 公告管理
  {
    path: '/admin/bulletin/notice',
    name: 'AdminNotices',
    component: () => import('@/views/admin/bulletin/notice/AdminNoticesView.vue'),
    meta: { requiresAuth: true, roles: ['admin'] }
  },
  {
    path: '/admin/bulletin/notice/new',
    name: 'AdminNoticeForm',
    component: () => import('@/views/admin/bulletin/notice/AdminNoticeFormView.vue'),
    meta: { requiresAuth: true, roles: ['admin'] }
  },
  {
    path: '/admin/bulletin/notice/edit/:id',
    name: 'AdminNoticeEdit',
    component: () => import('@/views/admin/bulletin/notice/AdminNoticeFormView.vue'),
    meta: { requiresAuth: true, roles: ['admin'] }
  },

  // 媒体资源管理
  {
    path: '/admin/resource/media',
    name: 'AdminMedia',
    component: () => import('@/views/admin/resource/media/AdminMediaView.vue'),
    meta: { requiresAuth: true, roles: ['admin', 'teacher'] }
  },
  {
    path: '/admin/resource/media/new',
    name: 'AdminMediumForm',
    component: () => import('@/views/admin/resource/media/AdminMediumFormView.vue'),
    meta: { requiresAuth: true, roles: ['admin', 'teacher'] }
  },
  {
    path: '/admin/resource/media/edit/:id',
    name: 'AdminMediumEdit',
    component: () => import('@/views/admin/resource/media/AdminMediumFormView.vue'),
    meta: { requiresAuth: true, roles: ['admin', 'teacher'] }
  },

  // 实验室管理
  {
    path: '/admin/resource/laboratory/new',
    name: 'AdminLaboratoryForm',
    component: () => import('@/views/admin/resource/laboratory/AdminLaboratoryFormView.vue'),
    meta: { requiresAuth: true, roles: ['admin'] }
  },
  {
    path: '/admin/resource/laboratory/edit/:id',
    name: 'AdminLaboratoryEdit',
    component: () => import('@/views/admin/resource/laboratory/AdminLaboratoryFormView.vue'),
    meta: { requiresAuth: true, roles: ['admin'] }
  },
  {
    path: '/admin/resource/lab-applications',
    name: 'AdminLabApplications',
    component: () => import('@/views/admin/resource/laboratory/lab-application/AdminLabApplicationsView.vue'),
    meta: { requiresAuth: true, roles: ['admin'] }
  },
  // 设备管理
  {
    path: '/admin/resource/device/new',
    name: 'AdminDeviceForm',
    component: () => import('@/views/admin/resource/device/AdminDeviceFormView.vue'),
    meta: { requiresAuth: true, roles: ['admin'] }
  },
  {
    path: '/admin/resource/device/edit/:id',
    name: 'AdminDeviceEdit',
    component: () => import('@/views/admin/resource/device/AdminDeviceFormView.vue'),
    meta: { requiresAuth: true, roles: ['admin'] }
  },

  // 系统日志
  {
    path: '/admin/system/logs',
    name: 'AdminLogs',
    component: () => import('@/views/admin/system/log/AdminLogsView.vue'),
    meta: { requiresAuth: true, roles: ['admin'] }
  },
  // 用户管理
  {
    path: '/admin/system/users',
    name: 'AdminUsers',
    component: () => import('@/views/admin/system/user/AdminUsersView.vue'),
    meta: { requiresAuth: true, roles: ['admin'] }
  },
  {
    path: '/admin/system/users/new',
    name: 'AdminUserForm',
    component: () => import('@/views/admin/system/user/AdminUserFormView.vue'),
    meta: { requiresAuth: true, roles: ['admin'] }
  },
  {
    path: '/admin/system/users/edit/:id',
    name: 'AdminUserEdit',
    component: () => import('@/views/admin/system/user/AdminUserFormView.vue'),
    meta: { requiresAuth: true, roles: ['admin'] }
  },

  // 课程管理
  {
    path: '/admin/training/courses',
    name: 'AdminCourses',
    component: () => import('@/views/admin/training/course/AdminCoursesView.vue'),
    meta: { requiresAuth: true, roles: ['admin', 'teacher'] }
  },
  {
    path: '/admin/training/courses/new',
    name: 'AdminCourseForm',
    component: () => import('@/views/admin/training/course/AdminCourseFormView.vue'),
    meta: { requiresAuth: true, roles: ['admin', 'teacher'] }
  },
  {
    path: '/admin/training/courses/edit/:id',
    name: 'AdminCourseEdit',
    component: () => import('@/views/admin/training/course/AdminCourseFormView.vue'),
    meta: { requiresAuth: true, roles: ['admin', 'teacher'] }
  },
  // 实验管理
  {
    path: '/admin/training/experiments',
    name: 'AdminExperiments',
    component: () => import('@/views/admin/training/experiment/AdminExperimentsView.vue'),
    meta: { requiresAuth: true, roles: ['admin', 'teacher'] }
  },
  {
    path: '/admin/training/experiments/new',
    name: 'AdminExperimentForm',
    component: () => import('@/views/admin/training/experiment/AdminExperimentFormView.vue'),
    meta: { requiresAuth: true, roles: ['admin', 'teacher'] }
  },
  {
    path: '/admin/training/experiments/edit/:id',
    name: 'AdminExperimentEdit',
    component: () => import('@/views/admin/training/experiment/AdminExperimentFormView.vue'),
    meta: { requiresAuth: true, roles: ['admin', 'teacher'] }
  },
  // 成绩管理
  {
    path: '/admin/training/grades',
    name: 'AdminGrades',
    component: () => import('@/views/admin/training/grade/AdminGradesView.vue'),
    meta: { requiresAuth: true, roles: ['admin', 'teacher'] }
  },
  // 教师管理
  {
    path: '/admin/training/teachers',
    name: 'AdminTeachers',
    component: () => import('@/views/admin/training/teacher/AdminTeachersView.vue'),
    meta: { requiresAuth: true, roles: ['admin'] }
  },
  {
    path: '/admin/training/teachers/new',
    name: 'AdminTeacherForm',
    component: () => import('@/views/admin/training/teacher/AdminTeacherFormView.vue'),
    meta: { requiresAuth: true, roles: ['admin'] }
  },
  {
    path: '/admin/training/teachers/edit/:id',
    name: 'AdminTeacherEdit',
    component: () => import('@/views/admin/training/teacher/AdminTeacherFormView.vue'),
    meta: { requiresAuth: true, roles: ['admin'] }
  },
  {
    path: '/admin/training/teachers/:id',
    name: 'AdminTeacherDetail',
    component: () => import('@/views/admin/training/teacher/AdminTeacherDetailView.vue'),
    meta: { requiresAuth: true, roles: ['admin'] }
  },
  // 教学计划管理
  {
    path: '/admin/training/teaching-plans',
    name: 'AdminTeachingPlans',
    component: () => import('@/views/admin/training/teaching-plan/AdminTeachingPlansView.vue'),
    meta: { requiresAuth: true, roles: ['admin', 'teacher'] }
  },
  {
    path: '/admin/training/teaching-plans/new',
    name: 'AdminTeachingPlanForm',
    component: () => import('@/views/admin/training/teaching-plan/AdminTeachingPlanFormView.vue'),
    meta: { requiresAuth: true, roles: ['admin', 'teacher'] }
  },
  {
    path: '/admin/training/teaching-plans/edit/:id',
    name: 'AdminTeachingPlanEdit',
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
    next({ name: 'Login', query: { redirect: to.fullPath } })
    return
  }

  if (to.meta.roles && user) {
    const roleMap = { 1: 'student', 2: 'teacher', 3: 'social', 4: 'admin' }
    const userRole = roleMap[user.role] || ''
    if (!to.meta.roles.includes(userRole)) {
      next({ name: 'Home' })
      return
    }
  }

  next()
})

export default router