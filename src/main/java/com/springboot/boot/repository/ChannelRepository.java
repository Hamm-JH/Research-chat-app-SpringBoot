package com.springboot.boot.repository;

import com.springboot.boot.model.Channel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChannelRepository extends JpaRepository<Channel, Long> {
    Channel findByName(String name);
}