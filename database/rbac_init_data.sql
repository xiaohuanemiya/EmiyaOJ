-- ==============================
-- EmiyaOJ RBAC权限管理系统初始化数据
-- ==============================

-- 初始化用户数据（密码为 123456，使用BCrypt加密）
INSERT INTO `user` (`id`, `username`, `password`, `nickname`, `email`, `status`, `create_by`) VALUES
(1, 'admin', '$2a$10$7JB720yubVSObGJ2kXEMCOWjLrSxX4fV8ZbLOkk0qeDLyZYMi4h2K', '系统管理员', 'admin@emiyaoj.com', 1, 1),
(2, 'user', '$2a$10$7JB720yubVSObGJ2kXEMCOWjLrSxX4fV8ZbLOkk0qeDLyZYMi4h2K', '普通用户', 'user@emiyaoj.com', 1, 1),
(3, 'judge', '$2a$10$7JB720yubVSObGJ2kXEMCOWjLrSxX4fV8ZbLOkk0qeDLyZYMi4h2K', '判题员', 'judge@emiyaoj.com', 1, 1);

-- 初始化角色数据
INSERT INTO `role` (`id`, `role_code`, `role_name`, `description`, `status`, `create_by`) VALUES
(1, 'SUPER_ADMIN', '超级管理员', '拥有系统所有权限', 1, 1),
(2, 'ADMIN', '管理员', '拥有系统管理权限', 1, 1),
(3, 'JUDGE', '判题员', '拥有判题相关权限', 1, 1),
(4, 'USER', '普通用户', '普通用户权限', 1, 1),
(5, 'CONTESTANT', '参赛者', '参加比赛的用户', 1, 1);

-- 初始化权限数据
INSERT INTO `permission` (`id`, `parent_id`, `permission_code`, `permission_name`, `permission_type`, `path`, `component`, `icon`, `sort_order`, `status`, `create_by`) VALUES
-- 系统管理模块
(1, 0, 'SYSTEM', '系统管理', 1, '/system', NULL, 'system', 1, 1, 1),
(2, 1, 'USER_MANAGE', '用户管理', 1, '/system/user', 'system/user/index', 'user', 1, 1, 1),
(3, 2, 'USER_LIST', '用户查询', 2, NULL, NULL, NULL, 1, 1, 1),
(4, 2, 'USER_ADD', '用户新增', 2, NULL, NULL, NULL, 2, 1, 1),
(5, 2, 'USER_EDIT', '用户修改', 2, NULL, NULL, NULL, 3, 1, 1),
(6, 2, 'USER_DELETE', '用户删除', 2, NULL, NULL, NULL, 4, 1, 1),
(7, 2, 'USER_RESET_PWD', '重置密码', 2, NULL, NULL, NULL, 5, 1, 1),

(8, 1, 'ROLE_MANAGE', '角色管理', 1, '/system/role', 'system/role/index', 'role', 2, 1, 1),
(9, 8, 'ROLE_LIST', '角色查询', 2, NULL, NULL, NULL, 1, 1, 1),
(10, 8, 'ROLE_ADD', '角色新增', 2, NULL, NULL, NULL, 2, 1, 1),
(11, 8, 'ROLE_EDIT', '角色修改', 2, NULL, NULL, NULL, 3, 1, 1),
(12, 8, 'ROLE_DELETE', '角色删除', 2, NULL, NULL, NULL, 4, 1, 1),
(13, 8, 'ROLE_ASSIGN', '分配权限', 2, NULL, NULL, NULL, 5, 1, 1),

(14, 1, 'PERMISSION_MANAGE', '权限管理', 1, '/system/permission', 'system/permission/index', 'permission', 3, 1, 1),
(15, 14, 'PERMISSION_LIST', '权限查询', 2, NULL, NULL, NULL, 1, 1, 1),
(16, 14, 'PERMISSION_ADD', '权限新增', 2, NULL, NULL, NULL, 2, 1, 1),
(17, 14, 'PERMISSION_EDIT', '权限修改', 2, NULL, NULL, NULL, 3, 1, 1),
(18, 14, 'PERMISSION_DELETE', '权限删除', 2, NULL, NULL, NULL, 4, 1, 1),

