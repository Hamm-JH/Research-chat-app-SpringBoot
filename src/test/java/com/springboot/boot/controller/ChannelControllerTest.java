package com.springboot.boot.controller;

import com.springboot.boot.model.Channel;
import com.springboot.boot.model.User;
import com.springboot.boot.repository.ChannelRepository;
import com.springboot.boot.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

public class ChannelControllerTest {

    @InjectMocks
    private ChannelController channelController;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @Mock
    private ChannelRepository channelRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testJoinChannel() {
        // 테스트 데이터 준비
        Channel channel = new Channel();
        channel.setName("TestChannel");
        User user = new User();
        user.setUsername("TestUser");
        user.setChannel(channel);

        // Mock 동작 설정
        when(channelRepository.findByName("TestChannel")).thenReturn(channel);
        when(userRepository.findByChannelName("TestChannel")).thenReturn(Arrays.asList(user));

        // 메서드 실행
        channelController.joinChannel(user);

        // 검증
        /*
            올바른 채널 이름으로 채널을 조회하는가?
            atLeastOnce(): 메서드가 최소 한 번 이상 호출되었는지 확인
            channel.getName(): 테스트 채널명이 조회되는지 확인
            | channel.getName 대신에 사용할만한 것들
            + anyString() : 어떤 문자열이든 매칭
            + any(), any(class)
         */
        verify(channelRepository, atLeastOnce()).findByName(channel.getName());
        /*
            사용자를 저장하는가?
         */
        verify(userRepository).save(user);  // 사용자를 저장하는가
        /*
            채널에 속한 사용자 목록을 WebSocket을 통해 전송하는지
            eq(): 정확한 토픽 문자열 지정
            any(List.class): 어떤 List 객체든 매칭되도록 함
         */
        verify(messagingTemplate).convertAndSend(eq("/topic/users/" + channel.getName()), any(List.class)); // 채널에 속한 사용자 목록을 Websocket을 통해 전송하는가?

        /*
            사용자를 두 번 저장하는가?
         */
        verify(userRepository).save(user);  // 사용자를 저장하는가
    }
}
