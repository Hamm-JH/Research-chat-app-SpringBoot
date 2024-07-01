package com.springboot.boot.dto;

import java.time.LocalDateTime;

public class ChatMessageDTO {
    private Long id;
    private String from;
    private String text;
    private String channelName;
    private LocalDateTime timestamp;

    // Constructors
    public ChatMessageDTO() {}

    public ChatMessageDTO(Long id, String from, String text, String channelName, LocalDateTime timestamp) {
        this.id = id;
        this.from = from;
        this.text = text;
        this.channelName = channelName;
        this.timestamp = timestamp;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}