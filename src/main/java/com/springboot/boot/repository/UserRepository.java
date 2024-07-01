package com.springboot.boot.repository;

import com.springboot.boot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByChannelName(String channelName);
}