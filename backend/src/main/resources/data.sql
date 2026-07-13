-- ============================================================
-- 虚拟仿真实训教学管理及资源共享云平台 - 示例数据插入
-- 依据：《数据库设计说明书》字段定义
-- 说明：所有ID固定，数据间有关联关系，覆盖主要业务场景
-- ============================================================

-- ----------------------------
-- 基础表数据
-- ----------------------------

-- 1. sm_user（包含四种角色，学生6人、教师2人、管理员1人、社会人士1人）
INSERT IGNORE INTO `sm_user` (`id`, `username`, `password_hash`, `role`, `phone`, `email`, `statu`, `last_login_time`, `last_login_ip`) VALUES
                                                                                                                                     (1, 'student01', SHA2('password', 256), 1, '13800001001', 'stu01@demo.com', 1, NOW(), '192.168.1.101'),
                                                                                                                                     (2, 'student02', SHA2('password', 256), 1, '13800001002', 'stu02@demo.com', 1, NOW(), '192.168.1.102'),
                                                                                                                                     (3, 'student03', SHA2('password', 256), 1, '13800001003', 'stu03@demo.com', 1, NOW(), '192.168.1.103'),
                                                                                                                                     (4, 'student04', SHA2('password', 256), 1, '13800001004', 'stu04@demo.com', 1, NOW(), '192.168.1.104'),
                                                                                                                                     (5, 'student05', SHA2('password', 256), 1, '13800001005', 'stu05@demo.com', 1, NOW(), '192.168.1.105'),
                                                                                                                                     (6, 'student06', SHA2('password', 256), 1, '13800001006', 'stu06@demo.com', 1, NOW(), '192.168.1.106'),
                                                                                                                                     (7, 'teacher01', SHA2('password', 256), 2, '13800002001', 'tch01@demo.com', 1, NOW(), '192.168.1.201'),
                                                                                                                                     (8, 'teacher02', SHA2('password', 256), 2, '13800002002', 'tch02@demo.com', 1, NOW(), '192.168.1.202'),
                                                                                                                                     (9, 'admin01', SHA2('password', 256), 4, '13800003001', 'admin@demo.com', 1, NOW(), '192.168.1.203'),
                                                                                                                                     (10, 'social01', SHA2('password', 256), 3, '13800004001', 'social@demo.com', 1, NOW(), '192.168.1.204');

-- 2. res_resource（5条）
INSERT IGNORE INTO `res_resource` (`id`, `number`, `name`, `type`, `thumbnail`, `school`, `leader`, `category`, `status`, `audit_remark`, `is_shared`, `profile`) VALUES
                                                                                                                                                               (1, 'RES-001', 'Unity3D入门教程', 1, '/thumb/unity.jpg', 'SC001', '张教授', '计算机', 1, NULL, 1, '适合初学者的Unity基础视频教程'),
                                                                                                                                                               (2, 'RES-002', '虚拟仿真实验指导手册', 3, '/thumb/manual.jpg', 'SC001', '李老师', '自动化', 1, NULL, 1, '虚拟仿真实验操作手册（PDF）'),
                                                                                                                                                               (3, 'RES-003', '3D建模基础音频讲解', 2, '/thumb/audio.jpg', 'SC002', '王老师', '设计', 1, NULL, 1, '音频讲解建模基础'),
                                                                                                                                                               (4, 'RES-004', 'VR项目开发案例', 1, '/thumb/vr.jpg', 'SC001', '赵老师', '计算机', 1, NULL, 1, 'VR项目开发完整案例视频'),
                                                                                                                                                               (5, 'RES-005', 'Python数据分析教程', 1, '/thumb/python.jpg', 'SC001', '张教授', '计算机', 0, '待审核', 0, 'Python数据处理基础');

