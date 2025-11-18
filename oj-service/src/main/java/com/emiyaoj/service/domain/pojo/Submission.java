package com.emiyaoj.service.domain.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("submission")
public class Submission {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long problemId;
    private Long userId;
    private String language;
    private String code;
    private String status;
    private Long timeUsed;
    private Long memoryUsed;
    private String passRate;
    private String errorMessage;
    private String judgeResult;
    private LocalDateTime createTime;
    private LocalDateTime finishTime;
}
