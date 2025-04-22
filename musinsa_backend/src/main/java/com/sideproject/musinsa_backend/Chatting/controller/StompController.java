package com.sideproject.musinsa_backend.Chatting.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sideproject.musinsa_backend.Chatting.domain.ChatRoom;
import com.sideproject.musinsa_backend.Chatting.exception.ChatRoomNotFoundException;
import com.sideproject.musinsa_backend.Chatting.repository.ChatRoomRepository;
import com.sideproject.musinsa_backend.Chatting.domain.ChatRoomType;

import com.sideproject.musinsa_backend.Chatting.dto.ChatMessageReqDto;
import com.sideproject.musinsa_backend.Chatting.service.ChatService;
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
    private final ChatRoomRepository chatRoomRepository;

    @MessageMapping("/{roomId}")
    public void sendMessage(@DestinationVariable Long roomId, ChatMessageReqDto chatMessageReqDto)
    throws JsonProcessingException {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
            .orElseThrow(() -> new ChatRoomNotFoundException("채팅방이 존재하지 않습니다."));

        ChatRoomType roomType = chatRoom.getChatRoomType();

        if (roomType == ChatRoomType.PROREQ) {
            // TODO: handle product request message logic
        }

        chatService.saveMessage(roomId, chatMessageReqDto);

        messagingTemplate.convertAndSend("/topic/" + roomId, chatMessageReqDto);
    }
}
