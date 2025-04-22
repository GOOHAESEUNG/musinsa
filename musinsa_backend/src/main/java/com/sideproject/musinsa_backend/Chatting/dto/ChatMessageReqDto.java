package com.sideproject.musinsa_backend.Chatting.dto;

import com.sideproject.musinsa_backend.Chatting.domain.MessageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.awt.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageReqDto {
    private String message;
    private String senderEmail;
    private MessageType messageType;

}
