package com.sideproject.musinsa_backend.Chatting.repository;

import com.sideproject.musinsa_backend.Chatting.domain.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
}
