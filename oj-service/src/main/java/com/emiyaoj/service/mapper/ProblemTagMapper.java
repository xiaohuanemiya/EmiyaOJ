package com.emiyaoj.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.emiyaoj.service.domain.pojo.ProblemTag;
import org.apache.ibatis.annotations.Mapper;

/**
 * 题目标签关联Mapper
 */
@Mapper
public interface ProblemTagMapper extends BaseMapper<ProblemTag> {
}
