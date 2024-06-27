package com.springboot.boot.controller;

import com.springboot.boot.model.Channel;
import com.springboot.boot.model.User;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ChannelController {

    private final SimpMessagingTemplate messagingTemplate;
    private List<Channel> channels = new ArrayList<>();
    private List<User> users = new ArrayList<>();

    public ChannelController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/addChannel")
    public void addChannel(Channel channel) {
        String trimmedName = channel.getName().trim();
        if (!trimmedName.isEmpty()) {
            channel.setName(trimmedName); // 공백을 제거한 이름으로 설정
            channels.add(channel);
            messagingTemplate.convertAndSend("/topic/channels", channels);
        }
    }

    @MessageMapping("/deleteChannel")
    public void deleteChannel(Channel channel) {
        channels.removeIf(c -> c.getName().equals(channel.getName()));
        users.removeIf(u -> u.getChannel().equals(channel.getName()));
        messagingTemplate.convertAndSend("/topic/channels", channels);
    }

    @MessageMapping("/joinChannel")
    public void joinChannel(User user) {
        users.add(user);
        messagingTemplate.convertAndSend("/topic/users/" + user.getChannel(), getUsersInChannel(user.getChannel()));
    }

    @MessageMapping("/leaveChannel")
    public void leaveChannel(User user) {
        users.removeIf(u -> u.getUsername().equals(user.getUsername()) && u.getChannel().equals(user.getChannel()));
        messagingTemplate.convertAndSend("/topic/users/" + user.getChannel(), getUsersInChannel(user.getChannel()));
    }

    @GetMapping("/channels")
    @ResponseBody
    public List<Channel> getChannels() {
        return channels;
    }

    private List<User> getUsersInChannel(String channel) {
        return users.stream()
                .filter(user -> user.getChannel().equals(channel))
                .collect(Collectors.toList());
    }
}
