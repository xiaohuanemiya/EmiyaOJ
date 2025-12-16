package com.emiyaoj.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.emiyaoj.service.domain.pojo.Problem;
import org.apache.ibatis.annotations.Mapper;

/**
 * 题目Mapper
 */
@Mapper
public interface ProblemMapper extends BaseMapper<Problem> {
}

