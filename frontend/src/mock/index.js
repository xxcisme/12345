import Mock from 'mockjs'

// ========== 通用工具 ==========

const ok = (data) => ({ code: 200, data, msg: 'success' })

const page = (list, total) => ({
  code: 200,
  data: { records: list, total, pageNo: 1, pageSize: 10 },
  msg: 'success'
})

let _id = 100
const nextId = () => ++_id

// ========== 认证 ==========

Mock.mock(/\/api\/v1\/auth\/login/, 'post', (options) => {
  const body = JSON.parse(options.body)
  return ok({
    token: 'mock-token-' + Date.now(),
    user: { id: 1, username: body.username || 'admin', role: 'admin', realName: '管理员' }
  })
})

Mock.mock(/\/api\/v1\/auth\/register/, 'post', ok({ id: nextId() }))

Mock.mock(/\/api\/v1\/auth\/logout/, 'post', ok(null))

// ========== 课程 ==========

Mock.mock(/\/api\/v1\/admin\/courses(\?.*)?$/, 'get', () => {
  const records = Mock.mock({
    'list|10': [{
      id: '@increment',
      courseName: '@ctitle(5, 10)',
      courseCode: 'COU-' + '@integer(1000, 9999)',
      courseType: '@pick(["必修", "选修", "实践"])',
      classHours: '@integer(16, 64)',
      credit: '@float(1, 6, 1, 1)',
      teacherName: '@cname',
      teacherId: '@integer(1, 100)',
      status: '@integer(0, 1)',
      createTime: '@datetime',
      updateTime: '@datetime'
    }]
  })
  return page(records.list, 50)
})

Mock.mock(/\/api\/v1\/admin\/courses$/, 'post', ok({ id: nextId() }))
Mock.mock(/\/api\/v1\/admin\/courses$/, 'put', ok({ id: nextId() }))
Mock.mock(/\/api\/v1\/admin\/courses\/\d+\/publish/, 'put', ok(null))
Mock.mock(/\/api\/v1\/admin\/courses\/\d+\/unpublish/, 'put', ok(null))
Mock.mock(/\/api\/v1\/admin\/courses\/\d+$/, 'delete', ok(null))

Mock.mock(/\/api\/v1\/user\/courses\/\d+$/, 'get', () => ok({
  id: 1,
  courseName: '计算机组成原理',
  courseCode: 'COU-1001',
  courseType: '必修',
  classHours: 48,
  credit: 3.0,
  teacherName: '张教授',
  teacherId: 1,
  introduction: '本课程介绍计算机硬件系统的组成和工作原理，包括数据表示、运算器、控制器、存储器、输入输出系统等核心内容。',
  outline: '第一章：计算机系统概述\n第二章：数据的表示与运算\n第三章：运算器设计\n第四章：指令系统\n第五章：中央处理器\n第六章：存储器层次结构\n第七章：输入输出系统',
  status: 1,
  createTime: '2025-01-15 10:00:00',
  updateTime: '2025-06-01 14:00:00'
}))

// ========== 实验 ==========

Mock.mock(/\/api\/v1\/admin\/experiments(\?.*)?$/, 'get', () => {
  const records = Mock.mock({
    'list|10': [{
      id: '@increment',
      number: 'EXP-' + '@integer(1000, 9999)',
      name: '@ctitle(5, 10)',
      courseName: '@ctitle(4, 8)',
      courseId: '@integer(1, 50)',
      status: '@integer(0, 1)',
      createTime: '@datetime',
      updateTime: '@datetime'
    }]
  })
  return page(records.list, 30)
})

Mock.mock(/\/api\/v1\/admin\/experiments$/, 'post', ok({ id: nextId() }))
Mock.mock(/\/api\/v1\/admin\/experiments$/, 'put', ok({ id: nextId() }))
Mock.mock(/\/api\/v1\/admin\/experiments\/\d+\/publish/, 'put', ok(null))
Mock.mock(/\/api\/v1\/admin\/experiments\/\d+$/, 'delete', ok(null))

