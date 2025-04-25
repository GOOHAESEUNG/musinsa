package com.sideproject.musinsa_backend.Chatting.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sideproject.musinsa_backend.Chatting.domain.ChatRoom;
import com.sideproject.musinsa_backend.Chatting.dto.ChatMessageDto;
import com.sideproject.musinsa_backend.Chatting.dto.ChatMessageHisDto;
import com.sideproject.musinsa_backend.Chatting.exception.ChatRoomNotFoundException;
import com.sideproject.musinsa_backend.Chatting.repository.ChatRoomRepository;
import com.sideproject.musinsa_backend.Chatting.domain.ChatRoomType;

import com.sideproject.musinsa_backend.Chatting.dto.ChatMessageReqDto;
import com.sideproject.musinsa_backend.Chatting.service.ChatService;
import com.sideproject.musinsa_backend.Chatting.service.RedisPubSubService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class StompController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;
    private final RedisPubSubService redisPubSubService;


    @MessageMapping("/{roomId}")
    public void sendMessage(@DestinationVariable Long roomId, ChatMessageDto chatMessageDto)
    throws JsonProcessingException {

         chatService.saveMessage(roomId, chatMessageDto);
         chatMessageDto.setRoomId(roomId);

         ObjectMapper objectMapper = new ObjectMapper();
         String message = objectMapper.writeValueAsString(chatMessageDto);
         redisPubSubService.publish("chat", message);
    }
}
