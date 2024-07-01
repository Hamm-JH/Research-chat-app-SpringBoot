package com.springboot.boot.repository;

import com.springboot.boot.model.Channel;
import com.springboot.boot.model.ChatMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ChatMessageRepositoryTest {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private ChannelRepository channelRepository;

    @Test
    public void whenFindByChannelName_thenReturnChatMessages() {
        Channel channel = new Channel("TestChannel");
        channelRepository.save(channel);

        ChatMessage message1 = new ChatMessage("User1", "Hello", channel);
        ChatMessage message2 = new ChatMessage("User2", "Hi", channel);
        chatMessageRepository.save(message1);
        chatMessageRepository.save(message2);

        List<ChatMessage> found = chatMessageRepository.findByChannelNameOrderByTimestampAsc("TestChannel");
        assertThat(found).hasSize(2);
        assertThat(found.get(0).getText()).isEqualTo("Hello");
        assertThat(found.get(1).getText()).isEqualTo("Hi");
    }
}
