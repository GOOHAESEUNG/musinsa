package com.sideproject.musinsa_backend.Chatting.dto;

import com.sideproject.musinsa_backend.Chatting.domain.ChatRoomType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatRoomResDto {
    private Long roomId;
    private String roomName;
    private String isGroupChat;
    private ChatRoomType chatRoomType;
    private String floor;
}
