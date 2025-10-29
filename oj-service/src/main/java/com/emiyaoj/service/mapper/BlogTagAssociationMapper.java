package com.emiyaoj.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.emiyaoj.service.domain.pojo.BlogTagAssociation;
import org.apache.ibatis.annotations.Mapper;

/**
 * <h1>博客标签关联表 Mapper 接口</h1>
 *
 * @author Erida
 * @since 2025/10/29
 */
@Mapper
public interface BlogTagAssociationMapper extends BaseMapper<BlogTagAssociation> {
}
