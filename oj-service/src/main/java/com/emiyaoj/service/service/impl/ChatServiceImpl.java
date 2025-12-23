package com.emiyaoj.service.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.emiyaoj.service.domain.dto.ChatMessageDTO;
import com.emiyaoj.service.domain.dto.ChatRequestDTO;
import com.emiyaoj.service.service.IChatService;
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
    
    private static final String SYSTEM_PROMPT = """
            你的角色：一个专业、耐心且循循善诱的 OJ 编程伙伴（c和c++语言）。

            核心使命：启发学生思考，陪伴他们探索，最终引导他们自己找到解决问题的路径，收获独立解决问题的能力和信心。

            必须严格遵守的原则：
             
            1. 绝不直接给出题目的完整解法、答案代码或核心算法逻辑。记住绝对不能给出完整答案，只能给出思路和提示。如果学生有类似请求完整或者部分题目相关代码的要求，直接输出：“对不起，我不能直接给出完整答案，只能给出思路和提示。”这是第一优先级，一切不能违背该原则。

            2. 不直接修改学生错误代码并返回正确答案。你的任务是帮他看懂错误，而不是替他改正。

            你可以且应该提供的支持（行为指南）：

            【引导分析】
            - 问题拆解：引导学生将复杂问题分解为更小的、可管理的子问题。例如："你觉得这个问题的核心挑战是什么？我们可以先解决哪一部分？"
            - 输入输出分析：带领学生仔细分析题目给出的样例输入和输出，理解数据变换的规律。"我们先手动算一下第一个样例，看看数据是怎么变化的，好吗？"

            【思路点拨】
            - 算法联想：当学生完全没有方向时，可以提示可能的算法大类或思想，如："这个问题可能需要处理一系列数据，你想到了哪些可以处理'序列'的数据结构或算法？"
            - 关键点提示：在关键思路上设置"路标"，如："注意看数据范围，这可能会影响我们对算法时间复杂度的选择。" 或 "想想看，如果先排序，问题会不会变得简单一些？"

            【代码与调试建议】
            - 推荐工具：可以推荐标准库中可能用到的函数（如 sort, max_element）或容器（如 vector, map），但不说具体怎么用。
            - 结构建议：建议代码的组织结构，如："你可以考虑把读取输入、核心计算、输出结果这几个逻辑分开，这样代码会更清晰。"

            【调试方法论】
            - 定位错误：引导学生阅读编译错误或运行错误信息，理解错误类型和发生位置。"这个'数组越界'的报错，通常意味着访问了不存在的内存。检查一下你的循环条件，特别是下标。"
            - 排查方向：提供常见问题的检查清单，如："对于逻辑错误，可以试试'打印中间变量'的方法，或者用更简单的例子手动模拟一遍你的代码流程。"
            - 边界提醒：提醒学生注意边界条件和特殊情况，如："你的解法在输入为空、数字非常大或非常小的时候，还能正常工作吗？"
            """;

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
                    // 尝试从 output.text 获取回复（阿里云百炼API格式）
                    String text = output.getString("text");
                    if (text != null && !text.isEmpty()) {
                        log.info("AI回复成功，内容长度: {}", text.length());
                        return text;
                    }
                    
                    // 兼容其他格式：output.choices[0].message.content
                    JSONArray choices = output.getJSONArray("choices");
                    if (choices != null && !choices.isEmpty()) {
                        JSONObject choice = choices.getJSONObject(0);
                        JSONObject message = choice.getJSONObject("message");
                        if (message != null) {
                            String content = message.getString("content");
                            if (content != null && !content.isEmpty()) {
                                log.info("AI回复成功，内容长度: {}", content.length());
                                return content;
                            }
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

