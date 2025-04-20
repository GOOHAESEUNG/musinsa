package com.sideproject.musinsa_backend.Chatting.service;

import com.sideproject.musinsa_backend.Chatting.domain.*;
import com.sideproject.musinsa_backend.Chatting.dto.ChatMessageReqDto;
import com.sideproject.musinsa_backend.Chatting.dto.ChatRoomResDto;
import com.sideproject.musinsa_backend.Chatting.exception.ChatParticipantNotFoundException;
import com.sideproject.musinsa_backend.Chatting.exception.ChatRoomNotFoundException;
import com.sideproject.musinsa_backend.Chatting.exception.GroupRoomUnauthorizedException;
import com.sideproject.musinsa_backend.Chatting.repository.ChatMessageRepository;
import com.sideproject.musinsa_backend.Chatting.repository.ChatParticipantRepository;
import com.sideproject.musinsa_backend.Chatting.repository.ChatRoomRepository;
import com.sideproject.musinsa_backend.Chatting.repository.ReadStatusRepository;
import com.sideproject.musinsa_backend.Employee.domain.Employee;
import com.sideproject.musinsa_backend.Employee.domain.Position;
import com.sideproject.musinsa_backend.Employee.exception.EmployeeNotFoundException;
import com.sideproject.musinsa_backend.Employee.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
    public void saveMessage(Long roomId, ChatMessageReqDto chatMessageReqDto) {

//         채팅방 조회
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new ChatRoomNotFoundException("존재 하지 않은 채팅방 입니다."));

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
        for (ChatParticipant c : chatParticipants) {
            ReadStatus readStatus = ReadStatus.builder()
                    .chatRoom(chatRoom)
                    .empolyee(c.getEmpolyee())
                    .chatMessage(chatMessage)
                    .isRead(c.getEmpolyee().equals(sender))
                    .build();
            readStatusRepository.save(readStatus);
        }

    }


    @Override
    //    단체 채팅방 생성
    public void createGroupRoom(String chatRoomName, ChatRoomType chatRoomtype, String floor) {
        Employee employee = employeeRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new EmployeeNotFoundException("존재하지 않은 회원입니다."));

//        관리자 권한만 단체 채팅방 생성할 수 있게함
        Position employeePosition = employee.getPosition();

        Set<Position> allowedPostitions = Set.of(Position.SM, Position.ASM, Position.VMD, Position.FM);

        if (!allowedPostitions.contains(employeePosition)) {
            throw new GroupRoomUnauthorizedException("해당 직책은 단체 채팅방을 생성할 수 없습니다.");
        }

// 채팅방 생성
        ChatRoom.ChatRoomBuilder chatRoomBuilder = ChatRoom.builder()
                .name(chatRoomName)
                .chatRoomType(chatRoomtype)
                .isGroupChat("Y");

// ChatRoomType이 FLOOR일 경우에만 floor 값 세팅
        if (chatRoomtype.equals(ChatRoomType.FLOOR)) {
            chatRoomBuilder.floor(floor);
        }

        ChatRoom chatRoom = chatRoomBuilder.build();
        chatRoomRepository.save(chatRoom);


//층별 채팅방일 경우
        if (chatRoomtype.equals(ChatRoomType.FLOOR)) {
            //        채팅참여자로 개설자를 추가 및 해당 층 직원들 자동으로 참가
            List<Employee> floorEmployees = employeeRepository.findByFloor(floor);

            // 해당 층 직원들 채팅 참여자로 추가
            for (Employee e : floorEmployees) {
                ChatParticipant c = ChatParticipant.builder()
                        .chatRoom(chatRoom)
                        .empolyee(e)
                        .build();

                chatParticipantRepository.save(c);
            }

            //채팅방 만든 관리자도 참가자로 추가
            ChatParticipant made = ChatParticipant.builder()
                    .chatRoom(chatRoom)
                    .empolyee(employee)
                    .build();

            chatParticipantRepository.save(made);


        }
//        전체 공지방 혹은 상품 요청 채팅방일 경우
        else if (chatRoomtype.equals(ChatRoomType.NOTICE) || chatRoomtype.equals(ChatRoomType.PROREQ)) {
            List<Employee> allEmployees = employeeRepository.findAll();

//            모든 직원을 참여자로 추가
            for (Employee e : allEmployees) {
                ChatParticipant c = ChatParticipant.builder()
                        .chatRoom(chatRoom)
                        .empolyee(e)
                        .build();

                chatParticipantRepository.save(c);
            }
        }
//        사담 단체방일 경우
        else {

            ChatParticipant made = ChatParticipant.builder()
                    .chatRoom(chatRoom)
                    .empolyee(employee)
                    .build();
            chatParticipantRepository.save(made);

        }

    }

    @Override
    public List<ChatRoomResDto> getMyGroupChatRooms(){
        List<ChatRoom> chatRooms = chatRoomRepository.findByIsGroupChat("Y");

        List<ChatRoomResDto> chatRoomResDtos = new ArrayList<>();

        for(ChatRoom c : chatRooms){
            ChatRoomResDto dto = ChatRoomResDto
                    .builder()
                    .roomId(c.getId())
                    .roomName(c.getName())
                    .isGroupChat(c.getIsGroupChat())
                    .chatRoomType(c.getChatRoomType())
                    .floor(c.getFloor())
                    .build();

            chatRoomResDtos.add(dto);
        }

        return chatRoomResDtos;
    }
}
