package com.sideproject.musinsa_backend.Chatting.repository;

import com.sideproject.musinsa_backend.Chatting.domain.ChatParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, Long> {
}
