package com.sideproject.musinsa_backend.Chatting.service;

import com.sideproject.musinsa_backend.Chatting.dto.ChatMessageReqDto;

public interface ChatService {

    void saveMessage(Long roomId, ChatMessageReqDto chatMessageReqDto);
}
