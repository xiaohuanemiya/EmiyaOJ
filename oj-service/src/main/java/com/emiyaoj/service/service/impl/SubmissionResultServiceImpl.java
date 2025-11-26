package com.emiyaoj.service.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.emiyaoj.service.domain.pojo.SubmissionResult;
import com.emiyaoj.service.mapper.SubmissionResultMapper;
import com.emiyaoj.service.service.ISubmissionResultService;
import org.springframework.stereotype.Service;

/**
 * 提交结果Service实现
 */
@Service
public class SubmissionResultServiceImpl extends ServiceImpl<SubmissionResultMapper, SubmissionResult> implements ISubmissionResultService {
}
