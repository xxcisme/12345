-- ============================================================
-- 虚拟仿真实训教学管理及资源共享云平台 - 示例数据插入
-- 使用 INSERT IGNORE 避免重复执行时报错
-- ============================================================

-- ----------------------------
-- 1. 基础表数据
-- ----------------------------

INSERT IGNORE INTO `sm_user` (`id`, `username`, `password_hash`, `role`, `phone`, `email`, `statu`, `last_login_time`, `last_login_ip`, `del_flag`) VALUES
(1, 'student01', SHA2('password', 256), 1, '13800001001', 'stu01@demo.com', 1, NOW(), '192.168.1.101', 0),
(2, 'student02', SHA2('password', 256), 1, '13800001002', 'stu02@demo.com', 1, NOW(), '192.168.1.102', 0),
(3, 'student03', SHA2('password', 256), 1, '13800001003', 'stu03@demo.com', 1, NOW(), '192.168.1.103', 0),
(4, 'student04', SHA2('password', 256), 1, '13800001004', 'stu04@demo.com', 1, NOW(), '192.168.1.104', 0),
(5, 'student05', SHA2('password', 256), 1, '13800001005', 'stu05@demo.com', 1, NOW(), '192.168.1.105', 0),
(6, 'student06', SHA2('password', 256), 1, '13800001006', 'stu06@demo.com', 1, NOW(), '192.168.1.106', 0),
(7, 'teacher01', SHA2('password', 256), 2, '13800002001', 'tch01@demo.com', 1, NOW(), '192.168.1.201', 0),
(8, 'teacher02', SHA2('password', 256), 2, '13800002002', 'tch02@demo.com', 1, NOW(), '192.168.1.202', 0),
(9, 'admin01', SHA2('password', 256), 4, '13800003001', 'admin@demo.com', 1, NOW(), '192.168.1.203', 0),
(10, 'social01', SHA2('password', 256), 3, '13800004001', 'social@demo.com', 1, NOW(), '192.168.1.204', 0);

INSERT IGNORE INTO `res_resource` (`id`, `number`, `name`, `type`, `thumbnail`, `school`, `leader`, `category`, `status`, `audit_remark`, `is_shared`, `profile`, `del_flag`) VALUES
(1, 'RES-001', 'Unity3D入门教程', 1, '/thumb/unity.jpg', 'SC001', '张教授', '计算机', 2, NULL, 1, '适合初学者的Unity基础视频教程', 0),
(2, 'RES-002', '虚拟仿真实验指导手册', 3, '/thumb/manual.jpg', 'SC001', '李老师', '自动化', 2, NULL, 1, '虚拟仿真实验操作手册（PDF）', 0),
(3, 'RES-003', '3D建模基础音频讲解', 2, '/thumb/audio.jpg', 'SC002', '王老师', '设计', 1, NULL, 1, '音频讲解建模基础', 0),
(4, 'RES-004', 'VR项目开发案例', 1, '/thumb/vr.jpg', 'SC001', '赵老师', '计算机', 1, NULL, 1, 'VR项目开发完整案例视频', 0),
(5, 'RES-005', 'Python数据分析教程', 1, '/thumb/python.jpg', 'SC001', '张教授', '计算机', 0, '待审核', 0, 'Python数据处理基础', 0);

INSERT IGNORE INTO `res_laboratory` (`id`, `number`, `name`, `station_num`, `area`, `address`, `profile`, `description`, `del_flag`) VALUES
(1, 'LAB-001', '虚拟仿真实验室A', 30, 120.50, '实验楼A座301', 'VR/AR专用', '配备HTC VIVE Pro等高端设备', 0),
(2, 'LAB-002', '智能控制实验室', 20, 90.00, '实验楼B座205', 'PLC及机器人', '工业自动化控制实验平台', 0),
(3, 'LAB-003', '网络技术实验室', 40, 150.00, '网络中心3楼', '华为网络设备', '路由交换、安全实验', 0),
(4, 'LAB-004', '多媒体制作实验室', 25, 100.00, '艺术楼206', '图形工作站', '视频剪辑、3D建模', 0);

INSERT IGNORE INTO `tc_teacher` (`id`, `teacher_id`, `name`, `type`, `phone`, `email`, `company`, `on_job`) VALUES
(1, 'TCH001', '张教授', '0', '13800002001', 'zhang@scu.edu', '四川大学', 1),
(2, 'TCH002', '李教授', '0', '13800002002', 'li@scu.edu', '电子科技大学', 1);

