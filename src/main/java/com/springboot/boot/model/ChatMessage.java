package com.springboot.boot.model;

public class ChatMessage {
    private String from;
    private String text;
    private String channel;

    public ChatMessage() {
    }

    public ChatMessage(String from, String text, String channel) {
        this.from = from;
        this.text = text;
        this.channel = channel;
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

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}