Mock.mock(/\/api\/v1\/user\/experiments\/\d+$/, 'get', () => ok({
  id: 1,
  number: 'EXP-2001',
  name: 'ALU 运算器设计实验',
  courseName: '计算机组成原理',
  courseId: 1,
  objective: '掌握算术逻辑单元的设计方法，理解加法器、乘法器的硬件实现原理',
  steps: '1. 设计 8 位 ALU 电路结构\n2. 编写 Verilog 代码实现\n3. 使用 ModelSim 进行功能仿真\n4. 验证加法、减法、与、或、非等运算\n5. 上板测试并记录结果',
  reportTemplate: '## 实验报告模板\n\n### 一、实验目的\n\n### 二、实验原理\n\n### 三、实验步骤\n\n### 四、实验结果与分析\n\n### 五、思考题\n\n### 六、实验总结',
  status: 1,
  createTime: '2025-02-01 09:00:00',
  updateTime: '2025-06-10 16:00:00'
}))

// ========== 线上资源 ==========

Mock.mock(/\/api\/v1\/resources(\?.*)?$/, 'get', () => {
  const records = Mock.mock({
    'list|10': [{
      id: '@increment',
      number: 'RES-' + '@integer(1000, 9999)',
      name: '@ctitle(5, 12)',
      type: '@integer(1, 3)',
      typeName: '@pick(["视频", "音频", "文档"])',
      thumbnail: '',
      school: '@pick(["清华大学", "北京大学", "浙江大学", "上海交通大学", "南京大学"])',
      leader: '@cname',
      category: '@pick(["计算机", "电子", "机械", "材料", "数学"])',
      isShared: '@boolean',
      score: '@float(3, 5, 1, 1)',
      ratingCount: '@integer(0, 200)',
      createTime: '@datetime'
    }]
  })
  return page(records.list, 80)
})

Mock.mock(/\/api\/v1\/resources\/\d+$/, 'get', () => ok({
  id: 1,
  number: 'RES-1001',
  name: '数字电路基础教程',
  type: 1,
  typeName: '视频',
  thumbnail: '',
  school: '清华大学',
  leader: '李教授',
  category: '电子',
  isShared: true,
  score: 4.5,
  ratingCount: 128,
  fileUrl: 'https://example.com/video.mp4',
  profile: '本资源详细讲解数字电路基础知识，包括逻辑门、组合电路、时序电路等内容，适合电子工程专业学生入门学习。',
  auditRemark: '',
  status: 2,
  statusName: '已发布',
  createTime: '2025-03-01 08:00:00'
}))

Mock.mock(/\/api\/v1\/resources\/\d+\/score/, 'post', () => ok({
  newAvgScore: 4.6,
  newRatingCount: 129
}))

// ========== 线上资源（后台） ==========

Mock.mock(/\/api\/v1\/admin\/resources(\?.*)?$/, 'get', () => {
  const records = Mock.mock({
    'list|10': [{
      id: '@increment',
      number: 'RES-' + '@integer(1000, 9999)',
      name: '@ctitle(5, 12)',
      type: '@integer(1, 3)',
      typeName: '@pick(["视频", "音频", "文档"])',
      thumbnail: '',
      school: '@pick(["清华大学", "北京大学", "浙江大学", "上海交通大学", "南京大学"])',
      leader: '@cname',
      category: '@pick(["计算机", "电子", "机械", "材料", "数学"])',
      isShared: '@boolean',
      score: '@float(3, 5, 1, 1)',
      ratingCount: '@integer(0, 200)',
      status: '@integer(0, 2)',
      uploaderId: '@integer(1, 50)',
      uploaderName: '@cname',
      createTime: '@datetime'
    }]
  })
  return page(records.list, 100)
})

Mock.mock(/\/api\/v1\/admin\/resources$/, 'post', ok({ id: nextId() }))
Mock.mock(/\/api\/v1\/admin\/resources$/, 'put', ok({ id: nextId() }))
Mock.mock(/\/api\/v1\/admin\/resources\/\d+$/, 'delete', ok(null))
Mock.mock(/\/api\/v1\/admin\/resources\/\d+\/audit/, 'put', ok({ id: 1, status: 2 }))