INSERT IGNORE INTO `tc_course` (`id`, `course_code`, `course_name`, `course_type`, `class_hours`, `credit`, `teacher_id`, `introduction`, `outline`, `status`) VALUES
(1, 'VR-101', '虚拟现实技术', '专业核心', 48, 3.0, 1, 'VR/AR原理与应用', '第一章 VR概述\n第二章 Unity基础\n第三章 交互设计', 1),
(2, 'AI-101', '人工智能基础', '专业基础', 40, 2.5, 2, '机器学习与深度学习入门', '第一章 机器学习概述\n第二章 线性回归\n第三章 神经网络', 1),
(3, 'AC-201', '自动控制原理', '专业基础', 48, 3.0, 1, '经典控制理论', '第一章 控制系统概述\n第二章 数学模型\n第三章 PID控制', 1),
(4, '3D-301', '3D建模与动画', '专业选修', 32, 2.0, 2, 'Maya、Blender建模', '第一章 建模基础\n第二章 材质与贴图\n第三章 动画制作', 0);

INSERT IGNORE INTO `tc_experiment` (`id`, `number`, `name`, `course_id`, `objective`, `steps`, `report_template`, `status`) VALUES
(1, 'EXP-001', 'VR场景构建', 1, '使用Unity构建简单3D场景', '步骤1：创建项目\n步骤2：搭建地面\n步骤3：添加物体', '实验目的：\n实验步骤：\n实验结果：', 1),
(2, 'EXP-002', 'VR交互设计', 1, '实现手柄交互和UI', '步骤1：导入VR插件\n步骤2：实现射线交互\n步骤3：UI设计', '实验目的：\n实验步骤：\n实验结果：', 1),
(3, 'EXP-003', '机器学习入门', 2, '使用Scikit-learn进行线性回归', '步骤1：导入数据\n步骤2：数据预处理\n步骤3：训练模型', '实验目的：\n实验步骤：\n实验结果：', 1),
(4, 'EXP-004', '深度学习实践', 2, '搭建CNN网络进行图像分类', '步骤1：加载数据集\n步骤2：构建网络\n步骤3：训练评估', '实验目的：\n实验步骤：\n实验结果：', 1),
(5, 'EXP-005', 'PLC电机控制', 3, '使用PLC控制电机启停', '步骤1：硬件接线\n步骤2：编写梯形图\n步骤3：下载调试', '实验目的：\n实验步骤：\n实验结果：', 1),
(6, 'EXP-006', 'PID调节实验', 3, '实现PID闭环控制', '步骤1：系统建模\n步骤2：参数整定\n步骤3：仿真验证', '实验目的：\n实验步骤：\n实验结果：', 1),
(7, 'EXP-007', 'Maya建模基础', 4, '多边形建模入门', '步骤1：创建基本体\n步骤2：编辑顶点\n步骤3：渲染输出', '实验目的：\n实验步骤：\n实验结果：', 0),
(8, 'EXP-008', 'Blender动画', 4, '关键帧动画和渲染', '步骤1：导入模型\n步骤2：设置关键帧\n步骤3：渲染输出', '实验目的：\n实验步骤：\n实验结果：', 0);

INSERT IGNORE INTO `tc_class` (`id`, `class_name`, `school_code`) VALUES
(1, '计算机科学与技术2023级1班', 'SC001'),
(2, '计算机科学与技术2023级2班', 'SC001'),
(3, '自动化2023级1班', 'SC001'),
(4, '数字媒体2023级1班', 'SC001');

INSERT IGNORE INTO `sys_news` (`id`, `title`, `origin`, `editor`, `enclosure`, `content`, `publish_time`) VALUES
(1, '学校虚拟仿真教学平台上线', '教务处', 'admin', '/img/news1.jpg', '经过半年建设，我校虚拟仿真实训平台正式上线，欢迎广大师生使用。', '2026-07-01 09:00:00'),
(2, '全国虚拟仿真大赛报名通知', '竞赛委员会', 'admin', '/img/news2.jpg', '2026年全国虚拟仿真创新大赛开始报名，截止日期为8月31日。', '2026-07-05 10:00:00'),
(3, '校企合作共建VR实验室', '校企合作办', 'admin', '/img/news3.jpg', '学校与华为公司签署合作协议，共同建设VR联合实验室。', '2026-07-10 14:00:00');

INSERT IGNORE INTO `sys_notice` (`id`, `title`, `editor`, `content`, `publish_time`) VALUES
(1, '平台维护公告', 'admin', '本平台将于7月20日00:00-06:00进行系统升级维护，届时暂停服务。', '2026-07-10 16:00:00'),
(2, '新增VR实验资源', 'admin', '平台新增Unity3D VR开发案例资源，欢迎师生登录查看和使用。', '2026-07-12 08:00:00'),
(3, '实验室开放时间调整', 'admin', '暑假期间实验室开放时间调整为周一至周五8:00-17:00，请提前预约。', '2026-07-13 09:00:00');

