package com.sideproject.musinsa_backend.Chatting.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sideproject.musinsa_backend.Chatting.dto.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Slf4j
@RequiredArgsConstructor
@Component
public class RedisSubscriber implements MessageListener {

    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String json = new String(message.getBody(), StandardCharsets.UTF_8);
            ChatMessageDto chatMessageDto = objectMapper.readValue(json, ChatMessageDto.class);

            // 🔥 메시지를 STOMP 구독자에게 뿌려줌
            messagingTemplate.convertAndSend(
                    "/topic/" + chatMessageDto.getRoomId(),
                    chatMessageDto
            );
        } catch (Exception e) {
            log.error("RedisSubscriber 오류 발생", e);
        }
    }
}