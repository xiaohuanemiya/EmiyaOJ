-- ==============================
-- EmiyaOJ 在线评测系统数据库表
-- 数据库: emiya-oj
-- ==============================

-- 题目表
CREATE TABLE `problem` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '题目ID',
    `title` VARCHAR(200) NOT NULL COMMENT '题目标题',
    `description` TEXT COMMENT '题目描述',
    `input_description` TEXT COMMENT '输入描述',
    `output_description` TEXT COMMENT '输出描述',
    `difficulty` TINYINT DEFAULT 1 COMMENT '难度：1-简单，2-中等，3-困难',
    `time_limit` INT DEFAULT 1000 COMMENT '时间限制(ms)',
    `memory_limit` INT DEFAULT 256 COMMENT '内存限制(MB)',
    `stack_limit` INT DEFAULT 128 COMMENT '栈限制(MB)',
    `examples` JSON COMMENT '示例用例',
    `hint` TEXT COMMENT '提示',
    `tags` VARCHAR(500) COMMENT '标签，逗号分隔',
    `accepted_count` INT DEFAULT 0 COMMENT '通过次数',
    `submit_count` INT DEFAULT 0 COMMENT '提交次数',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `deleted` TINYINT DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by` BIGINT COMMENT '创建者',
    `update_by` BIGINT COMMENT '更新者',
    PRIMARY KEY (`id`),
    INDEX `idx_difficulty` (`difficulty`),
    INDEX `idx_status` (`status`),
    INDEX `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='题目表';

-- 测试用例表
CREATE TABLE `test_case` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '测试用例ID',
    `problem_id` BIGINT NOT NULL COMMENT '题目ID',
    `input` TEXT NOT NULL COMMENT '输入数据',
    `output` TEXT NOT NULL COMMENT '期望输出',
    `is_sample` TINYINT DEFAULT 0 COMMENT '是否为样例：0-否，1-是',
    `score` INT DEFAULT 0 COMMENT '分数（部分题目使用）',
    `sort_order` INT DEFAULT 0 COMMENT '排序',
    `deleted` TINYINT DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    INDEX `idx_problem_id` (`problem_id`),
    CONSTRAINT `fk_test_case_problem` FOREIGN KEY (`problem_id`) REFERENCES `problem` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='测试用例表';

-- 提交记录表
CREATE TABLE `submission` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '提交ID',
    `problem_id` BIGINT NOT NULL COMMENT '题目ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `language` VARCHAR(20) NOT NULL COMMENT '编程语言',
    `code` TEXT NOT NULL COMMENT '代码',
    `status` VARCHAR(30) DEFAULT 'Pending' COMMENT '判题状态',
    `time_used` BIGINT DEFAULT 0 COMMENT '运行时间(ns)',
    `memory_used` BIGINT DEFAULT 0 COMMENT '内存使用(bytes)',
    `pass_rate` VARCHAR(20) COMMENT '通过率（如 3/10）',
    `error_message` TEXT COMMENT '错误信息',
    `judge_result` JSON COMMENT '详细判题结果',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '提交时间',
    `finish_time` DATETIME COMMENT '完成时间',
    PRIMARY KEY (`id`),
    INDEX `idx_problem_id` (`problem_id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_status` (`status`),
    INDEX `idx_create_time` (`create_time`),
    CONSTRAINT `fk_submission_problem` FOREIGN KEY (`problem_id`) REFERENCES `problem` (`id`),
    CONSTRAINT `fk_submission_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='提交记录表';
