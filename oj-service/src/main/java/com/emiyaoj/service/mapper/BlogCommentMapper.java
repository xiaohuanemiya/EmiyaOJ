package com.emiyaoj.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.emiyaoj.service.domain.pojo.BlogComment;
import org.apache.ibatis.annotations.Mapper;

/**
 * <h1>博客评论DAO</h1>
 *
 * @author Erida
 * @since 2025/11/14
 */
@Mapper
public interface BlogCommentMapper extends BaseMapper<BlogComment> {
}