-- 3. res_laboratory（4条）
INSERT IGNORE INTO `res_laboratory` (`id`, `number`, `name`, `station_num`, `area`, `address`, `profile`, `description`) VALUES
                                                                                                                      (1, 'LAB-001', '虚拟仿真实验室A', 30, 120.50, '实验楼A座301', 'VR/AR专用', '配备HTC VIVE Pro等高端设备'),
                                                                                                                      (2, 'LAB-002', '智能控制实验室', 20, 90.00, '实验楼B座205', 'PLC及机器人', '工业自动化控制实验平台'),
                                                                                                                      (3, 'LAB-003', '网络技术实验室', 40, 150.00, '网络中心3楼', '华为网络设备', '路由交换、安全实验'),
                                                                                                                      (4, 'LAB-004', '多媒体制作实验室', 25, 100.00, '艺术楼206', '图形工作站', '视频剪辑、3D建模');

-- 4. tc_teacher（2条，与用户ID对应）
INSERT IGNORE INTO `tc_teacher` (`id`, `teacher_id`, `name`, `type`, `phone`, `email`, `company`, `on_job`) VALUES
                                                                                                         (1, 'TCH001', '张教授', '0', '13800002001', 'tch01@demo.com', '四川大学', 1),
                                                                                                         (2, 'TCH002', '李教授', '0', '13800002002', 'tch02@demo.com', '电子科技大学', 1);

-- 5. tc_course（5条）
INSERT IGNORE INTO `tc_course` (`id`, `course_code`, `course_name`, `course_type`, `introduction`, `charge_flag`) VALUES
                                                                                                               (1, 'VR-101', '虚拟现实技术', '专业核心', 'VR/AR原理与应用', 1),
                                                                                                               (2, 'AI-101', '人工智能基础', '专业基础', '机器学习与深度学习入门', 1),
                                                                                                               (3, 'AC-201', '自动控制原理', '专业基础', '经典控制理论', 1),
                                                                                                               (4, '3D-301', '3D建模与动画', '专业选修', 'Maya、Blender建模', 1),
                                                                                                               (5, 'DS-101', '数据结构', '专业基础', '线性表、树、图', 0);

-- 6. tc_experiment（10条，每门课2个）
INSERT IGNORE INTO `tc_experiment` (`id`, `number`, `name`, `category`, `experiment_type`, `profile`, `description`) VALUES
                                                                                                                  (1, 'EXP-001', 'VR场景构建', '计算机', 'VR', '使用Unity构建简单3D场景', '详细步骤：...'),
                                                                                                                  (2, 'EXP-002', 'VR交互设计', '计算机', 'VR', '实现手柄交互和UI', '详细步骤：...'),
                                                                                                                  (3, 'EXP-003', '机器学习入门', '计算机', 'AI', '使用Scikit-learn进行线性回归', '代码示例：...'),
                                                                                                                  (4, 'EXP-004', '深度学习实践', '计算机', 'AI', '搭建CNN网络进行图像分类', '使用TensorFlow'),
                                                                                                                  (5, 'EXP-005', 'PLC电机控制', '自动化', '控制', '使用PLC控制电机启停', '接线与编程'),
                                                                                                                  (6, 'EXP-006', 'PID调节实验', '自动化', '控制', '实现PID闭环控制', '参数整定'),
                                                                                                                  (7, 'EXP-007', 'Maya建模基础', '数字媒体', '建模', '多边形建模入门', '创建简单物体'),
                                                                                                                  (8, 'EXP-008', 'Blender动画', '数字媒体', '动画', '关键帧动画和渲染', '制作短片'),
                                                                                                                  (9, 'EXP-009', '链表操作', '计算机', '数据结构', '实现单向链表及基本操作', 'C语言实现'),
                                                                                                                  (10, 'EXP-010', '树遍历', '计算机', '数据结构', '二叉树的前序、中序、后序遍历', '递归实现');

-- 7. tc_class（4条）
INSERT IGNORE INTO `tc_class` (`id`, `class_name`, `school_code`) VALUES
                                                               (1, '计算机科学与技术2023级1班', 'SC001'),
                                                               (2, '计算机科学与技术2023级2班', 'SC001'),
                                                               (3, '自动化2023级1班', 'SC001'),
                                                               (4, '数字媒体2023级1班', 'SC001');

