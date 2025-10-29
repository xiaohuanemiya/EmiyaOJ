# 论坛系统开发

## 路由表

列表

| 路由                     | 请求方法 | 功能说明                                                     |
| ------------------------ | -------- | ------------------------------------------------------------ |
| `/blog/topic`            | GET      | 查全部，暂时不用                                             |
| `/blog`                  | POST     | 添加博客                                                     |
| `/blog/query`            | POST     | **分页**按**给定信息**查询                                   |
| `/blog/{tid}`            | GET      | 查某一博客基本信息                                           |
| `/blog/{tid}`            | DELETE   | 删除博客，仅对**管理员**和**话题创建者**开放权限             |
| `/blog/{tid}`            | PUT      | 编辑博客基础信息，权限管理同上                               |
| `/blog/{tid}/comments`   | GET      | 以**表单**的形式提交实现分页查询（抄的百度贴吧）             |
| `/blog/{tid}/comments`   | POST     | 发表评论                                                     |
| `/blog/{tid}/star`       | POST     | 收藏博客，需要**用户登录**                                   |
| `/blog/user/{uid}`       | GET      | 查询指定作者发起的所有博客，暂时不用                         |
| `/blog/user/{uid}/blogs` | POST     | **分页**按**给定信息**查询指定作者发起的博客                 |
| `/blog/user/{uid}/star`  | POST     | 分页查用户收藏博客（**暂时不支持**）                         |
| `/blog/comments/{cid}`   | GET      | 获取指定评论（**不支持**）                                   |
| `/blog/comments/{cid}`   | PUT      | 修改已发表评论（**不支持**）                                 |
| `/blog/comments/{cid}`   | DELETE   | 删除评论，仅对**管理员**和**话题创建者**开放权限；<br />可以考虑使用AI智能检测评论 |

树状图

```
/blog  # GET, POST
 |--/query  # 条件分页查
 |--/{tid}  # 博客操作
 |   |--/comments  # 博客查、评论
 |   |--/star  # 收藏博客，需要用户登录
 |--/user/{uid}  # 查用户
 |   |--/blogs  # 分页查用户发表博客
 |   |--/star  # 分页查用户收藏博客
 |--/comments/{cid}  # 评论操作，偏系统管理方向
```



## 数据库设计

```mysql
use `emiya-oj`;

create table if not exists user_blog
(
    user_id bigint not null primary key
) engine = InnoDB
  default charset = utf8mb4
  collate = utf8mb4_general_ci;

create table if not exists blog
(
    id          bigint       not null auto_increment primary key,
    user_id     bigint,
    title       varchar(255) not null,
    content     text         not null,
    create_time datetime     not null default CURRENT_TIMESTAMP,
    update_time datetime     not null default CURRENT_TIMESTAMP,
    deleted     tinyint      not null,
    index idx_user_id (user_id),
    index idx_update_time (update_time)
) engine = InnoDB
  default charset = utf8mb4
  collate = utf8mb4_general_ci;

create table if not exists blog_tag
(
    id     bigint       not null auto_increment primary key,
    tag    varchar(255) not null,
    `desc` varchar(255) not null
) engine = InnoDB
  default charset = utf8mb4
  collate = utf8mb4_general_ci;

create table if not exists blog_tag_association
(
    id      bigint not null auto_increment primary key,
    blog_id bigint not null,
    tag_id  bigint not null,
    unique key uk_blog_tag (blog_id, tag_id),
    index idx_blog_id (blog_id),
    index idx_tag_id (tag_id)
) engine = InnoDB
  default charset = utf8mb4
  collate = utf8mb4_general_ci;
```

### 引用

新增权限：

```
BLOG  # 博客菜单
BLOG.LIST  # 博客搜索
BLOG.ADD  # 发表帖子
BLOG.DELETE  # 删除帖子
BLOG.EDIT  # 编辑帖子
BLOG.STAR  # 收藏帖子
COMMENT.LIST  # 列出评论
COMMENT.ADD  # 发表评论
COMMENT.DELETE  # 删除评论
```

### user_blog

- `user_id`：外键+主键

其他的留着扩展

### blog

- `id`：主键id
- `user_id`：外键id（不加物理外键）
- `title`：标题
- `content`：内容
- `create_time`：首次创建时间
- `update_time`：最后更新时间
- `deleted`：删除标记

评论相关的留着扩展

### blog_tag

- `id`：主键
- `name`：标签
- `desc`：标签描述

### blog_tag_association

- `id`
- `blog_id`
- `tag_id`



## 技术问题

- 话题讨论夹带图片信息，不好以纯文字存到数据库

  解决方案：图片上传特殊服务器，以URL的形式存储。minio

