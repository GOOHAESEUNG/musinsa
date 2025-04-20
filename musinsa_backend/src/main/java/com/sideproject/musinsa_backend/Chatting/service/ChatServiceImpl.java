package com.sideproject.musinsa_backend.Chatting.service;

import com.sideproject.musinsa_backend.Chatting.dto.ChatMessageReqDto;
import com.sideproject.musinsa_backend.Chatting.repository.ChatMessageRepository;
import com.sideproject.musinsa_backend.Chatting.repository.ChatParticipantRepository;
import com.sideproject.musinsa_backend.Chatting.repository.ChatRoomRepository;
import com.sideproject.musinsa_backend.Chatting.repository.ReadStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatParticipantRepository chatParticipantRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ReadStatusRepository readStatusRepository;
    private final ChatService chatService;


    @Override
    public void saveMessage(Long roomId, ChatMessageReqDto chatMessageReqDto){

    }

}
