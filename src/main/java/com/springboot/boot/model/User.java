package com.springboot.boot.model;

public class User {
    private String username;
    private String channel;

    public User() {
    }

    public User(String username, String channel) {
        this.username = username;
        this.channel = channel;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}