-- 8. sys_news（4条）
INSERT IGNORE INTO `sys_news` (`id`, `type`, `title`, `origin`, `editor`, `enclosure`, `content`, `publish_time`) VALUES
                                                                                                               (1, 1, '学校虚拟仿真教学平台上线', '教务处', 'admin', NULL, '经过半年建设，我校虚拟仿真实训平台正式上线...', '2026-07-01 09:00:00'),
                                                                                                               (2, 1, '全国虚拟仿真大赛报名通知', '竞赛委员会', 'admin', '/files/competition.pdf', '2026年全国虚拟仿真创新大赛开始报名...', '2026-07-05 10:00:00'),
                                                                                                               (3, 0, '平台维护公告', '信息中心', 'admin', NULL, '本平台将于7月20日进行系统升级维护，届时暂停服务。', '2026-07-10 16:00:00'),
                                                                                                               (4, 0, '新增VR实验资源', '资源中心', 'admin', NULL, '新增Unity3D VR开发案例资源，欢迎师生使用。', '2026-07-12 08:00:00');

-- ----------------------------
-- 依赖表数据
-- ----------------------------

-- 9. sm_user_profile（对应所有10个用户）
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

-- 10. sm_user_favorite（收藏）
INSERT IGNORE INTO `sm_user_favorite` (`id`, `user_id`, `resource_id`) VALUES
                                                                    (1, 1, 1),
                                                                    (2, 1, 2),
                                                                    (3, 2, 3),
                                                                    (4, 3, 4),
                                                                    (5, 4, 1),
                                                                    (6, 5, 5);

-- 11. sm_user_message（消息）
INSERT IGNORE INTO `sm_user_message` (`id`, `user_id`, `title`, `content`, `type`, `is_read`) VALUES
                                                                                           (1, 1, '实验室申请通过', '您申请的虚拟仿真实验室（A101）已通过审批，请按时使用。', 1, 0),
                                                                                           (2, 2, '课程提醒', '您选修的《虚拟现实技术》将于明日开始，请做好准备。', 2, 0),
                                                                                           (3, 1, '成绩发布', '您的《虚拟现实技术》实验成绩已发布，得分92分。', 3, 0),
                                                                                           (4, 3, '资源审核通知', '您上传的资源《Unity基础教程》已通过审核，现已发布。', 1, 1),
                                                                                           (5, 4, '课程提醒', '您选修的《人工智能基础》课程有新的实验安排。', 2, 0);

-- 12. res_rating（评分）
INSERT IGNORE INTO `res_rating` (`id`, `user_id`, `resource_id`, `score`) VALUES
                                                                       (1, 1, 1, 5),
                                                                       (2, 2, 1, 4),
                                                                       (3, 3, 2, 3),
                                                                       (4, 4, 4, 5),
                                                                       (5, 5, 3, 4),
                                                                       (6, 6, 1, 5);

-- 13. res_media（媒体资源）
INSERT IGNORE INTO `res_media` (`id`, `resource_id`, `file_url`) VALUES
                                                              (1, 1, '/files/unity_tutorial.mp4'),
                                                              (2, 2, '/files/manual.pdf'),
                                                              (3, 3, '/files/audio_3d.mp3'),
                                                              (4, 4, '/files/vr_case.mp4'),
                                                              (5, 5, '/files/python_data.mp4');

-- 14. res_device（设备）
INSERT IGNORE INTO `res_device` (`id`, `number`, `name`, `type`, `version_number`, `laboratory_id`, `status`) VALUES
                                                                                                           (1, 'DEV-001', 'HTC VIVE Pro', 'VR头显', '2.0', 1, 0),
                                                                                                           (2, 'DEV-002', '高性能工作站', '计算机', 'Dell Precision', 1, 1),
                                                                                                           (3, 'DEV-003', 'PLC控制器', '控制设备', 'S7-1200', 2, 0),
                                                                                                           (4, 'DEV-004', '示波器', '测量仪器', 'Tektronix', 2, 0),
                                                                                                           (5, 'DEV-005', '核心交换机', '网络设备', 'Huawei S5700', 3, 0),
                                                                                                           (6, 'DEV-006', '路由器', '网络设备', 'Cisco 2900', 3, 1),
                                                                                                           (7, 'DEV-007', '图形工作站', '计算机', 'HP Z系列', 4, 0),
                                                                                                           (8, 'DEV-008', '数位板', '输入设备', 'Wacom', 4, 0);

