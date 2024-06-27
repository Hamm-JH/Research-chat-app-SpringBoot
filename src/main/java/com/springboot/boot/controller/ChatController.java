package com.springboot.boot.controller;

import com.springboot.boot.model.ChatMessage;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ChatController {

    // 채널별로 메시지를 처리하도록 변경하고, 채널 정보를 포함한 메시지를 전송합니다.
    private final SimpMessagingTemplate messagingTemplate;
    private List<ChatMessage> messages = new ArrayList<>();

    // SimpMessagingTemplate를 초기화 합니다.
    public ChatController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/message/{channel}")
    public void sendMessage(ChatMessage message, @DestinationVariable String channel) {
        if (!message.getText().trim().isEmpty()) {
            messagingTemplate.convertAndSend("/topic/messages/" + channel, message);
        }
    }
}