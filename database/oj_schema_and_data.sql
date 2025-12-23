-- OJ系统数据库表结构和初始数据

-- 编程语言表
CREATE TABLE `language` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '语言ID',
  `name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '语言名称，如 C, C++, Java, Python',
  `version` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '版本，如 gcc-11, jdk-21, python-3.11',
  `compile_command` text COLLATE utf8mb4_unicode_ci COMMENT '编译命令模板',
  `execute_command` text COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '执行命令模板',
  `source_file_ext` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '源文件扩展名，如 .c, .cpp, .java, .py',
  `executable_ext` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '可执行文件扩展名',
  `is_compiled` tinyint DEFAULT '1' COMMENT '是否需要编译：0-否，1-是',
  `time_limit_multiplier` decimal(3,2) DEFAULT '1.00' COMMENT '时间限制倍数',
  `memory_limit_multiplier` decimal(3,2) DEFAULT '1.00' COMMENT '内存限制倍数',
  `status` tinyint DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_name_version` (`name`,`version`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='编程语言表';

-- 题目表
CREATE TABLE `problem` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '题目ID',
  `title` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '题目标题',
  `description` text COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '题目描述',
  `input_description` text COLLATE utf8mb4_unicode_ci COMMENT '输入描述',
  `output_description` text COLLATE utf8mb4_unicode_ci COMMENT '输出描述',
  `sample_input` text COLLATE utf8mb4_unicode_ci COMMENT '样例输入',
  `sample_output` text COLLATE utf8mb4_unicode_ci COMMENT '样例输出',
  `hint` text COLLATE utf8mb4_unicode_ci COMMENT '提示信息',
  `difficulty` tinyint DEFAULT '1' COMMENT '难度：1-简单，2-中等，3-困难',
  `time_limit` int NOT NULL COMMENT 'CPU时间限制（毫秒）',
  `memory_limit` int NOT NULL COMMENT '内存限制（MB）',
  `stack_limit` int DEFAULT '128' COMMENT '栈内存限制（MB）',
  `source` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '题目来源',
  `author_id` bigint DEFAULT NULL COMMENT '出题人ID',
  `accept_count` int DEFAULT '0' COMMENT '通过次数',
  `submit_count` int DEFAULT '0' COMMENT '提交次数',
  `status` tinyint DEFAULT '1' COMMENT '状态：0-隐藏，1-公开',
  `deleted` tinyint DEFAULT '0' COMMENT '是否删除：0-未删除，1-已删除',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建者',
  `update_by` bigint DEFAULT NULL COMMENT '更新者',
  PRIMARY KEY (`id`),
  KEY `idx_difficulty` (`difficulty`),
  KEY `idx_status` (`status`),
  KEY `idx_author_id` (`author_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='题目表';

-- 标签表
CREATE TABLE `tag` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '标签ID',
  `name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '标签名称',
  `description` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '标签描述',
  `color` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT '#409EFF' COMMENT '标签颜色',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='标签表';

-- 题目标签关联表
CREATE TABLE `problem_tag` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `problem_id` bigint NOT NULL COMMENT '题目ID',
  `tag_id` bigint NOT NULL COMMENT '标签ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_problem_tag` (`problem_id`,`tag_id`),
  KEY `idx_problem_id` (`problem_id`),
  KEY `idx_tag_id` (`tag_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='题目标签关联表';

-- 测试用例表
CREATE TABLE `test_case` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '测试用例ID',
  `problem_id` bigint NOT NULL COMMENT '题目ID',
  `input` longtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '输入数据',
  `output` longtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '预期输出',
  `is_sample` tinyint DEFAULT '0' COMMENT '是否为样例：0-否，1-是',
  `score` int DEFAULT '0' COMMENT '分值（若支持部分分）',
  `sort_order` int DEFAULT '0' COMMENT '排序',
  `deleted` tinyint DEFAULT '0' COMMENT '是否删除：0-未删除，1-已删除',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_problem_id` (`problem_id`),
  KEY `idx_is_sample` (`is_sample`),
  KEY `idx_sort_order` (`sort_order`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='测试用例表';

-- 提交记录表
CREATE TABLE `submission` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '提交ID',
  `problem_id` bigint NOT NULL COMMENT '题目ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `language_id` bigint NOT NULL COMMENT '语言ID',
  `code` longtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '源代码',
  `status` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '判题状态：Pending, Judging, Accepted, Wrong Answer, Time Limit Exceeded, Memory Limit Exceeded, Runtime Error, Compile Error, System Error',
  `score` int DEFAULT '0' COMMENT '得分',
  `time_used` int DEFAULT '0' COMMENT '实际使用时间（毫秒）',
  `memory_used` int DEFAULT '0' COMMENT '实际使用内存（KB）',
  `error_message` text COLLATE utf8mb4_unicode_ci COMMENT '错误信息',
  `compile_message` text COLLATE utf8mb4_unicode_ci COMMENT '编译信息',
  `pass_rate` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '通过率，如 10/10',
  `ip_address` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '提交IP',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '提交时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_problem_id` (`problem_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_language_id` (`language_id`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='提交记录表';

-- 测试用例判题结果表
CREATE TABLE `submission_result` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '结果ID',
  `submission_id` bigint NOT NULL COMMENT '提交ID',
  `test_case_id` bigint NOT NULL COMMENT '测试用例ID',
  `status` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '判题状态',
  `time_used` int DEFAULT '0' COMMENT '使用时间（毫秒）',
  `memory_used` int DEFAULT '0' COMMENT '使用内存（KB）',
  `error_message` text COLLATE utf8mb4_unicode_ci COMMENT '错误信息',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_submission_id` (`submission_id`),
  KEY `idx_test_case_id` (`test_case_id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='测试用例判题结果表';

-- 插入C和C++语言初始数据
INSERT INTO `language` (`id`, `name`, `version`, `compile_command`, `execute_command`, `source_file_ext`, `executable_ext`, `is_compiled`, `time_limit_multiplier`, `memory_limit_multiplier`, `status`) VALUES
(1, 'C', 'gcc-11', '/usr/bin/gcc {source} -o {output}', '{executable}', '.c', '', 1, 1.00, 1.00, 1),
(2, 'C++', 'g++-11', '/usr/bin/g++ {source} -o {output}', '{executable}', '.cpp', '', 1, 1.00, 1.00, 1);

-- 插入示例题目：A+B Problem
INSERT INTO `problem` (`id`, `title`, `description`, `input_description`, `output_description`, `sample_input`, `sample_output`, `hint`, `difficulty`, `time_limit`, `memory_limit`, `stack_limit`, `accept_count`, `submit_count`, `status`, `deleted`) VALUES
(1, 'A+B Problem', '计算两个整数的和。', '输入两个整数 a 和 b，以空格分隔。', '输出一个整数，表示 a + b 的值。', '1 2', '3', '这是一道非常简单的题目，适合初学者练习。', 1, 1000, 256, 128, 0, 0, 1, 0);

-- 插入测试用例
INSERT INTO `test_case` (`problem_id`, `input`, `output`, `is_sample`, `score`, `sort_order`, `deleted`) VALUES
(1, '1 2', '3', 1, 10, 1, 0),
(1, '100 200', '300', 0, 10, 2, 0),
(1, '-1 1', '0', 0, 10, 3, 0),
(1, '0 0', '0', 0, 10, 4, 0),
(1, '999999999 1', '1000000000', 0, 10, 5, 0);

-- 插入常见标签
INSERT INTO `tag` (`name`, `description`, `color`) VALUES
('数学', '数学相关题目', '#67C23A'),
('字符串', '字符串处理', '#409EFF'),
('数组', '数组操作', '#E6A23C'),
('动态规划', '动态规划算法', '#F56C6C'),
('贪心', '贪心算法', '#909399'),
('模拟', '模拟题', '#67C23A'),
('入门', '适合初学者', '#409EFF');

-- 关联题目和标签
INSERT INTO `problem_tag` (`problem_id`, `tag_id`) VALUES
(1, 1),  -- A+B Problem 关联 数学
(1, 7);  -- A+B Problem 关联 入门
