use `emiya-oj`;

drop table if exists user_blog;

drop table if exists blog;

drop table if exists blog_tag;

drop table if exists blog_tag_association;

drop table if exists blog_star;

drop table if exists blog_comment;

drop table if exists blog_picture;

drop trigger after_blog_insert;
drop trigger after_blog_delete;
drop trigger after_blog_star_insert;
drop trigger after_blog_star_delete;
drop trigger after_user_username_update;
drop trigger after_user_nickname_update;

delete from user_role
where id in (11080, 11081, 11082, 11083, 11084, 11085);

delete from user
where id in (10080, 10081, 10082, 10083, 10084);
