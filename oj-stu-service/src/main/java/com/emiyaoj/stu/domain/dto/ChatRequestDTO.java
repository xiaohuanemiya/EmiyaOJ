package com.emiyaoj.stu.domain.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 聊天请求DTO
 */
@Data
public class ChatRequestDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 题目ID（可选）
     */
    private Long problemId;

    /**
     * 用户消息
     */
    private String message;

    /**
     * 历史对话记录
     */
    private List<ChatMessageDTO> history;
}

