use `emiya-oj`;

create table if not exists user_blog
(
    user_id     bigint      not null primary key,
    username    varchar(50) not null,
    nickname    varchar(50) not null,
    blog_count  int         not null default 0,
    star_count  int         not null default 0,
    create_time datetime    not null default CURRENT_TIMESTAMP
) engine = InnoDB
  default charset = utf8mb4
  collate = utf8mb4_general_ci;

create table if not exists blog
(
    id          bigint       not null auto_increment primary key,
    user_id     bigint       not null,
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

-- 博客插入后自动增加博客数量
DELIMITER $$
CREATE TRIGGER after_blog_insert
    AFTER INSERT
    ON blog
    FOR EACH ROW
BEGIN
    UPDATE user_blog
    SET blog_count = blog_count + 1
    WHERE user_id = NEW.user_id;
END$$
DELIMITER ;

-- 博客删除后自动减少博客数量
DELIMITER $$
CREATE TRIGGER after_blog_delete
    AFTER UPDATE
    ON blog
    FOR EACH ROW
BEGIN
    IF NEW.deleted = 1 AND OLD.deleted = 0 THEN
        UPDATE user_blog
        SET blog_count = GREATEST(blog_count - 1, 0)
        WHERE user_id = NEW.user_id;
    END IF;
END$$
DELIMITER ;

-- 收藏插入后自动增加收藏数量
DELIMITER $$
CREATE TRIGGER after_blog_star_insert
    AFTER INSERT
    ON blog_star
    FOR EACH ROW
BEGIN
    UPDATE user_blog
    SET star_count = star_count + 1
    WHERE user_id = NEW.user_id;
END$$
DELIMITER ;

-- 收藏删除后自动减少收藏数量
DELIMITER $$
CREATE TRIGGER after_blog_star_delete
    AFTER DELETE
    ON blog_star
    FOR EACH ROW
BEGIN
    UPDATE user_blog
    SET star_count = GREATEST(star_count - 1, 0)
    WHERE user_id = OLD.user_id;
END$$
DELIMITER ;

-- 创建触发器：当user表的username更新时，同步更新user_blog表
DELIMITER $$
CREATE TRIGGER after_user_username_update
    AFTER UPDATE
    ON user
    FOR EACH ROW
BEGIN
    -- 检查username是否发生变化且不为空
    IF NEW.username <> OLD.username AND NEW.username IS NOT NULL THEN
        UPDATE user_blog
        SET username = NEW.username
        WHERE user_id = NEW.id;
    END IF;
END$$
DELIMITER ;

-- 创建触发器：当user表的nickname更新时，同步更新user_blog表
DELIMITER $$
CREATE TRIGGER after_user_nickname_update
    AFTER UPDATE
    ON user
    FOR EACH ROW
BEGIN
    -- 检查nickname是否发生变化且不为空
    IF NEW.nickname <> OLD.nickname AND NEW.nickname IS NOT NULL THEN
        UPDATE user_blog
        SET nickname = NEW.nickname
        WHERE user_id = NEW.id;
    END IF;
END$$
DELIMITER ;
