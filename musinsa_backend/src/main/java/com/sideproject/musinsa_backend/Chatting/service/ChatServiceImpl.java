package com.sideproject.musinsa_backend.Chatting.service;

import com.sideproject.musinsa_backend.Chatting.domain.ChatMessage;
import com.sideproject.musinsa_backend.Chatting.domain.ChatParticipant;
import com.sideproject.musinsa_backend.Chatting.domain.ChatRoom;
import com.sideproject.musinsa_backend.Chatting.domain.ReadStatus;
import com.sideproject.musinsa_backend.Chatting.dto.ChatMessageReqDto;
import com.sideproject.musinsa_backend.Chatting.exception.ChatParticipantNotFoundException;
import com.sideproject.musinsa_backend.Chatting.exception.ChatRoomNotFoundException;
import com.sideproject.musinsa_backend.Chatting.repository.ChatMessageRepository;
import com.sideproject.musinsa_backend.Chatting.repository.ChatParticipantRepository;
import com.sideproject.musinsa_backend.Chatting.repository.ChatRoomRepository;
import com.sideproject.musinsa_backend.Chatting.repository.ReadStatusRepository;
import com.sideproject.musinsa_backend.Employee.domain.Employee;
import com.sideproject.musinsa_backend.Employee.exception.EmployeeNotFoundException;
import com.sideproject.musinsa_backend.Employee.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatParticipantRepository chatParticipantRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ReadStatusRepository readStatusRepository;
    private final EmployeeRepository employeeRepository;
    private final ChatService chatService;


    @Override
    public void saveMessage(Long roomId, ChatMessageReqDto chatMessageReqDto){

//         채팅방 조회
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() ->new ChatRoomNotFoundException("존재 하지 않은 채팅방 입니다."));

//        보낸 사람 조회
        Employee sender = employeeRepository.findByEmail(chatMessageReqDto.getSenderEmail())
                .orElseThrow(() -> new EmployeeNotFoundException("존재하지 않은 회원 입니다."));

//        메시지 저장
        ChatMessage chatMessage = ChatMessage.builder()
                .chatRoom(chatRoom)
                .employee(sender)
                .content(chatMessageReqDto.getMessage())
                .build();

        chatMessageRepository.save(chatMessage);


//        사용자별 읽음 처리
        List<ChatParticipant> chatParticipants = chatParticipantRepository.findByChatRoom(chatRoom);
        for(ChatParticipant c : chatParticipants){
            ReadStatus readStatus = ReadStatus.builder()
                    .chatRoom(chatRoom)
                    .empolyee(c.getEmpolyee())
                    .chatMessage(chatMessage)
                    .isRead(c.getEmpolyee().equals(sender))
                    .build();
            readStatusRepository.save(readStatus);
        }

    }

}
