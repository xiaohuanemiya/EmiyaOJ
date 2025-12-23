package com.emiyaoj.stu.controller;

import com.emiyaoj.common.domain.ResponseResult;
import com.emiyaoj.stu.domain.dto.ChatRequestDTO;
import com.emiyaoj.stu.service.IChatService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 聊天控制器
 */
@RestController
@RequestMapping("/client/chat")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin
public class ChatController {

    private final IChatService chatService;

    @PostMapping("/message")
    @Operation(summary = "发送聊天消息")
    public ResponseResult<String> sendMessage(@RequestBody ChatRequestDTO requestDTO) {
        log.info("收到聊天消息请求，problemId: {}, message: {}", requestDTO.getProblemId(), requestDTO.getMessage());
        try {
            String response = chatService.sendMessage(requestDTO);
            log.info("AI回复成功，回复长度: {}", response != null ? response.length() : 0);
            return ResponseResult.success(response);
        } catch (Exception e) {
            log.error("发送聊天消息失败", e);
            return ResponseResult.error("发送消息失败：" + e.getMessage());
        }
    }
}

