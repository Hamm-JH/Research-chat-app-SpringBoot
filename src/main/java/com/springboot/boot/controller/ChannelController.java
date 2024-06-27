package com.springboot.boot.controller;

import com.springboot.boot.model.Channel;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ChannelController {

    private List<Channel> channels = new ArrayList<>();

    @MessageMapping("/addChannel")
    @SendTo("/topic/channels")
    public List<Channel> addChannel(Channel channel) {
        channels.add(channel);
        return channels;
    }

    @MessageMapping("/deleteChannel")
    @SendTo("/topic/channels")
    public List<Channel> deleteChannel(Channel channel) {
        channels.removeIf(c -> c.getName().equals(channel.getName()));
        return channels;
    }

    @GetMapping("/channels")
    @ResponseBody
    public List<Channel> getChannels() {
        return channels;
    }
}
