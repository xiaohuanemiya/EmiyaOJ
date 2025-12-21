use `emiya-oj`;

delete
from blog_comment
where id in (40000, 40001, 40002, 40003, 40004);

delete
from blog_star
where id in (35080, 35081, 35082, 35083, 35084, 35085);

delete
from blog_tag_association
where id in (30080, 30081, 30082, 30083, 30084, 30085, 30086, 30087, 30088, 30089, 30090, 30091, 30092, 30093, 30094);

delete
from blog_tag
where id in (20080, 20081, 20082, 20083, 20084, 20085);

delete
from blog
where id in (15080, 15081, 15082, 15083, 15084, 15085, 15086);

delete
from user_blog
where user_id in (10080, 10081, 10082, 10083, 10084);

delete
from user
where id in (10080, 10081, 10082, 10083, 10084);
