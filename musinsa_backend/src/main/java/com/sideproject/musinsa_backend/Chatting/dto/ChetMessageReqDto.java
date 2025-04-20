package com.sideproject.musinsa_backend.Chatting.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChetMessageReqDto {
    private Long roomId;
    private String message;
    private String senderEmail;
}
