package com.springboot.boot.repository;

import com.springboot.boot.model.Channel;
import com.springboot.boot.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChannelRepository channelRepository;

    @Test
    public void whenFindByChannelName_thenReturnUsers() {
        Channel channel = new Channel("TestChannel");
        channelRepository.save(channel);

        User user1 = new User("User1", channel);
        User user2 = new User("User2", channel);
        userRepository.save(user1);
        userRepository.save(user2);

        List<User> found = userRepository.findByChannelName("TestChannel");
        assertThat(found).hasSize(2);
        assertThat(found.get(0).getUsername()).isEqualTo("User1");
        assertThat(found.get(1).getUsername()).isEqualTo("User2");
    }
}