// ========== 实验室 ==========

Mock.mock(/\/api\/v1\/laboratories(\?.*)?$/, 'get', () => {
  const records = Mock.mock({
    'list|10': [{
      id: '@increment',
      number: 'LAB-' + '@integer(100, 999)',
      name: '@ctitle(4, 8)',
      stationNum: '@integer(20, 60)',
      area: '@float(50, 200, 1, 1)',
      address: '@city(true)',
      createTime: '@datetime',
      updateTime: '@datetime'
    }]
  })
  return page(records.list, 20)
})

Mock.mock(/\/api\/v1\/laboratories\/\d+$/, 'get', () => ok({
  id: 1,
  number: 'LAB-101',
  name: '数字电路实验室',
  stationNum: 40,
  area: 120.5,
  address: '北京市海淀区',
  profile: '用于数字电路实验教学，配备示波器、信号发生器、逻辑分析仪等设备，可同时容纳 40 名学生进行实验。',
  createTime: '2025-01-01 08:00:00',
  updateTime: '2025-06-01 10:00:00',
  devices: Mock.mock({
    'list|3': [{
      id: '@increment',
      number: 'DEV-' + '@integer(100, 999)',
      name: '@pick(["示波器", "信号发生器", "逻辑分析仪", "万用表", "电源"])',
      type: '@pick(["测量", "信号", "分析", "电源"])',
      status: '@integer(0, 3)'
    }]
  }).list
}))

Mock.mock(/\/api\/v1\/admin\/laboratories$/, 'post', ok({ id: nextId() }))
Mock.mock(/\/api\/v1\/admin\/laboratories$/, 'put', ok({ id: nextId() }))
Mock.mock(/\/api\/v1\/admin\/laboratories\/\d+$/, 'delete', ok(null))

// ========== 设备 ==========

Mock.mock(/\/api\/v1\/devices(\?.*)?$/, 'get', () => {
  const records = Mock.mock({
    'list|10': [{
      id: '@increment',
      number: 'DEV-' + '@integer(100, 999)',
      name: '@pick(["示波器", "信号发生器", "逻辑分析仪", "万用表", "电源"])',
      type: '@pick(["测量", "信号", "分析", "电源"])',
      versionNumber: 'V' + '@integer(1, 5).@integer(0, 9)',
      laboratoryId: '@integer(1, 20)',
      'laboratoryName|1': ['数字电路实验室', '模拟电路实验室', '嵌入式实验室', '通信原理实验室'],
      status: '@integer(0, 3)',
      statusName: '@pick(["空闲", "使用中", "保修", "损坏"])',
      createTime: '@datetime',
      updateTime: '@datetime'
    }]
  })
  return page(records.list, 60)
})

Mock.mock(/\/api\/v1\/devices\/\d+$/, 'get', () => ok({
  id: 1,
  number: 'DEV-101',
  name: '数字示波器',
  type: '测量',
  versionNumber: 'V3.2',
  laboratoryId: 1,
  laboratoryName: '数字电路实验室',
  status: 0,
  statusName: '空闲',
  createTime: '2025-01-15 08:00:00',
  updateTime: '2025-06-01 10:00:00',
  laboratory: {
    id: 1,
    number: 'LAB-101',
    name: '数字电路实验室',
    stationNum: 40,
    area: 120.5,
    address: '北京市海淀区'
  }
}))

Mock.mock(/\/api\/v1\/admin\/devices$/, 'post', ok({ id: nextId() }))
Mock.mock(/\/api\/v1\/admin\/devices$/, 'put', ok({ id: nextId() }))
Mock.mock(/\/api\/v1\/admin\/devices\/\d+$/, 'delete', ok(null))

// ========== 实验室申请（用户端） ==========

Mock.mock(/\/api\/v1\/lab-applications$/, 'post', ok({ id: nextId() }))

