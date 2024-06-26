package com.springboot.boot.controller;

import com.springboot.boot.model.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @MessageMapping("/message")
    @SendTo("/topic/messages")
    public ChatMessage send(ChatMessage message) throws Exception {
        return new ChatMessage(message.getFrom(), message.getText());
    }
}
