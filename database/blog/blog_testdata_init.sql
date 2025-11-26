use `emiya-oj`;

# 密码均为123456
insert into user(id, username, password, nickname, email, phone, avatar, create_by, update_by)
values 
    (10080, 'xiaowang', '$2a$10$d.HySwYXfPV9atRubAMM6.FpXNgZ5BgOJOqhBUJThxKInyZ6zE/Jq', '小王', 'xiaowang@gmail.com', '12345678901', 'undefined', 1, 1),
    (10081, 'xiaoli', '$2a$10$d.HySwYXfPV9atRubAMM6.FpXNgZ5BgOJOqhBUJThxKInyZ6zE/Jq', '小李', 'xiaoli@gmail.com', '12345678901', 'undefined', 1, 1),
    (10082, 'xiaoming', '$2a$10$d.HySwYXfPV9atRubAMM6.FpXNgZ5BgOJOqhBUJThxKInyZ6zE/Jq', '小明', 'xiaoming@gmail.com', '12345678901', 'undefined', 1, 1),
    (10083, 'xiaohong', '$2a$10$d.HySwYXfPV9atRubAMM6.FpXNgZ5BgOJOqhBUJThxKInyZ6zE/Jq', '小红', 'xiaohong@gmail.com', '12345678901', 'undefined', 1, 1),
    (10084, 'xiaozhang', '$2a$10$d.HySwYXfPV9atRubAMM6.FpXNgZ5BgOJOqhBUJThxKInyZ6zE/Jq', '小张', 'xiaozhang@gmail.com', '12345678901', 'undefined', 1, 1);

insert into user_role(id, user_id, role_id, create_by)
values
    (11080, 10080, 1, 1),
    (11081, 10080, 2, 1),
    (11082, 10081, 2, 1),
    (11083, 10082, 2, 1),
    (10084, 10082, 1, 1),
    (11085, 10083, 2, 1);

insert into user_blog(user_id, username, nickname)
values
    (10080, 'xiaowang', '小王'),
    (10081, 'xiaoli', '小李'),
    (10082, 'xiaoming', '小明'),
    (10083, 'xiaohong', '小红'),
    (10084, 'xiaozhang', '小张');

insert into blog_tag
values 
    (20080, 'aglgorithm', '算法'),
    (20081, 'data-structure', '数据结构'),
    (20082, 'java', 'java'),
    (20083, 'python', 'python'),
    (20084, 'c++', 'c++'),
    (20085, 'database', '数据库');

insert into blog(id, user_id, title, content)
values 
    (15080, 10080, '标题-贪心算法', '内容-算法'),
    (15081, 10080, '标题-动态规划', '内容-算法'),
    (15082, 10081, '标题-计算机网络数据结构', '内容-数据结构'),
    (15083, 10082, '标题-java2d游戏引擎', '内容-java'),
    (15084, 10082, '标题-python人工智能API', '内容-python'),
    (15085, 10082, '标题-c++底层优化', '内容-c++'),
    (15086, 10084, '标题-数据库增删改查', '内容-数据库');

insert into blog_tag_association
values 
    # 贪心算法 - 算法、数据结构
    (30080, 15080, 20080),
    (30081, 15080, 20081),
    # 动态规划 - 算法、python
    (30082, 15081, 20081),
    (30083, 15081, 20083),
    # 计算机数据结构 - 算法、c++
    (30084, 15082, 20080),
    (30085, 15082, 20084),
    # java2d游戏引擎 - 算法、数据结构、java
    (30086, 15083, 20080),
    (30087, 15083, 20081),
    (30088, 15083, 20082),
    # python人工智能API - python
    (30089, 15084, 20083),
    # c++底层优化 - 算法、数据结构、c++
    (30090, 15085, 20080),
    (30091, 15085, 20081),
    (30092, 15085, 20084),
    # 数据库增删改查 - Java、数据库
    (30093, 15086, 20082),
    (30094, 15086, 20085);

insert into blog_star(id, user_id, blog_id) 
values 
    (35080, 10080, 15080),
    (35081, 10080, 15085),
    (35082, 10081, 15085),
    (35083, 10083, 15081),
    (35084, 10083, 15082),
    (35085, 10083, 15086);

insert into blog_comment(id, blog_id, user_id, content)
values 
    (40000, 15080, 10080, '评论内容-小王'),
    (40001, 15080, 10081, '评论内容-小李'),
    (40002, 15080, 10082, '评论内容-小明'),
    (40003, 15080, 10083, '评论内容-小红'),
    (40004, 15085, 10080, '评论内容-小王');