Mock.mock(/\/api\/v1\/lab-applications(\?.*)?$/, 'get', () => {
  const records = Mock.mock({
    'list|10': [{
      id: '@increment',
      number: 'APP-' + '@integer(1000, 9999)',
      labId: '@integer(1, 20)',
      labName: '@pick(["数字电路实验室", "模拟电路实验室", "嵌入式实验室", "通信原理实验室"])',
      applicantName: '@cname',
      contactPhone: '1' + '@integer(3, 9)' + '@integer(10000000, 99999999)',
      name: '@ctitle(4, 10)',
      startTime: '@datetime("yyyy-MM-dd HH:mm:ss")',
      endTime: '@datetime("yyyy-MM-dd HH:mm:ss")',
      status: '@integer(0, 2)',
      statusName: '@pick(["待审批", "已通过", "已拒绝"])',
      approvalTime: '@datetime',
      createTime: '@datetime'
    }]
  })
  return page(records.list, 25)
})

// ========== 实验室申请（后台） ==========

Mock.mock(/\/api\/v1\/admin\/lab-applications(\?.*)?$/, 'get', () => {
  const records = Mock.mock({
    'list|10': [{
      id: '@increment',
      number: 'APP-' + '@integer(1000, 9999)',
      labId: '@integer(1, 20)',
      labName: '@pick(["数字电路实验室", "模拟电路实验室", "嵌入式实验室", "通信原理实验室"])',
      applicantName: '@cname',
      applicantUserId: '@integer(1, 100)',
      contactPhone: '1' + '@integer(3, 9)' + '@integer(10000000, 99999999)',
      name: '@ctitle(4, 10)',
      purpose: '@csentence(10, 30)',
      startTime: '@datetime("yyyy-MM-dd HH:mm:ss")',
      endTime: '@datetime("yyyy-MM-dd HH:mm:ss")',
      status: 0,
      statusName: '待审批',
      approvalTime: '',
      createTime: '@datetime'
    }]
  })
  return page(records.list, 15)
})

Mock.mock(/\/api\/v1\/admin\/lab-applications\/\d+\/audit/, 'put', ok({ id: 1, status: 1 }))

// ========== 用户（个人信息 / 收藏 / 课程 / 实验 / 通知） ==========

Mock.mock(/\/api\/v1\/user\/profile/, 'get', () => ok({
  id: 1,
  username: 'admin',
  realName: '管理员',
  role: 'admin',
  email: 'admin@example.com',
  phone: '13800000000',
  avatar: ''
}))

Mock.mock(/\/api\/v1\/user\/profile/, 'put', ok(null))
Mock.mock(/\/api\/v1\/user\/password/, 'put', ok(null))

Mock.mock(/\/api\/v1\/user\/favorites/, 'get', () => page([], 0))
Mock.mock(/\/api\/v1\/user\/favorites/, 'post', ok(null))
Mock.mock(/\/api\/v1\/user\/favorites\/\d+/, 'delete', ok(null))

Mock.mock(/\/api\/v1\/user\/courses(\?.*)?$/, 'get', () => page([], 0))
Mock.mock(/\/api\/v1\/user\/experiments(\?.*)?$/, 'get', () => page([], 0))
Mock.mock(/\/api\/v1\/user\/messages/, 'get', () => page([], 0))
Mock.mock(/\/api\/v1\/user\/messages\/read/, 'put', ok(null))

// ========== 公告 / 新闻 ==========

Mock.mock(/\/api\/v1\/news(\?.*)?$/, 'get', () => page([], 0))
Mock.mock(/\/api\/v1\/news\/\d+$/, 'get', () => ok({}))
Mock.mock(/\/api\/v1\/notices(\?.*)?$/, 'get', () => page([], 0))
Mock.mock(/\/api\/v1\/notices\/\d+$/, 'get', () => ok({}))

