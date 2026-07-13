-- ============================================================
-- 虚拟仿真实训教学管理及资源共享云平台 - 建表脚本
-- 依据：《数据库设计说明书》v2.1
-- 表总数：21张
-- ============================================================

-- ============================================================
-- 一、基础表（无外键依赖）
-- ============================================================

-- 1. 用户主表
CREATE TABLE IF NOT EXISTS `sm_user` (
                                         `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户主键ID',
                                         `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `password_hash` VARCHAR(255) NOT NULL COMMENT '密码哈希',
    `role` TINYINT(1) NOT NULL COMMENT '用户角色：1-学生，2-老师，3-社会人士，4-管理员',
    `phone` VARCHAR(11) NOT NULL COMMENT '手机号',
    `email` VARCHAR(255) DEFAULT NULL COMMENT '邮箱',
    `statu` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '账号状态：0-停用，1-启用',
    `last_login_time` DATETIME DEFAULT NULL COMMENT '最后登录时间',
    `last_login_ip` VARCHAR(50) DEFAULT NULL COMMENT '最后登录IP',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `del_flag` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '删除标记：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    UNIQUE KEY `uk_phone` (`phone`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户主表';

-- 2. 资源主表
CREATE TABLE IF NOT EXISTS `res_resource` (
                                              `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '资源主键ID',
                                              `number` VARCHAR(50) DEFAULT NULL COMMENT '资源编号',
    `name` VARCHAR(255) NOT NULL COMMENT '资源名称',
    `type` TINYINT(1) NOT NULL COMMENT '资源类型：1-视频，2-音频，3-文档',
    `thumbnail` VARCHAR(500) DEFAULT NULL COMMENT '缩略图地址',
    `school` VARCHAR(100) DEFAULT NULL COMMENT '所属学校',
    `leader` VARCHAR(30) DEFAULT NULL COMMENT '负责人',
    `category` VARCHAR(100) DEFAULT NULL COMMENT '专业大类',
    `status` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '审核状态：0-待审核，1-已发布，2-已驳回',
    `audit_remark` VARCHAR(500) DEFAULT NULL COMMENT '审核备注/驳回原因',
    `is_shared` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '是否开放共享：0-不共享，1-共享',
    `profile` LONGTEXT DEFAULT NULL COMMENT '概要介绍',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `del_flag` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '删除标记：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_number` (`number`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资源主表';

-- 3. 实验室表
CREATE TABLE IF NOT EXISTS `res_laboratory` (
                                                `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '实验室主键ID',
                                                `number` VARCHAR(50) DEFAULT NULL COMMENT '实验室编号',
    `name` VARCHAR(100) NOT NULL COMMENT '实验室名称',
    `station_num` INT DEFAULT NULL COMMENT '工位数量',
    `area` DECIMAL(10,2) DEFAULT NULL COMMENT '面积（㎡）',
    `address` VARCHAR(200) DEFAULT NULL COMMENT '地址',
    `profile` VARCHAR(1000) DEFAULT NULL COMMENT '简介',
    `description` LONGTEXT DEFAULT NULL COMMENT '概要介绍（富文本）',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `del_flag` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '删除标记：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_number` (`number`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='实验室表';

-- 4. 师资管理表
CREATE TABLE IF NOT EXISTS `tc_teacher` (
                                            `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '师资主键ID',
                                            `teacher_id` VARCHAR(30) DEFAULT NULL COMMENT '教师ID',
    `name` VARCHAR(10) NOT NULL COMMENT '姓名',
    `type` VARCHAR(30) DEFAULT NULL COMMENT '师资类型：0-实训老师，1-非实训老师',
    `phone` VARCHAR(11) NOT NULL COMMENT '手机号',
    `email` VARCHAR(255) DEFAULT NULL COMMENT '邮箱',
    `company` VARCHAR(100) DEFAULT NULL COMMENT '所在单位',
    `on_job` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '在职状态：0-离职，1-在职',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_teacher_id` (`teacher_id`),
    UNIQUE KEY `uk_phone` (`phone`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='师资管理表';

-- 5. 课程表
CREATE TABLE IF NOT EXISTS `tc_course` (
                                           `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '课程主键ID',
                                           `course_code` VARCHAR(50) DEFAULT NULL COMMENT '课程编号',
    `course_name` VARCHAR(255) NOT NULL COMMENT '课程名称',
    `course_type` VARCHAR(100) DEFAULT NULL COMMENT '课程类型',
    `class_hours` INT DEFAULT NULL COMMENT '课时',
    `credit` DECIMAL(3,1) DEFAULT NULL COMMENT '学分',
    `teacher_id` BIGINT NOT NULL COMMENT '负责教师ID',
    `introduction` LONGTEXT DEFAULT NULL COMMENT '课程介绍',
    `outline` LONGTEXT DEFAULT NULL COMMENT '课程大纲',
    `status` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '课程状态：0-草稿，1-已发布，2-已下架',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_course_code` (`course_code`),
    KEY `idx_teacher_id` (`teacher_id`),
    CONSTRAINT `fk_course_teacher` FOREIGN KEY (`teacher_id`) REFERENCES `tc_teacher` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程表';

-- 6. 实验表
CREATE TABLE IF NOT EXISTS `tc_experiment` (
                                               `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '实验主键ID',
                                               `number` VARCHAR(50) DEFAULT NULL COMMENT '实验编号',
    `name` VARCHAR(255) NOT NULL COMMENT '实验名称',
    `course_id` BIGINT DEFAULT NULL COMMENT '所属课程ID',
    `objective` VARCHAR(500) DEFAULT NULL COMMENT '实验目标',
    `steps` LONGTEXT DEFAULT NULL COMMENT '实验步骤',
    `report_template` LONGTEXT DEFAULT NULL COMMENT '报告模板',
    `status` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '实验状态：0-草稿，1-已发布',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_number` (`number`),
    KEY `idx_course_id` (`course_id`),
    CONSTRAINT `fk_experiment_course` FOREIGN KEY (`course_id`) REFERENCES `tc_course` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='实验表';

-- 7. 班级表
CREATE TABLE IF NOT EXISTS `tc_class` (
                                          `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '班级主键ID',
                                          `class_name` VARCHAR(255) NOT NULL COMMENT '班级名称',
    `school_code` VARCHAR(50) DEFAULT NULL COMMENT '学校编号',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='班级表';

-- 8. 新闻表
CREATE TABLE IF NOT EXISTS `sys_news` (
                                          `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '新闻主键ID',
                                          `title` VARCHAR(255) NOT NULL COMMENT '标题',
    `origin` VARCHAR(255) DEFAULT NULL COMMENT '来源',
    `editor` VARCHAR(50) DEFAULT NULL COMMENT '编辑人员',
    `enclosure` VARCHAR(500) DEFAULT NULL COMMENT '图片地址',
    `content` LONGTEXT NOT NULL COMMENT '内容',
    `publish_time` DATETIME DEFAULT NULL COMMENT '发布时间',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='新闻表';

-- 9. 公告表
CREATE TABLE IF NOT EXISTS `sys_notice` (
                                            `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '公告主键ID',
                                            `title` VARCHAR(255) NOT NULL COMMENT '标题',
    `editor` VARCHAR(50) DEFAULT NULL COMMENT '编辑人员',
    `content` LONGTEXT NOT NULL COMMENT '内容',
    `publish_time` DATETIME DEFAULT NULL COMMENT '发布时间',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='公告表';

-- ============================================================
-- 二、依赖表（有外键引用）
-- ============================================================

-- 10. 用户详细信息表（依赖 sm_user, tc_class）
CREATE TABLE IF NOT EXISTS `sm_user_profile` (
                                                 `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '详情主键ID',
                                                 `user_id` BIGINT NOT NULL COMMENT '用户ID',
                                                 `real_name` VARCHAR(30) NOT NULL COMMENT '真实姓名',
    `school_code` VARCHAR(50) DEFAULT NULL COMMENT '学校编号',
    `class_name` VARCHAR(255) DEFAULT NULL COMMENT '班级名称',
    `class_id` BIGINT DEFAULT NULL COMMENT '班级ID',
    `occupation_type` VARCHAR(50) DEFAULT NULL COMMENT '职业类型',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_class_id` (`class_id`),
    CONSTRAINT `fk_profile_user` FOREIGN KEY (`user_id`) REFERENCES `sm_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `fk_profile_class` FOREIGN KEY (`class_id`) REFERENCES `tc_class` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户详细信息表';

-- 11. 用户收藏表（依赖 sm_user, res_resource）
CREATE TABLE IF NOT EXISTS `sm_user_favorite` (
                                                  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '收藏主键ID',
                                                  `user_id` BIGINT NOT NULL COMMENT '用户ID',
                                                  `resource_id` BIGINT NOT NULL COMMENT '资源ID',
                                                  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
                                                  PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_resource` (`user_id`, `resource_id`),
    CONSTRAINT `fk_fav_user` FOREIGN KEY (`user_id`) REFERENCES `sm_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `fk_fav_resource` FOREIGN KEY (`resource_id`) REFERENCES `res_resource` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户收藏表';

-- 12. 用户消息表（依赖 sm_user）
CREATE TABLE IF NOT EXISTS `sm_user_message` (
                                                 `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '消息主键ID',
                                                 `user_id` BIGINT NOT NULL COMMENT '用户ID',
                                                 `title` VARCHAR(255) NOT NULL COMMENT '消息标题',
    `content` LONGTEXT NOT NULL COMMENT '消息内容',
    `type` TINYINT(1) NOT NULL COMMENT '消息类型：1-审核，2-课程，3-成绩',
    `is_read` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否已读：0-未读，1-已读',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    CONSTRAINT `fk_msg_user` FOREIGN KEY (`user_id`) REFERENCES `sm_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户消息表';

-- 13. 资源评分表（依赖 sm_user, res_resource）
CREATE TABLE IF NOT EXISTS `res_rating` (
                                            `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '评分主键ID',
                                            `user_id` BIGINT NOT NULL COMMENT '用户ID',
                                            `resource_id` BIGINT NOT NULL COMMENT '资源ID',
                                            `score` TINYINT(1) NOT NULL COMMENT '评分值（1-5星）',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_resource` (`user_id`, `resource_id`),
    CONSTRAINT `fk_rating_user` FOREIGN KEY (`user_id`) REFERENCES `sm_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `fk_rating_resource` FOREIGN KEY (`resource_id`) REFERENCES `res_resource` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资源评分表';

-- 14. 媒体资源详情表（依赖 res_resource）
CREATE TABLE IF NOT EXISTS `res_media` (
                                           `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                           `resource_id` BIGINT NOT NULL COMMENT '资源ID',
                                           `file_url` VARCHAR(500) DEFAULT NULL COMMENT '文件地址',
    PRIMARY KEY (`id`),
    KEY `idx_resource_id` (`resource_id`),
    CONSTRAINT `fk_media_resource` FOREIGN KEY (`resource_id`) REFERENCES `res_resource` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='媒体资源详情表';

-- 15. 设备管理表（依赖 res_laboratory）
CREATE TABLE IF NOT EXISTS `res_device` (
                                            `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '设备主键ID',
                                            `number` VARCHAR(30) DEFAULT NULL COMMENT '设备编号',
    `name` VARCHAR(50) NOT NULL COMMENT '设备名称',
    `type` VARCHAR(50) DEFAULT NULL COMMENT '类别',
    `version_number` VARCHAR(20) DEFAULT NULL COMMENT '版本号',
    `laboratory_id` BIGINT DEFAULT NULL COMMENT '所在实验室ID',
    `status` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '设备状态：0-空闲，1-使用中，2-保修，3-损坏',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `del_flag` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '删除标记：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_laboratory_id` (`laboratory_id`),
    CONSTRAINT `fk_device_lab` FOREIGN KEY (`laboratory_id`) REFERENCES `res_laboratory` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='设备管理表';

-- 16. 实验室申请表（依赖 res_laboratory）
CREATE TABLE IF NOT EXISTS `res_lab_application` (
                                                     `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '申请主键ID',
                                                     `number` VARCHAR(50) DEFAULT NULL COMMENT '申请编号',
    `lab_id` BIGINT NOT NULL COMMENT '实验室ID',
    `applicant_name` VARCHAR(30) NOT NULL COMMENT '申请人姓名',
    `contact_phone` VARCHAR(11) NOT NULL COMMENT '联系电话',
    `purpose` LONGTEXT DEFAULT NULL COMMENT '申请说明',
    `name` VARCHAR(255) NOT NULL COMMENT '实验名称',
    `objective` VARCHAR(500) DEFAULT NULL COMMENT '实验目标',
    `steps` LONGTEXT DEFAULT NULL COMMENT '实验步骤',
    `start_time` DATETIME NOT NULL COMMENT '使用开始时间',
    `end_time` DATETIME NOT NULL COMMENT '使用结束时间',
    `status` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '审批状态：0-待审批，1-已通过，2-已拒绝',
    `audit_remark` VARCHAR(500) DEFAULT NULL COMMENT '审核备注/驳回原因',
    `approval_time` DATETIME DEFAULT NULL COMMENT '审批时间',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_lab_id` (`lab_id`),
    CONSTRAINT `fk_app_lab` FOREIGN KEY (`lab_id`) REFERENCES `res_laboratory` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='实验室申请表';

-- 17. 教学计划表（依赖 tc_class, sm_user）
CREATE TABLE IF NOT EXISTS `tc_teaching_plan` (
                                                  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '计划主键ID',
                                                  `name` VARCHAR(255) NOT NULL COMMENT '计划名称',
    `semester` VARCHAR(50) NOT NULL COMMENT '学期',
    `class_id` BIGINT NOT NULL COMMENT '班级ID',
    `create_id` BIGINT NOT NULL COMMENT '创建人ID',
    `start_date` DATE NOT NULL COMMENT '开始日期',
    `end_date` DATE NOT NULL COMMENT '结束日期',
    `status` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '计划状态：0-草稿，1-已发布',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_class_id` (`class_id`),
    KEY `idx_create_id` (`create_id`),
    CONSTRAINT `fk_plan_class` FOREIGN KEY (`class_id`) REFERENCES `tc_class` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `fk_plan_creator` FOREIGN KEY (`create_id`) REFERENCES `sm_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='教学计划表';

-- 18. 教学计划排课明细表（依赖 tc_teaching_plan, tc_course, tc_experiment）
CREATE TABLE IF NOT EXISTS `tc_plan_detail` (
                                                `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '排课主键ID',
                                                `plan_id` BIGINT NOT NULL COMMENT '所属计划ID',
                                                `course_id` BIGINT NOT NULL COMMENT '课程ID',
                                                `experiment_id` BIGINT NOT NULL COMMENT '实验ID',
                                                `schedule_date` DATE NOT NULL COMMENT '上课日期',
                                                `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                                `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                                PRIMARY KEY (`id`),
    KEY `idx_plan_id` (`plan_id`),
    KEY `idx_course_id` (`course_id`),
    KEY `idx_experiment_id` (`experiment_id`),
    CONSTRAINT `fk_detail_plan` FOREIGN KEY (`plan_id`) REFERENCES `tc_teaching_plan` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `fk_detail_course` FOREIGN KEY (`course_id`) REFERENCES `tc_course` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `fk_detail_experiment` FOREIGN KEY (`experiment_id`) REFERENCES `tc_experiment` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='教学计划排课明细表';

-- 19. 实验报告表（依赖 tc_plan_detail, sm_user）
CREATE TABLE IF NOT EXISTS `tc_lab_report` (
                                               `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '报告主键ID',
                                               `schedule_id` BIGINT NOT NULL COMMENT '排课ID',
                                               `student_id` BIGINT NOT NULL COMMENT '学生ID',
                                               `report_content` LONGTEXT DEFAULT NULL COMMENT '报告详情',
                                               `attachment` VARCHAR(500) DEFAULT NULL COMMENT '报告附件',
    `score` DECIMAL(5,2) DEFAULT NULL COMMENT '成绩（分）',
    `evaluation_status` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '评定状态：0-待评定，1-已评定',
    `submitted_at` DATETIME DEFAULT NULL COMMENT '提交时间',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_schedule_id` (`schedule_id`),
    KEY `idx_student_id` (`student_id`),
    CONSTRAINT `fk_report_schedule` FOREIGN KEY (`schedule_id`) REFERENCES `tc_plan_detail` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `fk_report_student` FOREIGN KEY (`student_id`) REFERENCES `sm_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='实验报告表';

-- 20. 成绩评定表（依赖 tc_plan_detail, sm_user）
CREATE TABLE IF NOT EXISTS `tc_grade` (
                                          `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '评定主键ID',
                                          `schedule_id` BIGINT NOT NULL COMMENT '排课ID',
                                          `student_id` BIGINT NOT NULL COMMENT '学生ID',
                                          `overall_score` DECIMAL(5,2) DEFAULT NULL COMMENT '综合成绩',
    `comment` VARCHAR(500) DEFAULT NULL COMMENT '评语',
    `publish_status` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '发布状态：0-未发布，1-已发布',
    `graded_at` DATETIME DEFAULT NULL COMMENT '评定时间',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_schedule_student` (`schedule_id`, `student_id`),
    CONSTRAINT `fk_grade_schedule` FOREIGN KEY (`schedule_id`) REFERENCES `tc_plan_detail` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `fk_grade_student` FOREIGN KEY (`student_id`) REFERENCES `sm_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='成绩评定表';

-- 21. 操作日志表（依赖 sm_user）
CREATE TABLE IF NOT EXISTS `sys_operation_log` (
                                                   `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '日志主键ID',
                                                   `user_id` BIGINT DEFAULT NULL COMMENT '用户ID',
                                                   `user_role` VARCHAR(20) DEFAULT NULL COMMENT '账号类型',
    `username` VARCHAR(50) DEFAULT NULL COMMENT '账号',
    `action` VARCHAR(500) NOT NULL COMMENT '操作内容',
    `ip_address` VARCHAR(50) DEFAULT NULL COMMENT 'IP地址',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    CONSTRAINT `fk_log_user` FOREIGN KEY (`user_id`) REFERENCES `sm_user` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';