-- 15. res_lab_application（实验室申请）
INSERT IGNORE INTO `res_lab_application` (`id`, `number`, `lab_id`, `applicant_name`, `contact_phone`, `purpose`, `name`, `experiment_type`, `profile`, `description`, `start_time`, `end_time`, `status`, `audit_remark`, `approval_time`) VALUES
                                                                                                                                                                                                                                         (1, 'APP-001', 1, '张三', '13800001001', 'VR课程实验', '虚拟现实交互实验', 'VR', '基础交互', '使用Unity开发VR交互', '2026-07-15 08:00:00', '2026-07-15 12:00:00', 1, '同意使用', NOW()),
                                                                                                                                                                                                                                         (2, 'APP-002', 2, '李四', '13800001002', '自动化控制实验', 'PLC编程实验', '控制', '梯形图编程', '基础PLC编程', '2026-07-16 14:00:00', '2026-07-16 18:00:00', 0, NULL, NULL),
                                                                                                                                                                                                                                         (3, 'APP-003', 3, '王五', '13800001003', '网络配置实验', '交换机配置', '网络', 'VLAN划分', '华为交换机VLAN配置', '2026-07-17 09:00:00', '2026-07-17 11:00:00', 2, '设备维护中，暂不可用', NOW()),
                                                                                                                                                                                                                                         (4, 'APP-004', 4, '赵六', '13800001004', '视频剪辑实验', '后期制作实验', '媒体', 'Premiere操作', '视频剪辑与特效', '2026-07-18 13:00:00', '2026-07-18 17:00:00', 1, '同意', NOW());

-- 16. tc_course_experiment（课程-实验关联）
INSERT IGNORE INTO `tc_course_experiment` (`id`, `course_id`, `experiment_id`, `sort_order`) VALUES
                                                                                          (1, 1, 1, 1),
                                                                                          (2, 1, 2, 2),
                                                                                          (3, 2, 3, 1),
                                                                                          (4, 2, 4, 2),
                                                                                          (5, 3, 5, 1),
                                                                                          (6, 3, 6, 2),
                                                                                          (7, 4, 7, 1),
                                                                                          (8, 4, 8, 2),
                                                                                          (9, 5, 9, 1),
                                                                                          (10, 5, 10, 2);

-- 17. tc_teaching_plan（教学计划）
INSERT IGNORE INTO `tc_teaching_plan` (`id`, `name`, `semester`, `course_id`, `teacher_id`, `status`) VALUES
                                                                                                   (1, '2026秋-虚拟现实技术教学计划', '2026-2027-1', 1, 1, 1),
                                                                                                   (2, '2026秋-人工智能基础教学计划', '2026-2027-1', 2, 2, 1),
                                                                                                   (3, '2026秋-自动控制原理教学计划', '2026-2027-1', 3, 1, 1),
                                                                                                   (4, '2026秋-3D建模与动画教学计划', '2026-2027-1', 4, 2, 0),
                                                                                                   (5, '2026秋-数据结构教学计划', '2026-2027-1', 5, 1, 0);

-- 18. tc_schedule（排课）
INSERT IGNORE INTO `tc_schedule` (`id`, `course_id`, `experiment_id`, `class_id`, `teacher_id`, `lab_id`, `schedule_date`, `start_time`, `end_time`, `class_hours`) VALUES
                                                                                                                                                                 (1, 1, 1, 1, 1, 1, '2026-07-20', '08:00:00', '09:50:00', 2),
                                                                                                                                                                 (2, 1, 1, 2, 1, 1, '2026-07-20', '10:00:00', '11:50:00', 2),
                                                                                                                                                                 (3, 1, 2, 1, 1, 1, '2026-07-21', '08:00:00', '09:50:00', 2),
                                                                                                                                                                 (4, 2, 3, 2, 2, 3, '2026-07-21', '10:00:00', '11:50:00', 2),
                                                                                                                                                                 (5, 2, 4, 1, 2, 3, '2026-07-22', '08:00:00', '09:50:00', 2),
                                                                                                                                                                 (6, 3, 5, 3, 1, 2, '2026-07-22', '14:00:00', '15:50:00', 2),
                                                                                                                                                                 (7, 4, 7, 4, 2, 4, '2026-07-23', '09:00:00', '10:50:00', 2);

