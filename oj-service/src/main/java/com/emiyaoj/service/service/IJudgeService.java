package com.emiyaoj.service.service;

import com.emiyaoj.service.domain.pojo.Submission;

public interface IJudgeService {
    /**
     * 判题
     * @param submission 提交记录
     */
    void judge(Submission submission);
}
