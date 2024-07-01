package com.springboot.boot.repository;

import com.springboot.boot.model.Channel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ChannelRepositoryTest {

    @Autowired
    private ChannelRepository channelRepository;

    @Test
    public void whenSaveChannel_thenFindByName() {
        Channel channel = new Channel("TestChannel");
        channelRepository.save(channel);

        Channel found = channelRepository.findByName("TestChannel");
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("TestChannel");
    }

}