-- 19. tc_student_course（学生选课）
INSERT IGNORE INTO `tc_student_course` (`id`, `student_id`, `course_id`) VALUES
                                                                      (1, 1, 1),
                                                                      (2, 1, 2),
                                                                      (3, 2, 1),
                                                                      (4, 2, 3),
                                                                      (5, 3, 1),
                                                                      (6, 3, 4),
                                                                      (7, 4, 2),
                                                                      (8, 4, 3),
                                                                      (9, 5, 2),
                                                                      (10, 5, 4),
                                                                      (11, 6, 3);

-- 20. tc_lab_report（实验报告）
INSERT IGNORE INTO `tc_lab_report` (`id`, `schedule_id`, `student_id`, `report_content`, `attachment`, `score`, `status`, `submitted_at`) VALUES
                                                                                                                                       (1, 1, 1, 'VR场景构建实验报告：完成场景搭建，使用Unity3D...', '/reports/rep1.pdf', 92.5, 1, '2026-07-20 10:00:00'),
                                                                                                                                       (2, 2, 2, 'VR场景构建实验报告：场景搭建完成，但纹理贴图有瑕疵...', NULL, 78.0, 2, '2026-07-20 11:00:00'),
                                                                                                                                       (3, 1, 3, 'VR场景构建：完整实现，交互流畅。', '/reports/rep3.pdf', 88.0, 1, '2026-07-20 10:15:00'),
                                                                                                                                       (4, 4, 1, '机器学习实验：线性回归模型实现，R2=0.92', '/reports/rep4.pdf', 85.0, 1, '2026-07-21 11:00:00'),
                                                                                                                                       (5, 4, 4, '机器学习实验：模型欠拟合，需增加特征', NULL, 65.0, 2, '2026-07-21 11:30:00'),
                                                                                                                                       (6, 6, 6, 'PLC控制实验：成功实现电机启停和正反转', '/reports/rep6.pdf', 95.0, 1, '2026-07-22 15:00:00'),
                                                                                                                                       (7, 6, 2, NULL, NULL, NULL, 0, NULL);

-- 21. tc_grade（成绩评定）
INSERT IGNORE INTO `tc_grade` (`id`, `schedule_id`, `student_id`, `overall_score`, `comment`, `status`, `graded_at`) VALUES
                                                                                                                  (1, 1, 1, 92.5, '实验完成度高，逻辑清晰', 1, NOW()),
                                                                                                                  (2, 1, 3, 88.0, '基本功能完善，交互细节可优化', 1, NOW()),
                                                                                                                  (3, 2, 2, 78.0, '场景搭建完成，但交互部分待加强', 1, NOW()),
                                                                                                                  (4, 4, 1, 85.0, '模型训练结果良好，报告详细', 1, NOW()),
                                                                                                                  (5, 4, 4, 65.0, '模型效果不理想，需重新调参', 1, NOW()),
                                                                                                                  (6, 6, 6, 95.0, '操作规范，程序正确，报告优秀', 1, NOW()),
                                                                                                                  (7, 6, 2, NULL, NULL, 0, NULL);

-- 22. sys_operation_log（操作日志）
INSERT IGNORE INTO `sys_operation_log` (`id`, `user_id`, `user_role`, `username`, `action`, `ip_address`) VALUES
                                                                                                       (1, 9, '管理员', 'admin01', '添加用户 student01', '192.168.1.203'),
                                                                                                       (2, 7, '老师', 'teacher01', '发布课程《虚拟现实技术》', '192.168.1.201'),
                                                                                                       (3, 1, '学生', 'student01', '收藏资源 Unity3D入门教程', '192.168.1.101'),
                                                                                                       (4, 9, '管理员', 'admin01', '审批实验室申请 APP-001 通过', '192.168.1.203'),
                                                                                                       (5, 8, '老师', 'teacher02', '为班级 计算机2023级1班 安排实验 AI-101-1', '192.168.1.202'),
                                                                                                       (6, 9, '管理员', 'admin01', '审核资源 RES-004 通过', '192.168.1.203');