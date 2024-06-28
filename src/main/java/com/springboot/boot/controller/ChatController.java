package com.springboot.boot.controller;

import com.springboot.boot.model.ChatMessage;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ChatController {

    // 채널별로 메시지를 처리하도록 변경하고, 채널 정보를 포함한 메시지를 전송합니다.
    private final SimpMessagingTemplate messagingTemplate;
    private final Map<String, List<ChatMessage>> chatHistory = new HashMap<>();

    // SimpMessagingTemplate를 초기화 합니다.
    public ChatController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/message/{channel}")
    public void sendMessage(ChatMessage message, @DestinationVariable String channel) {
        if (!message.getText().trim().isEmpty()) {
            message.setChannel(channel);
            saveMessage(message);
            messagingTemplate.convertAndSend("/topic/messages/" + channel, message); 
        }
    }

    @MessageMapping("/history/{channel}")
    public void sendChatHistory(@DestinationVariable String channel) {
        List<ChatMessage> messages = chatHistory.getOrDefault(channel, new ArrayList<>());
        messagingTemplate.convertAndSend("/topic/history/" + channel, messages);
    }

    private void saveMessage(ChatMessage message) {
        chatHistory.computeIfAbsent(message.getChannel(), k -> new ArrayList<>()).add(message);
    }
}