package com.sideproject.musinsa_backend.Chatting.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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

        ChatMessageDto pubMessage = chatService.saveMessage(roomId, chatMessageDto);

        pubMessage.setRoomId(roomId);
         ObjectMapper objectMapper = new ObjectMapper();
         objectMapper.registerModule(new JavaTimeModule());
         objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

         String message = objectMapper.writeValueAsString(pubMessage);
//        System.out.println(message);
         redisPubSubService.publish("chat", message);
    }
}
