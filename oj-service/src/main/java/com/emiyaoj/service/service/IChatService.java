package com.emiyaoj.service.service;

import com.emiyaoj.service.domain.dto.ChatRequestDTO;

/**
 * 聊天服务接口
 */
public interface IChatService {

    /**
     * 发送聊天消息
     *
     * @param requestDTO 聊天请求DTO
     * @return AI助手回复
     */
    String sendMessage(ChatRequestDTO requestDTO);
}

