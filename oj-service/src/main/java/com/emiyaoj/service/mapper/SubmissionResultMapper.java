package com.emiyaoj.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.emiyaoj.service.domain.pojo.SubmissionResult;
import org.apache.ibatis.annotations.Mapper;

/**
 * 提交结果Mapper
 */
@Mapper
public interface SubmissionResultMapper extends BaseMapper<SubmissionResult> {
}
