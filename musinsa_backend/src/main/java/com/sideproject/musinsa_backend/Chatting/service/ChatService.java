package com.sideproject.musinsa_backend.Chatting.service;

import com.sideproject.musinsa_backend.Chatting.domain.ChatRoomType;
import com.sideproject.musinsa_backend.Chatting.dto.ChatMessageReqDto;
import com.sideproject.musinsa_backend.Chatting.dto.ChatRoomResDto;

import java.util.List;

public interface ChatService {

    void saveMessage(Long roomId, ChatMessageReqDto chatMessageReqDto);
    void createGroupRoom(String chatRoomName, ChatRoomType chatRoomtype, String floor);
    List<ChatRoomResDto> getMyGroupChatRooms();
}
