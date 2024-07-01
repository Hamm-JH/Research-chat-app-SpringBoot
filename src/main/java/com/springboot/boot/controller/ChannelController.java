package com.springboot.boot.controller;

import com.springboot.boot.dto.ChannelDTO;
import com.springboot.boot.dto.UserDTO;
import com.springboot.boot.model.Channel;
import com.springboot.boot.model.User;
import com.springboot.boot.repository.ChannelRepository;
import com.springboot.boot.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Optional;
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
    @Transactional
    public void joinChannel(User user) {
        Channel channel = channelRepository.findByName(user.getChannel().getName());
        if (channel != null) {
            try {
                user.setChannel(channel);
                userRepository.save(user);

                List<UserDTO> channelUsers = userRepository.findByChannelName(channel.getName())
                        .stream()
                        .map(u -> new UserDTO(u.getId(), u.getUsername()))
                        .collect(Collectors.toList());
                messagingTemplate.convertAndSend("/topic/users/" + channel.getName(), channelUsers);
            } catch (DataIntegrityViolationException e) {
                // 중복 사용자 이름 예외 처리
                messagingTemplate.convertAndSend("/topic/error/" + channel.getName(), "Username already exists");
            }
        }
    }

    @MessageMapping("/leaveChannel")
    @Transactional
    public void leaveChannel(User user) {
        User existingUser = userRepository.findById(user.getId()).orElse(null);
        if (existingUser != null) {
            Channel channel = existingUser.getChannel();
            userRepository.delete(existingUser);

            List<UserDTO> channelUsers = userRepository.findByChannelName(channel.getName())
                    .stream()
                    .map(u -> new UserDTO(u.getId(), u.getUsername()))
                    .collect(Collectors.toList());
            messagingTemplate.convertAndSend("/topic/users/" + channel.getName(), channelUsers);
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