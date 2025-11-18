package com.emiyaoj.service.domain.constant;

/**
 * 判题状态枚举
 */
public enum JudgeStatus {
    PENDING("Pending", "等待判题"),
    JUDGING("Judging", "判题中"),
    ACCEPTED("Accepted", "通过"),
    WRONG_ANSWER("Wrong Answer", "答案错误"),
    TIME_LIMIT_EXCEEDED("Time Limit Exceeded", "时间超限"),
    MEMORY_LIMIT_EXCEEDED("Memory Limit Exceeded", "内存超限"),
    OUTPUT_LIMIT_EXCEEDED("Output Limit Exceeded", "输出超限"),
    RUNTIME_ERROR("Runtime Error", "运行时错误"),
    COMPILE_ERROR("Compile Error", "编译错误"),
    SYSTEM_ERROR("System Error", "系统错误");
    
    private final String value;
    private final String description;
    
    JudgeStatus(String value, String description) {
        this.value = value;
        this.description = description;
    }
    
    public String getValue() {
        return value;
    }
    
    public String getDescription() {
        return description;
    }
    
    public static JudgeStatus fromValue(String value) {
        for (JudgeStatus status : values()) {
            if (status.value.equals(value)) {
                return status;
            }
        }
        return null;
    }
}
