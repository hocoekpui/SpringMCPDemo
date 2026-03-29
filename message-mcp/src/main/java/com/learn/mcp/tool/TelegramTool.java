package com.learn.mcp.tool;

import com.learn.service.McpTool;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class TelegramTool implements McpTool {

    @Value("${spring.tg.token}")
    private String TOKEN;
    @Value("${spring.tg.chat-id}")
    private String CHAT_ID;

    @Resource
    private RestTemplate restTemplate;

    @Tool(description = "给群组发送邮件信息")
    public void sendMessage(String text) {
        Map<String, String> request = new HashMap<>();
        request.put("chat_id", CHAT_ID);
        request.put("text", text);
        request.put("parse_mode", "HTML");
        try {
            restTemplate.postForObject("https://api.telegram.org/bot" + TOKEN + "/sendMessage", request, String.class);
        } catch (Exception e) {
            log.warn("TelegramBotService#sendMessage exception", e);
        }
    }
}