(19, 1, 'DEPT_MANAGE', '部门管理', 1, '/system/dept', 'system/dept/index', 'dept', 4, 1, 1),
(20, 19, 'DEPT_LIST', '部门查询', 2, NULL, NULL, NULL, 1, 1, 1),
(21, 19, 'DEPT_ADD', '部门新增', 2, NULL, NULL, NULL, 2, 1, 1),
(22, 19, 'DEPT_EDIT', '部门修改', 2, NULL, NULL, NULL, 3, 1, 1),
(23, 19, 'DEPT_DELETE', '部门删除', 2, NULL, NULL, NULL, 4, 1, 1),

-- OJ核心模块
(100, 0, 'OJ', '在线判题', 1, '/oj', NULL, 'oj', 2, 1, 1),
(101, 100, 'PROBLEM_MANAGE', '题目管理', 1, '/oj/problem', 'oj/problem/index', 'problem', 1, 1, 1),
(102, 101, 'PROBLEM_LIST', '题目查询', 2, NULL, NULL, NULL, 1, 1, 1),
(103, 101, 'PROBLEM_ADD', '题目新增', 2, NULL, NULL, NULL, 2, 1, 1),
(104, 101, 'PROBLEM_EDIT', '题目修改', 2, NULL, NULL, NULL, 3, 1, 1),
(105, 101, 'PROBLEM_DELETE', '题目删除', 2, NULL, NULL, NULL, 4, 1, 1),
(106, 101, 'PROBLEM_SUBMIT', '提交代码', 2, NULL, NULL, NULL, 5, 1, 1),

(107, 100, 'CONTEST_MANAGE', '比赛管理', 1, '/oj/contest', 'oj/contest/index', 'contest', 2, 1, 1),
(108, 107, 'CONTEST_LIST', '比赛查询', 2, NULL, NULL, NULL, 1, 1, 1),
(109, 107, 'CONTEST_ADD', '比赛新增', 2, NULL, NULL, NULL, 2, 1, 1),
(110, 107, 'CONTEST_EDIT', '比赛修改', 2, NULL, NULL, NULL, 3, 1, 1),
(111, 107, 'CONTEST_DELETE', '比赛删除', 2, NULL, NULL, NULL, 4, 1, 1),
(112, 107, 'CONTEST_JOIN', '参加比赛', 2, NULL, NULL, NULL, 5, 1, 1),

(113, 100, 'SUBMISSION_MANAGE', '提交管理', 1, '/oj/submission', 'oj/submission/index', 'submission', 3, 1, 1),
(114, 113, 'SUBMISSION_LIST', '提交查询', 2, NULL, NULL, NULL, 1, 1, 1),
(115, 113, 'SUBMISSION_VIEW', '查看提交', 2, NULL, NULL, NULL, 2, 1, 1),
(116, 113, 'SUBMISSION_REJUDGE', '重新判题', 2, NULL, NULL, NULL, 3, 1, 1),

(117, 100, 'JUDGE_MANAGE', '判题管理', 1, '/oj/judge', 'oj/judge/index', 'judge', 4, 1, 1),
(118, 117, 'JUDGE_QUEUE', '判题队列', 2, NULL, NULL, NULL, 1, 1, 1),
(119, 117, 'JUDGE_SERVER', '判题服务器', 2, NULL, NULL, NULL, 2, 1, 1),

-- 监控模块
(200, 0, 'MONITOR', '系统监控', 1, '/monitor', NULL, 'monitor', 3, 1, 1),
(201, 200, 'LOGIN_LOG', '登录日志', 1, '/monitor/loginlog', 'monitor/loginlog/index', 'loginlog', 1, 1, 1),
(202, 201, 'LOGIN_LOG_LIST', '登录日志查询', 2, NULL, NULL, NULL, 1, 1, 1),
(203, 201, 'LOGIN_LOG_DELETE', '登录日志删除', 2, NULL, NULL, NULL, 2, 1, 1),

(204, 200, 'OPERATION_LOG', '操作日志', 1, '/monitor/operlog', 'monitor/operlog/index', 'operlog', 2, 1, 1),
(205, 204, 'OPERATION_LOG_LIST', '操作日志查询', 2, NULL, NULL, NULL, 1, 1, 1),
(206, 204, 'OPERATION_LOG_DELETE', '操作日志删除', 2, NULL, NULL, NULL, 2, 1, 1),

