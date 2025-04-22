package com.sideproject.musinsa_backend.Chatting.dto;

import com.sideproject.musinsa_backend.Chatting.domain.ChatRoomType;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateGroupRoomReqDto {
    private String roomName;
    private ChatRoomType chatRoomType;
    private String floor;
}
