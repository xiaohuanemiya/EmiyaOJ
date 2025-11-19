-- ==============================
-- EmiyaOJ Online Judge System Initial Data
-- ==============================

-- 插入支持的编程语言
INSERT INTO `language` (`name`, `version`, `compile_command`, `execute_command`, `source_file_ext`, `executable_ext`, `is_compiled`, `time_limit_multiplier`, `memory_limit_multiplier`, `status`) VALUES
('C', 'gcc-14', 'gcc -O2 -Wall -std=c17 -o {executable} {source}', '{executable}', '.c', '', 1, 1.0, 1.0, 1),
('C++', 'g++-14', 'g++ -O2 -Wall -std=c++20 -o {executable} {source}', '{executable}', '.cpp', '', 1, 1.0, 1.0, 1);

-- 插入示例标签
INSERT INTO `tag` (`name`, `description`, `color`) VALUES
('数组', '数组相关问题', '#409EFF'),
('字符串', '字符串处理问题', '#67C23A'),
('动态规划', '动态规划算法', '#E6A23C'),
('贪心', '贪心算法', '#F56C6C'),
('深度优先搜索', 'DFS算法', '#909399'),
('广度优先搜索', 'BFS算法', '#409EFF'),
('二分查找', '二分查找算法', '#67C23A'),
('排序', '排序算法', '#E6A23C'),
('数学', '数学问题', '#F56C6C'),
('位运算', '位运算技巧', '#909399'),
('链表', '链表数据结构', '#409EFF'),
('树', '树结构问题', '#67C23A'),
('图', '图论算法', '#E6A23C'),
('哈希表', '哈希表应用', '#F56C6C'),
('栈', '栈数据结构', '#909399'),
('队列', '队列数据结构', '#409EFF');

-- 插入示例题目：A+B Problem
INSERT INTO `problem` (`title`, `description`, `input_description`, `output_description`, `sample_input`, `sample_output`, `hint`, `difficulty`, `time_limit`, `memory_limit`, `stack_limit`, `source`, `status`) VALUES
('A+B Problem', 
'给定两个整数A和B，计算A+B的值。

这是一道经典的入门题目，用于测试你的基本输入输出能力。',
'一行包含两个整数A和B，用空格分隔。(-10^9 <= A, B <= 10^9)',
'输出A+B的值。',
'1 2',
'3',
'这是最简单的题目，确保你能正确读取输入并输出结果。',
1, 1000, 128, 128, 'EmiyaOJ Example', 1);

-- 为A+B Problem插入测试用例
INSERT INTO `test_case` (`problem_id`, `input`, `output`, `is_sample`, `score`, `sort_order`) VALUES
(1, '1 2', '3', 1, 10, 1),
(1, '100 200', '300', 0, 10, 2),
(1, '-1 1', '0', 0, 10, 3),
(1, '0 0', '0', 0, 10, 4),
(1, '999999999 1', '1000000000', 0, 10, 5),
(1, '-999999999 -1', '-1000000000', 0, 10, 6),
(1, '123456789 987654321', '1111111110', 0, 10, 7),
(1, '-123456789 123456789', '0', 0, 10, 8),
(1, '500000000 500000000', '1000000000', 0, 10, 9),
(1, '-500000000 -500000000', '-1000000000', 0, 10, 10);

-- 为A+B Problem添加标签
INSERT INTO `problem_tag` (`problem_id`, `tag_id`) VALUES
(1, 9); -- 数学

-- 插入示例题目：Hello World
INSERT INTO `problem` (`title`, `description`, `input_description`, `output_description`, `sample_input`, `sample_output`, `hint`, `difficulty`, `time_limit`, `memory_limit`, `stack_limit`, `source`, `status`) VALUES
('Hello World', 
'输出 "Hello, World!" 到标准输出。

这是最简单的程序，用于验证你的编程环境是否正确配置。',
'无输入',
'输出一行：Hello, World!',
'',
'Hello, World!',
'注意输出的格式要完全一致，包括标点符号和大小写。',
1, 1000, 128, 128, 'EmiyaOJ Example', 1);

-- 为Hello World插入测试用例
INSERT INTO `test_case` (`problem_id`, `input`, `output`, `is_sample`, `score`, `sort_order`) VALUES
(2, '', 'Hello, World!', 1, 100, 1);

-- 为Hello World添加标签
INSERT INTO `problem_tag` (`problem_id`, `tag_id`) VALUES
(2, 2); -- 字符串
