package com.sideproject.musinsa_backend.Chatting.dto;

import com.sideproject.musinsa_backend.Chatting.domain.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageHisDto {

    private String message;
    private String senderEmail;
    private String senderName;
    private MessageType messageType;

}
