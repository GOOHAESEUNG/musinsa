package com.sideproject.musinsa_backend.Chatting.controller;

import com.sideproject.musinsa_backend.Chatting.dto.ChetMessageReqDto;
import com.sideproject.musinsa_backend.Chatting.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class StompController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;

    @MessageMapping("/{roomId}")
    public void sendMessage(@PathVariable Long roomId,
                            ChetMessageReqDto chatMessageReqDto) {
        System.out.println(chatMessageReqDto.getMessage());
        chatService.saveMessage(roomId, chatMessageReqDto);
        chatMessageReqDto.setRoomId(roomId);

        messagingTemplate.convertAndSend("/topic/" + roomId, chatMessageReqDto);
    }
}
