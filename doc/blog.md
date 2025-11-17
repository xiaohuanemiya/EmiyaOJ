# 论坛系统开发

## 路由表

列表

| 路由                     | 请求方法 | 功能说明                                                     |
| ------------------------ | -------- | ------------------------------------------------------------ |
| `/blog`                  | GET      | 查全部，暂时不用                                             |
| `/blog`                  | POST     | 添加博客                                                     |
| `/blog/query`            | POST     | **分页**按**给定信息**查询                                   |
| `/blog/{bid}`            | GET      | 查某一博客基本信息                                           |
| `/blog/{bid}`            | DELETE   | 删除博客，仅对**管理员**和**话题创建者**开放权限             |
| `/blog/{bid}`            | PUT      | 编辑博客基础信息，权限管理同上                               |
| `/blog/{bid}/comments`   | GET      | 以**表单**的形式提交实现分页查询（抄的百度贴吧）             |
| `/blog/{bid}/comments`   | POST     | 发表评论                                                     |
| `/blog/{bid}/star`       | POST     | 收藏博客，需要**用户登录**                                   |
| `/blog/user/{uid}`       | GET      | 查询指定作者发起的所有博客，暂时不用                         |
| `/blog/user/{uid}/blogs` | POST     | **分页**按**给定信息**查询指定作者发起的博客                 |
| `/blog/user/{uid}/star`  | POST     | 分页查用户收藏博客（**暂时不支持**）                         |
| `/blog/tags`             | GET      | 获取所有标签。因为由管理员规定，所以暂时不考虑分页           |
| `/blog/comments`         | POST     | 条件查所有评论（**不支持**）                                 |
| `/blog/comments/{cid}`   | GET      | 获取指定评论（**不支持**）                                   |
| `/blog/comments/{cid}`   | PUT      | 修改已发表评论（**不支持**）                                 |
| `/blog/comments/{cid}`   | DELETE   | 删除评论，仅对**管理员**和**话题创建者**开放权限；<br />可以考虑使用AI智能检测评论 |

树状图

```
/blog  # GET, POST
 |--/query  # 条件分页查
 |--/{bid}  # 博客操作
 |   |--/comments  # 博客查、评论
 |   |--/star  # 收藏博客，需要用户登录
 |--/user/{uid}  # 查用户
 |   |--/blogs  # 分页查用户发表博客
 |   |--/star  # 分页查用户收藏博客
 |--/tags  # 获取所有标签
 |--/comments  # 条件查评论，给管理员开放的接口
     |--/{cid}  # 评论操作，偏系统管理方向
```



## 数据库设计

### 关系设计

根据已有需求设计出以下实体

- A：原数据库用户
- B：博客模块用户信息
- C：博客标签
- D：发布的博客
- E：收藏的博客
- F：评论

关系如下（经过简化）：

```
A -> B  1:1
B -> D  1:n
B -> E  n:m
B -> F  1:n
C -> D  n:m
```



### 数据库初始化代码

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

引用角色：

```
admin  # 系统管理员
manager  # 管理员
user  # 用户
```



### user_blog

- `user_id`：主键
- `blog_count`：发布博客数量
- `star_count`：收藏博客数量
- `create_time`：博客模块用户初始化时间点

其他的留着扩展。每个博客模块用户仅在创建时检查user表中是否存在对应用户

### blog

- `id`：主键id
- `user_id`：外键id（不加物理外键）
- `title`：标题
- `content`：内容（存储html内容）
- `create_time`：首次创建时间
- `update_time`：最后更新时间
- `deleted`：删除标记（删除时需要修改`blog_tag_association`和`user_blog`）

评论相关的留着扩展。数据库清理已删除内容时一般从此表开始下手

### blog_tag

- `id`：主键
- `name`：标签
- `desc`：标签描述

### blog_tag_association

- `id`：主键
- `blog_id`：关联博客id
- `tag_id`：关联标签id
- `create_time`：关联时间

需要防止`(blog_id, tag_id)`重复

### blog_star

- `id`：主键
- `user_id`：用户id
- `blog_id`：收藏博客id
- `create_time`：收藏时间
- `deleted`：当博客删除时需要标记此状态方便前端显示。博客或用户不存在时标记，当用户存在且主动取消收藏时，**直接删除记录**

### blog_comment

- `id`：主键
- `blog_id`：被评论的博客
- `user_id`：评论用户
- `content`：评论内容
- `create_time`：评论时间
- `update_time`：评论修改时间（一般来说不太需要，至少前端不需要知道，这是留给后端管理员操作的）
- `deleted`：是否删除评论

### blog_picture

- `url`：主键，传给前端时可以包装成html标签
- `deleted`：被动标记。当博客删除时标记此状态，以便后续删除图片

如果需要主动部署服务器来存储用户上传的图片，需要用此数据表作为对照以方便删除过时的图片（仅记录url是否删除，不存储实际图片）



### 注意事项

需要删除时，先调用以下sql代码

```mysql

```

然后根据`blog_picture`表中`deleted`被标记的内容删除托管的图片

最后清理`blog_picture`不需要的内容

```mysql
using emiya-oj;
delete from blog where deleted=1;
```





## 技术问题

- 话题讨论夹带图片信息，不好以纯文字存到数据库

  解决方案：图片上传特殊服务器，以URL的形式存储。minio
  
  具体形式：前端先上传文件，后端返回url，前端显示出html表示的图片。之后评论完成后后端以html的形式存储
  
- 评论时考虑markdown渲染（？）