-- ----------------------------
-- 2. 依赖表数据
-- ----------------------------

INSERT IGNORE INTO `sm_user_profile` (`id`, `user_id`, `real_name`, `school_code`, `class_name`, `class_id`, `occupation_type`) VALUES
(1, 1, '张三', 'SC001', '计算机2023级1班', 1, '学生'),
(2, 2, '李四', 'SC001', '计算机2023级1班', 1, '学生'),
(3, 3, '王五', 'SC001', '计算机2023级1班', 1, '学生'),
(4, 4, '赵六', 'SC001', '计算机2023级2班', 2, '学生'),
(5, 5, '孙七', 'SC001', '计算机2023级2班', 2, '学生'),
(6, 6, '周八', 'SC001', '自动化2023级1班', 3, '学生'),
(7, 7, '张教授', 'SC001', NULL, NULL, '教师'),
(8, 8, '李教授', 'SC001', NULL, NULL, '教师'),
(9, 9, '管理员', 'SC001', NULL, NULL, '管理员'),
(10, 10, '社会人士', 'SC002', NULL, NULL, '工程师');

INSERT IGNORE INTO `sm_user_favorite` (`id`, `user_id`, `resource_id`) VALUES
(1, 1, 1),
(2, 1, 2),
(3, 2, 3),
(4, 3, 4),
(5, 4, 1),
(6, 5, 5);

INSERT IGNORE INTO `sm_user_message` (`id`, `user_id`, `title`, `content`, `type`, `is_read`) VALUES
(1, 1, '实验室申请通过', '您申请的虚拟仿真实验室（A101）已通过审批，请按时使用。', 1, 0),
(2, 2, '课程提醒', '您选修的《虚拟现实技术》将于明日开始，请做好准备。', 2, 0),
(3, 1, '成绩发布', '您的《虚拟现实技术》实验成绩已发布，得分92分。', 3, 0),
(4, 3, '资源审核通知', '您上传的资源《Unity基础教程》已通过审核，现已发布。', 1, 1),
(5, 4, '课程提醒', '您选修的《人工智能基础》课程有新的实验安排。', 2, 0);

INSERT IGNORE INTO `res_rating` (`id`, `user_id`, `resource_id`, `score`) VALUES
(1, 1, 1, 5),
(2, 2, 1, 4),
(3, 3, 2, 3),
(4, 4, 4, 5),
(5, 5, 3, 4),
(6, 6, 1, 5);

INSERT IGNORE INTO `res_media` (`id`, `resource_id`, `file_url`) VALUES
(1, 1, '/files/unity_tutorial.mp4'),
(2, 2, '/files/manual.pdf'),
(3, 3, '/files/audio_3d.mp3'),
(4, 4, '/files/vr_case.mp4'),
(5, 5, '/files/python_data.mp4');

INSERT IGNORE INTO `res_device` (`id`, `number`, `name`, `type`, `version_number`, `laboratory_id`, `status`, `del_flag`) VALUES
(1, 'DEV-001', 'HTC VIVE Pro', 'VR头显', '2.0', 1, 0, 0),
(2, 'DEV-002', '高性能工作站', '计算机', 'Dell Precision', 1, 1, 0),
(3, 'DEV-003', 'PLC控制器', '控制设备', 'S7-1200', 2, 0, 0),
(4, 'DEV-004', '示波器', '测量仪器', 'Tektronix', 2, 0, 0),
(5, 'DEV-005', '核心交换机', '网络设备', 'Huawei S5700', 3, 0, 0),
(6, 'DEV-006', '路由器', '网络设备', 'Cisco 2900', 3, 1, 0),
(7, 'DEV-007', '图形工作站', '计算机', 'HP Z系列', 4, 0, 0),
(8, 'DEV-008', '数位板', '输入设备', 'Wacom', 4, 0, 0);

INSERT IGNORE INTO `res_lab_application` (`id`, `number`, `lab_id`, `applicant_name`, `contact_phone`, `purpose`, `name`, `objective`, `steps`, `start_time`, `end_time`, `status`, `audit_remark`, `approval_time`) VALUES
(1, 'APP-001', 1, '张三', '13800001001', 'VR课程实验', '虚拟现实交互实验', '掌握VR交互开发技术', '步骤1：配置环境\n步骤2：开发交互功能\n步骤3：测试优化', '2026-07-15 08:00:00', '2026-07-15 12:00:00', 1, '同意使用', NOW()),
(2, 'APP-002', 2, '李四', '13800001002', '自动化控制实验', 'PLC编程实验', '掌握PLC梯形图编程', '步骤1：分析需求\n步骤2：编写程序\n步骤3：调试运行', '2026-07-16 14:00:00', '2026-07-16 18:00:00', 0, NULL, NULL),
(3, 'APP-003', 3, '王五', '13800001003', '网络配置实验', '交换机配置', '掌握VLAN配置方法', '步骤1：连接设备\n步骤2：配置VLAN\n步骤3：测试连通性', '2026-07-17 09:00:00', '2026-07-17 11:00:00', 2, '设备维护中，暂不可用', NOW()),
(4, 'APP-004', 4, '赵六', '13800001004', '视频剪辑实验', '后期制作实验', '掌握Premiere基本操作', '步骤1：导入素材\n步骤2：剪辑拼接\n步骤3：导出成片', '2026-07-18 13:00:00', '2026-07-18 17:00:00', 1, '同意', NOW());

