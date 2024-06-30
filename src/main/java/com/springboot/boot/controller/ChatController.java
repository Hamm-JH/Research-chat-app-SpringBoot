package com.springboot.boot.controller;

import com.springboot.boot.dto.ChatMessageDTO;
import com.springboot.boot.model.Channel;
import com.springboot.boot.model.ChatMessage;
import com.springboot.boot.repository.ChannelRepository;
import com.springboot.boot.repository.ChatMessageRepository;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageRepository chatMessageRepository;
    private final ChannelRepository channelRepository;

    public ChatController(SimpMessagingTemplate messagingTemplate,
                          ChatMessageRepository chatMessageRepository,
                          ChannelRepository channelRepository) {
        this.messagingTemplate = messagingTemplate;
        this.chatMessageRepository = chatMessageRepository;
        this.channelRepository = channelRepository;
    }

    @MessageMapping("/message/{channel}")
    public void sendMessage(ChatMessageDTO messageDTO, @DestinationVariable String channel) {
        if (!messageDTO.getText().trim().isEmpty()) {
            Channel channelEntity = channelRepository.findByName(channel);
            if (channelEntity != null) {
                ChatMessage message = new ChatMessage();
                message.setFrom(messageDTO.getFrom());
                message.setText(messageDTO.getText());
                message.setChannel(channelEntity);
                message.setTimestamp(LocalDateTime.now());
                chatMessageRepository.save(message);

                // 저장된 메시지의 ID와 타임스탬프를 DTO에 설정
                messageDTO.setId(message.getId());
                messageDTO.setTimestamp(message.getTimestamp());
                messageDTO.setChannelName(channel);

                messagingTemplate.convertAndSend("/topic/messages/" + channel, messageDTO);
            }
        }
    }

    @MessageMapping("/history/{channel}")
    public void sendChatHistory(@DestinationVariable String channel) {
        List<ChatMessageDTO> messages = chatMessageRepository.findByChannelNameOrderByTimestampAsc(channel)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        messagingTemplate.convertAndSend("/topic/history/" + channel, messages);
    }

    private ChatMessageDTO convertToDTO(ChatMessage message) {
        ChatMessageDTO dto = new ChatMessageDTO();
        dto.setId(message.getId());
        dto.setFrom(message.getFrom());
        dto.setText(message.getText());
        dto.setChannelName(message.getChannel().getName());
        dto.setTimestamp(message.getTimestamp());
        return dto;
    }
}