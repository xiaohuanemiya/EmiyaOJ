package com.emiyaoj.stu.service.apiservice;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.emiyaoj.stu.domain.dto.ChatMessageDTO;
import com.emiyaoj.stu.domain.dto.ChatRequestDTO;
import com.emiyaoj.stu.service.IChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * 聊天服务实现（调用阿里云百炼API）
 */
@Service
@Slf4j
public class ChatServiceImpl implements IChatService {

    private static final String API_KEY = "sk-492762b5f2434bf59cfcbe80ce6612f5";
    private static final String API_URL = "https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation";
    private static final String MODEL = "qwen-turbo";
    
    private static final String SYSTEM_PROMPT = "你是一个OJ编程助手。你的职责是帮助学生解决编程问题，但需要遵循以下原则：\n" +
            "1. 不能直接回答题目的原则性问题（比如不能直接给出题目的完整解法或答案）\n" +
            "2. 不能直接解决代码报错（不能直接修复代码）\n" +
            "3. 可以提供小的提示，比如：\n" +
            "   - 推荐使用的库或函数\n" +
            "   - 算法思路的提示\n" +
            "   - 代码结构的建议\n" +
            "   - 常见问题的排查方向\n" +
            "4. 鼓励学生独立思考，引导他们自己解决问题\n" +
            "5. 用友好、耐心的语气与学生交流";

    private final RestTemplate restTemplate;

    public ChatServiceImpl() {
        this.restTemplate = new RestTemplate();
    }

    @Override
    public String sendMessage(ChatRequestDTO requestDTO) {
        try {
            // 构建消息列表
            List<JSONObject> messages = new ArrayList<>();
            
            // 添加系统提示词
            JSONObject systemMessage = new JSONObject();
            systemMessage.put("role", "system");
            systemMessage.put("content", SYSTEM_PROMPT);
            messages.add(systemMessage);
            
            // 添加历史对话
            if (requestDTO.getHistory() != null && !requestDTO.getHistory().isEmpty()) {
                for (ChatMessageDTO historyMsg : requestDTO.getHistory()) {
                    JSONObject msg = new JSONObject();
                    msg.put("role", historyMsg.getRole());
                    msg.put("content", historyMsg.getContent());
                    messages.add(msg);
                }
            }
            
            // 添加当前用户消息
            JSONObject userMessage = new JSONObject();
            userMessage.put("role", "user");
            userMessage.put("content", requestDTO.getMessage());
            messages.add(userMessage);

            // 构建请求体
            JSONObject requestBody = new JSONObject();
            requestBody.put("model", MODEL);
            requestBody.put("input", new JSONObject());
            requestBody.getJSONObject("input").put("messages", messages);
            requestBody.put("parameters", new JSONObject());
            requestBody.getJSONObject("parameters").put("temperature", 0.7);
            requestBody.getJSONObject("parameters").put("max_tokens", 2000);

            // 设置请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + API_KEY);
            headers.set("X-DashScope-SSE", "disable");

            HttpEntity<String> entity = new HttpEntity<>(requestBody.toJSONString(), headers);

            // 发送请求
            ResponseEntity<String> response = restTemplate.exchange(
                    API_URL,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            // 解析响应
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                JSONObject responseJson = JSON.parseObject(response.getBody());
                JSONObject output = responseJson.getJSONObject("output");
                if (output != null) {
                    JSONArray choices = output.getJSONArray("choices");
                    if (choices != null && !choices.isEmpty()) {
                        JSONObject choice = choices.getJSONObject(0);
                        JSONObject message = choice.getJSONObject("message");
                        if (message != null) {
                            String content = message.getString("content");
                            log.info("AI回复成功，内容长度: {}", content != null ? content.length() : 0);
                            return content != null ? content : "抱歉，我没有理解您的问题。";
                        }
                    }
                }
                log.warn("响应格式异常: {}", response.getBody());
                return "抱歉，AI助手暂时无法回复，请稍后再试。";
            } else {
                log.error("API请求失败，状态码: {}, 响应: {}", response.getStatusCode(), response.getBody());
                return "抱歉，AI助手暂时无法回复，请稍后再试。";
            }
        } catch (Exception e) {
            log.error("调用AI API异常", e);
            return "抱歉，AI助手暂时无法回复，请稍后再试。错误信息：" + e.getMessage();
        }
    }
}