INSERT IGNORE INTO `tc_teaching_plan` (`id`, `name`, `semester`, `class_id`, `create_id`, `start_date`, `end_date`, `status`) VALUES
(1, '2026秋-虚拟现实技术教学计划', '2026-2027-1', 1, 7, '2026-09-01', '2026-12-31', 1),
(2, '2026秋-虚拟现实技术教学计划', '2026-2027-1', 2, 7, '2026-09-01', '2026-12-31', 1),
(3, '2026秋-人工智能基础教学计划', '2026-2027-1', 2, 8, '2026-09-01', '2026-12-31', 1),
(4, '2026秋-自动控制原理教学计划', '2026-2027-1', 3, 7, '2026-09-01', '2026-12-31', 1);

INSERT IGNORE INTO `tc_plan_detail` (`id`, `plan_id`, `course_id`, `experiment_id`, `schedule_date`) VALUES
(1, 1, 1, 1, '2026-09-15'),
(2, 1, 1, 2, '2026-09-22'),
(3, 2, 1, 1, '2026-09-16'),
(4, 2, 1, 2, '2026-09-23'),
(5, 3, 2, 3, '2026-09-17'),
(6, 3, 2, 4, '2026-09-24'),
(7, 4, 3, 5, '2026-09-18'),
(8, 4, 3, 6, '2026-09-25');

INSERT IGNORE INTO `tc_lab_report` (`id`, `schedule_id`, `student_id`, `report_content`, `attachment`, `score`, `evaluation_status`, `submitted_at`) VALUES
(1, 1, 1, 'VR场景构建实验报告：成功搭建了完整的3D场景，包含地形、建筑和灯光...', '/reports/rep1.pdf', 92.5, 1, '2026-09-15 10:30:00'),
(2, 3, 2, 'VR场景构建：场景搭建完成，交互部分实现基本功能...', NULL, 78.0, 1, '2026-09-16 09:30:00'),
(3, 1, 3, 'VR场景：完整实现场景搭建与交互逻辑，效果良好。', '/reports/rep3.pdf', 88.0, 1, '2026-09-15 10:45:00'),
(4, 5, 1, '机器学习实验：完成线性回归模型，R2达到0.92...', '/reports/rep4.pdf', 85.0, 1, '2026-09-17 14:30:00'),
(5, 5, 4, '机器学习实验：模型存在欠拟合问题...', NULL, 65.0, 1, '2026-09-17 14:20:00'),
(6, 7, 6, 'PLC控制实验：成功实现电机启停、正反转控制，程序稳定。', '/reports/rep6.pdf', 95.0, 1, '2026-09-18 10:00:00');

INSERT IGNORE INTO `tc_grade` (`id`, `schedule_id`, `student_id`, `overall_score`, `comment`, `publish_status`, `graded_at`) VALUES
(1, 1, 1, 92.5, '实验完成度高，逻辑清晰，报告规范', 1, NOW()),
(2, 1, 3, 88.0, '基本功能完善，交互设计可进一步优化', 1, NOW()),
(3, 3, 2, 78.0, '场景搭建完整，交互功能基本实现', 1, NOW()),
(4, 5, 1, 85.0, '模型训练效果良好，报告详细完整', 1, NOW()),
(5, 5, 4, 65.0, '模型效果不理想，建议重新调参', 1, NOW()),
(6, 7, 6, 95.0, '操作规范，编程正确，报告优秀', 1, NOW());

INSERT IGNORE INTO `sys_operation_log` (`id`, `user_id`, `user_role`, `username`, `action`, `ip_address`) VALUES
(1, 9, '管理员', 'admin01', '添加用户 student01', '192.168.1.203'),
(2, 7, '老师', 'teacher01', '发布课程《虚拟现实技术》', '192.168.1.201'),
(3, 1, '学生', 'student01', '收藏资源 Unity3D入门教程', '192.168.1.101'),
(4, 9, '管理员', 'admin01', '审批实验室申请 APP-001 通过', '192.168.1.203'),
(5, 8, '老师', 'teacher02', '创建教学计划 2026秋-人工智能基础教学计划', '192.168.1.202');