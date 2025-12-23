package com.emiyaoj.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.emiyaoj.service.domain.pojo.Tag;
import org.apache.ibatis.annotations.Mapper;

/**
 * 标签Mapper
 */
@Mapper
public interface TagMapper extends BaseMapper<Tag> {
}
