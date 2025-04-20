package com.sideproject.musinsa_backend.Chatting.repository;

import com.sideproject.musinsa_backend.Chatting.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}
