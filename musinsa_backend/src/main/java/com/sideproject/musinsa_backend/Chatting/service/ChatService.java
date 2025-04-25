package com.sideproject.musinsa_backend.Chatting.service;

import com.sideproject.musinsa_backend.Chatting.domain.ChatRoom;
import com.sideproject.musinsa_backend.Chatting.domain.ChatRoomType;
import com.sideproject.musinsa_backend.Chatting.dto.ChatMessageDto;
import com.sideproject.musinsa_backend.Chatting.dto.ChatMessageHisDto;
import com.sideproject.musinsa_backend.Chatting.dto.ChatRoomResDto;
import com.sideproject.musinsa_backend.Employee.domain.Employee;

import java.util.List;

public interface ChatService {

    ChatMessageDto saveMessage(Long roomId, ChatMessageDto chatMessageDto);
    Long createGroupRoom(String chatRoomName, ChatRoomType chatRoomtype, String floor);
    List<ChatRoomResDto> getMyGroupChatRooms();
    List<ChatMessageHisDto> getChatHistory(Long roomId);
    void addParticipantToGroupChat(Long roomId);
}
