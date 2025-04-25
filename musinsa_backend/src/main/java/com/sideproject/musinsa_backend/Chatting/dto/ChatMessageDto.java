package com.sideproject.musinsa_backend.Chatting.dto;

import com.sideproject.musinsa_backend.Chatting.domain.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageDto {
    private Long roomId;
    private String message;
    private String senderEmail;
    private String senderName;
    private MessageType messageType;
    private LocalDateTime sendDate;
}