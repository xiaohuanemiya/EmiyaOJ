package com.emiyaoj.stu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.emiyaoj.stu.domain.pojo.Submission;
import org.apache.ibatis.annotations.Mapper;

/**
 * 提交记录Mapper
 */
@Mapper
public interface SubmissionMapper extends BaseMapper<Submission> {
}

