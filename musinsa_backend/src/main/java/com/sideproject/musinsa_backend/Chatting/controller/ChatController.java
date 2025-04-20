package com.sideproject.musinsa_backend.Chatting.controller;

import com.sideproject.musinsa_backend.Chatting.service.ChatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public final class ChatController {

    private final ChatService chatService;

    @GetMapping("/rooms/my")
    public ResponseEntity<List<ChatRoomResDto>> getMyChatRooms() {
        List<ChatRoomResDto> chatRooms = chatService.getMyChatRooms();
        return ResponseEntity.ok(chatRooms);
    }

    @PostMapping("/rooms/group")
    public ResponseEntity<Long> createGroupChatRoom(@RequestBody CreateGroupRoomReqDto request) {
        Long roomId = chatService.createGroupRoom(request.getRoomName(), request.getChatRoomType(), request.getFloor());
        return ResponseEntity.status(HttpStatus.CREATED).body(roomId);
    }
}
