use `emiya-oj`;

create table if not exists user_blog
(
    user_id  bigint      not null primary key,
    username varchar(50) not null,
    nickname varchar(50) not null
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
    deleted     tinyint      not null default 0,
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

create table if not exists blog_star
(
    id          bigint   not null auto_increment primary key,
    user_id     bigint   not null,
    blog_id     bigint   not null,
    create_time datetime not null default CURRENT_TIMESTAMP,
    deleted     tinyint  not null default 0,
    unique key uk_user_blog (user_id, blog_id)
) engine = InnoDB
  default charset = utf8mb4
  collate = utf8mb4_general_ci;

create table if not exists blog_comment
(
    id          bigint   not null auto_increment primary key,
    blog_id     bigint   not null,
    user_id     bigint   not null,
    content     text     not null,
    create_time datetime not null default CURRENT_TIMESTAMP,
    update_time datetime not null default CURRENT_TIMESTAMP,
    deleted     tinyint  not null default 0,
    index idx_blog_id (blog_id),
    index idx_user_id (user_id)
) engine = InnoDB
  default charset = utf8mb4
  collate = utf8mb4_general_ci;

create table if not exists blog_picture
(
    url     varchar(255) not null primary key,
    deleted tinyint      not null default 0
) engine = InnoDB
  default charset = utf8mb4
  collate = utf8mb4_general_ci;
