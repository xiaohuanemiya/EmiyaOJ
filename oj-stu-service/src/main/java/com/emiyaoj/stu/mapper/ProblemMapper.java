package com.emiyaoj.stu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.emiyaoj.stu.domain.pojo.Problem;
import org.apache.ibatis.annotations.Mapper;

/**
 * 题目Mapper
 */
@Mapper
public interface ProblemMapper extends BaseMapper<Problem> {
}

