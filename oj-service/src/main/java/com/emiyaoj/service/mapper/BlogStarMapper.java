package com.emiyaoj.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.emiyaoj.service.domain.pojo.BlogStar;
import org.apache.ibatis.annotations.Mapper;

/**
 * <h1>用户收藏博客DAO</h1>
 *
 * @author Erida
 * @since 2025/11/14
 */
@Mapper
public interface BlogStarMapper extends BaseMapper<BlogStar> {
}
