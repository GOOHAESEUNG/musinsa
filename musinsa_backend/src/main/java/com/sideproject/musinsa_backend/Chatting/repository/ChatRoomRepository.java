package com.sideproject.musinsa_backend.Chatting.repository;

import com.sideproject.musinsa_backend.Chatting.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    List<ChatRoom> findByIsGroupChat(String isGroupChat);
}