Mock.mock(/\/api\/v1\/admin\/news(\?.*)?$/, 'get', () => page([], 0))
Mock.mock(/\/api\/v1\/admin\/news$/, 'post', ok({ id: nextId() }))
Mock.mock(/\/api\/v1\/admin\/news$/, 'put', ok({ id: nextId() }))
Mock.mock(/\/api\/v1\/admin\/news\/\d+$/, 'delete', ok(null))

Mock.mock(/\/api\/v1\/admin\/notices(\?.*)?$/, 'get', () => page([], 0))
Mock.mock(/\/api\/v1\/admin\/notices$/, 'post', ok({ id: nextId() }))
Mock.mock(/\/api\/v1\/admin\/notices$/, 'put', ok({ id: nextId() }))
Mock.mock(/\/api\/v1\/admin\/notices\/\d+$/, 'delete', ok(null))

// ========== 系统管理（用户 / 日志） ==========

Mock.mock(/\/api\/v1\/admin\/users(\?.*)?$/, 'get', () => page([], 0))
Mock.mock(/\/api\/v1\/admin\/users\/\d+$/, 'get', () => ok({}))
Mock.mock(/\/api\/v1\/admin\/users$/, 'post', ok({ id: nextId() }))
Mock.mock(/\/api\/v1\/admin\/users$/, 'put', ok({ id: nextId() }))
Mock.mock(/\/api\/v1\/admin\/users\/\d+\/status/, 'put', ok(null))
Mock.mock(/\/api\/v1\/admin\/users\/\d+$/, 'delete', ok(null))
Mock.mock(/\/api\/v1\/admin\/logs(\?.*)?$/, 'get', () => page([], 0))
Mock.mock(/\/api\/v1\/admin\/logs\/export/, 'get', () => ({}))
Mock.mock(/\/api\/v1\/admin\/logs\/clean/, 'delete', () => ok({ deletedCount: 150 }))

// ========== 师资管理 ==========

Mock.mock(/\/api\/v1\/admin\/teachers(\?.*)?$/, 'get', () => page([], 0))
Mock.mock(/\/api\/v1\/admin\/teachers\/\d+$/, 'get', () => ok({}))
Mock.mock(/\/api\/v1\/admin\/teachers$/, 'post', ok({ id: nextId() }))
Mock.mock(/\/api\/v1\/admin\/teachers$/, 'put', ok({ id: nextId() }))
Mock.mock(/\/api\/v1\/admin\/teachers\/\d+$/, 'delete', ok(null))
Mock.mock(/\/api\/v1\/admin\/teachers\/\d+\/reset-password/, 'put', ok(null))

// ========== 教学计划 ==========

Mock.mock(/\/api\/v1\/admin\/teaching-plans(\?.*)?$/, 'get', () => page([], 0))
Mock.mock(/\/api\/v1\/admin\/teaching-plans$/, 'post', ok({ id: nextId() }))
Mock.mock(/\/api\/v1\/admin\/teaching-plans$/, 'put', ok({ id: nextId() }))
Mock.mock(/\/api\/v1\/admin\/teaching-plans\/\d+\/publish/, 'put', ok(null))
Mock.mock(/\/api\/v1\/admin\/teaching-plans\/\d+$/, 'delete', ok(null))

// ========== 成绩管理 ==========

Mock.mock(/\/api\/v1\/admin\/grades(\?.*)?$/, 'get', () => page([], 0))
Mock.mock(/\/api\/v1\/admin\/grades$/, 'post', ok({ id: nextId() }))
Mock.mock(/\/api\/v1\/admin\/grades$/, 'put', ok({ id: nextId() }))
Mock.mock(/\/api\/v1\/admin\/grades\/\d+\/publish/, 'put', ok({ id: 1, publishStatus: 1 }))
Mock.mock(/\/api\/v1\/admin\/grades\/statistics/, 'get', () => ok({
  avgScore: 85.5,
  maxScore: 98,
  minScore: 60,
  passRate: 92.3,
  totalCount: 30,
  passCount: 28
}))

console.log('[Mock] 已启用，共拦截 ' + Mock._mocked.length + ' 条规则')