-- ==============================
-- EmiyaOJ Online Judge System Database Schema
-- Database: emiya-oj
-- ==============================

-- 编程语言表
CREATE TABLE `language` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '语言ID',
    `name` VARCHAR(50) NOT NULL COMMENT '语言名称，如 C, C++, Java, Python',
    `version` VARCHAR(50) NOT NULL COMMENT '版本，如 gcc-11, jdk-21, python-3.11',
    `compile_command` TEXT COMMENT '编译命令模板',
    `execute_command` TEXT NOT NULL COMMENT '执行命令模板',
    `source_file_ext` VARCHAR(20) NOT NULL COMMENT '源文件扩展名，如 .c, .cpp, .java, .py',
    `executable_ext` VARCHAR(20) COMMENT '可执行文件扩展名',
    `is_compiled` TINYINT DEFAULT 1 COMMENT '是否需要编译：0-否，1-是',
    `time_limit_multiplier` DECIMAL(3,2) DEFAULT 1.0 COMMENT '时间限制倍数',
    `memory_limit_multiplier` DECIMAL(3,2) DEFAULT 1.0 COMMENT '内存限制倍数',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_name_version` (`name`, `version`),
    INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='编程语言表';

-- 题目表
CREATE TABLE `problem` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '题目ID',
    `title` VARCHAR(255) NOT NULL COMMENT '题目标题',
    `description` TEXT NOT NULL COMMENT '题目描述',
    `input_description` TEXT COMMENT '输入描述',
    `output_description` TEXT COMMENT '输出描述',
    `sample_input` TEXT COMMENT '样例输入',
    `sample_output` TEXT COMMENT '样例输出',
    `hint` TEXT COMMENT '提示信息',
    `difficulty` TINYINT DEFAULT 1 COMMENT '难度：1-简单，2-中等，3-困难',
    `time_limit` INT NOT NULL COMMENT 'CPU时间限制（毫秒）',
    `memory_limit` INT NOT NULL COMMENT '内存限制（MB）',
    `stack_limit` INT DEFAULT 128 COMMENT '栈内存限制（MB）',
    `source` VARCHAR(255) COMMENT '题目来源',
    `author_id` BIGINT COMMENT '出题人ID',
    `accept_count` INT DEFAULT 0 COMMENT '通过次数',
    `submit_count` INT DEFAULT 0 COMMENT '提交次数',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-隐藏，1-公开',
    `deleted` TINYINT DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by` BIGINT COMMENT '创建者',
    `update_by` BIGINT COMMENT '更新者',
    PRIMARY KEY (`id`),
    INDEX `idx_difficulty` (`difficulty`),
    INDEX `idx_status` (`status`),
    INDEX `idx_author_id` (`author_id`),
    INDEX `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='题目表';

-- 测试用例表
CREATE TABLE `test_case` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '测试用例ID',
    `problem_id` BIGINT NOT NULL COMMENT '题目ID',
    `input` LONGTEXT NOT NULL COMMENT '输入数据',
    `output` LONGTEXT NOT NULL COMMENT '预期输出',
    `is_sample` TINYINT DEFAULT 0 COMMENT '是否为样例：0-否，1-是',
    `score` INT DEFAULT 0 COMMENT '分值（若支持部分分）',
    `sort_order` INT DEFAULT 0 COMMENT '排序',
    `deleted` TINYINT DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    INDEX `idx_problem_id` (`problem_id`),
    INDEX `idx_is_sample` (`is_sample`),
    INDEX `idx_sort_order` (`sort_order`),
    CONSTRAINT `fk_testcase_problem` FOREIGN KEY (`problem_id`) REFERENCES `problem` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='测试用例表';

-- 提交记录表
CREATE TABLE `submission` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '提交ID',
    `problem_id` BIGINT NOT NULL COMMENT '题目ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `language_id` BIGINT NOT NULL COMMENT '语言ID',
    `code` LONGTEXT NOT NULL COMMENT '源代码',
    `status` VARCHAR(50) NOT NULL COMMENT '判题状态：Pending, Judging, Accepted, Wrong Answer, Time Limit Exceeded, Memory Limit Exceeded, Runtime Error, Compile Error, System Error',
    `score` INT DEFAULT 0 COMMENT '得分',
    `time_used` INT DEFAULT 0 COMMENT '实际使用时间（毫秒）',
    `memory_used` INT DEFAULT 0 COMMENT '实际使用内存（KB）',
    `error_message` TEXT COMMENT '错误信息',
    `compile_message` TEXT COMMENT '编译信息',
    `pass_rate` VARCHAR(50) COMMENT '通过率，如 10/10',
    `ip_address` VARCHAR(50) COMMENT '提交IP',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '提交时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    INDEX `idx_problem_id` (`problem_id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_language_id` (`language_id`),
    INDEX `idx_status` (`status`),
    INDEX `idx_create_time` (`create_time`),
    CONSTRAINT `fk_submission_problem` FOREIGN KEY (`problem_id`) REFERENCES `problem` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_submission_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_submission_language` FOREIGN KEY (`language_id`) REFERENCES `language` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='提交记录表';

-- 测试用例判题结果表
CREATE TABLE `submission_result` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '结果ID',
    `submission_id` BIGINT NOT NULL COMMENT '提交ID',
    `test_case_id` BIGINT NOT NULL COMMENT '测试用例ID',
    `status` VARCHAR(50) NOT NULL COMMENT '判题状态',
    `time_used` INT DEFAULT 0 COMMENT '使用时间（毫秒）',
    `memory_used` INT DEFAULT 0 COMMENT '使用内存（KB）',
    `error_message` TEXT COMMENT '错误信息',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    INDEX `idx_submission_id` (`submission_id`),
    INDEX `idx_test_case_id` (`test_case_id`),
    CONSTRAINT `fk_result_submission` FOREIGN KEY (`submission_id`) REFERENCES `submission` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_result_testcase` FOREIGN KEY (`test_case_id`) REFERENCES `test_case` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='测试用例判题结果表';

-- 标签表
CREATE TABLE `tag` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '标签ID',
    `name` VARCHAR(50) NOT NULL COMMENT '标签名称',
    `description` VARCHAR(255) COMMENT '标签描述',
    `color` VARCHAR(20) DEFAULT '#409EFF' COMMENT '标签颜色',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='标签表';

-- 题目标签关联表
CREATE TABLE `problem_tag` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `problem_id` BIGINT NOT NULL COMMENT '题目ID',
    `tag_id` BIGINT NOT NULL COMMENT '标签ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_problem_tag` (`problem_id`, `tag_id`),
    INDEX `idx_problem_id` (`problem_id`),
    INDEX `idx_tag_id` (`tag_id`),
    CONSTRAINT `fk_problem_tag_problem` FOREIGN KEY (`problem_id`) REFERENCES `problem` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_problem_tag_tag` FOREIGN KEY (`tag_id`) REFERENCES `tag` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='题目标签关联表';