-- API接口权限
(300, 0, 'API', 'API接口', 3, NULL, NULL, NULL, 4, 1, 1),
(301, 300, 'API_USER', '用户接口', 3, '/api/user/**', NULL, NULL, 1, 1, 1),
(302, 300, 'API_ROLE', '角色接口', 3, '/api/role/**', NULL, NULL, 2, 1, 1),
(303, 300, 'API_PERMISSION', '权限接口', 3, '/api/permission/**', NULL, NULL, 3, 1, 1),
(304, 300, 'API_PROBLEM', '题目接口', 3, '/api/problem/**', NULL, NULL, 4, 1, 1),
(305, 300, 'API_CONTEST', '比赛接口', 3, '/api/contest/**', NULL, NULL, 5, 1, 1),
(306, 300, 'API_SUBMISSION', '提交接口', 3, '/api/submission/**', NULL, NULL, 6, 1, 1),
(307, 300, 'API_JUDGE', '判题接口', 3, '/api/judge/**', NULL, NULL, 7, 1, 1);

-- 分配用户角色关系
INSERT INTO `user_role` (`user_id`, `role_id`, `create_by`) VALUES
(1, 1, 1), -- admin -> SUPER_ADMIN
(2, 4, 1), -- user -> USER
(3, 3, 1); -- judge -> JUDGE

-- 分配角色权限关系（超级管理员拥有所有权限）
INSERT INTO `role_permission` (`role_id`, `permission_id`, `create_by`)
SELECT 1, id, 1 FROM `permission` WHERE `status` = 1;

-- 管理员权限（除了超级管理员特有权限外的大部分权限）
INSERT INTO `role_permission` (`role_id`, `permission_id`, `create_by`) VALUES
-- 系统管理权限
(2, 1, 1), (2, 2, 1), (2, 3, 1), (2, 4, 1), (2, 5, 1), (2, 6, 1), (2, 7, 1),
(2, 8, 1), (2, 9, 1), (2, 10, 1), (2, 11, 1), (2, 12, 1), (2, 13, 1),
(2, 19, 1), (2, 20, 1), (2, 21, 1), (2, 22, 1), (2, 23, 1),
-- OJ管理权限
(2, 100, 1), (2, 101, 1), (2, 102, 1), (2, 103, 1), (2, 104, 1), (2, 105, 1),
(2, 107, 1), (2, 108, 1), (2, 109, 1), (2, 110, 1), (2, 111, 1),
(2, 113, 1), (2, 114, 1), (2, 115, 1), (2, 116, 1),
-- 监控权限
(2, 200, 1), (2, 201, 1), (2, 202, 1), (2, 203, 1), (2, 204, 1), (2, 205, 1), (2, 206, 1);

-- 判题员权限
INSERT INTO `role_permission` (`role_id`, `permission_id`, `create_by`) VALUES
-- OJ相关权限
(3, 100, 1), (3, 101, 1), (3, 102, 1), (3, 103, 1), (3, 104, 1),
(3, 113, 1), (3, 114, 1), (3, 115, 1), (3, 116, 1),
(3, 117, 1), (3, 118, 1), (3, 119, 1),
-- API权限
(3, 304, 1), (3, 306, 1), (3, 307, 1);

-- 普通用户权限
INSERT INTO `role_permission` (`role_id`, `permission_id`, `create_by`) VALUES
-- 基本OJ权限
(4, 100, 1), (4, 101, 1), (4, 102, 1), (4, 106, 1),
(4, 107, 1), (4, 108, 1), (4, 112, 1),
(4, 113, 1), (4, 114, 1), (4, 115, 1);

-- 参赛者权限（继承普通用户权限）
INSERT INTO `role_permission` (`role_id`, `permission_id`, `create_by`) VALUES
(5, 100, 1), (5, 101, 1), (5, 102, 1), (5, 106, 1),
(5, 107, 1), (5, 108, 1), (5, 112, 1),
(5, 113, 1), (5, 114, 1), (5, 115, 1);
