package com.springboot.boot.controller;

import com.springboot.boot.dto.ChannelDTO;
import com.springboot.boot.model.Channel;
import com.springboot.boot.model.User;
import com.springboot.boot.repository.ChannelRepository;
import com.springboot.boot.repository.UserRepository;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ChannelController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;

    public ChannelController(SimpMessagingTemplate messagingTemplate,
                             ChannelRepository channelRepository,
                             UserRepository userRepository) {
        this.messagingTemplate = messagingTemplate;
        this.channelRepository = channelRepository;
        this.userRepository = userRepository;
    }

    @MessageMapping("/addChannel")
    public void addChannel(ChannelDTO channelDTO) {
        String trimmedName = channelDTO.getName().trim();
        if (!trimmedName.isEmpty()) {
            Channel channel = new Channel();
            channel.setName(trimmedName);
            channelRepository.save(channel);
            messagingTemplate.convertAndSend("/topic/channels", getChannelDTOs());
        }
    }

    @MessageMapping("/deleteChannel")
    public void deleteChannel(ChannelDTO channelDTO) {
        Channel existingChannel = channelRepository.findByName(channelDTO.getName());
        if (existingChannel != null) {
            channelRepository.delete(existingChannel);
            userRepository.deleteAll(userRepository.findByChannelName(channelDTO.getName()));
            messagingTemplate.convertAndSend("/topic/channels", getChannelDTOs());
        }
    }

    @MessageMapping("/joinChannel")
    public void joinChannel(User user) {
        Channel channel = channelRepository.findByName(user.getChannel().getName());
        if (channel != null) {
            user.setChannel(channel);
            userRepository.save(user);
            messagingTemplate.convertAndSend("/topic/users/" + channel.getName(), userRepository.findByChannelName(channel.getName()));
        }
    }

    @MessageMapping("/leaveChannel")
    public void leaveChannel(User user) {
        User existingUser = userRepository.findById(user.getId()).orElse(null);
        if (existingUser != null) {
            Channel channel = existingUser.getChannel();
            userRepository.delete(existingUser);
            messagingTemplate.convertAndSend("/topic/users/" + channel.getName(), userRepository.findByChannelName(channel.getName()));
        }
    }

    @GetMapping("/channels")
    @ResponseBody
    public List<ChannelDTO> getChannels() {
        return getChannelDTOs();
    }

    private List<ChannelDTO> getChannelDTOs() {
        return channelRepository.findAll().stream()
                .map(channel -> new ChannelDTO(channel.getId(), channel.getName()))
                .collect(Collectors.toList());
    }
}