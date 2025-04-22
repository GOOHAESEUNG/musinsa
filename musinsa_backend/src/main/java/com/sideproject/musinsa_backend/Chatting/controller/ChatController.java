package com.sideproject.musinsa_backend.Chatting.controller;

import com.sideproject.musinsa_backend.Chatting.dto.ChatRoomResDto;
import com.sideproject.musinsa_backend.Chatting.dto.CreateGroupRoomReqDto;
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

    //채팅방 목록
    @GetMapping("/rooms/my")
    public ResponseEntity<List<ChatRoomResDto>> getMyChatRooms() {
        List<ChatRoomResDto> chatRooms = chatService.getMyGroupChatRooms();
        return ResponseEntity.ok(chatRooms);
    }

    //채팅방 만들기
    @PostMapping("/rooms/group")
    public ResponseEntity<Long> createGroupChatRoom(@RequestBody CreateGroupRoomReqDto request) {
        Long roomId = chatService.createGroupRoom(request.getRoomName(), request.getChatRoomType(), request.getFloor());
        return ResponseEntity.status(HttpStatus.CREATED).body(roomId);
    }


    //채팅방 이전 메시지 조회
    @GetMapping("/history/{roomId}")
    public ResponseEntity<?> getChatHistory(@PathVariable Long roomId) {
        return ResponseEntity.ok(chatService.getChatHistory(roomId));
    }
}
