package com.sideproject.musinsa_backend.Chatting.service;

import com.sideproject.musinsa_backend.Chatting.domain.*;
import com.sideproject.musinsa_backend.Chatting.dto.ChatMessageDto;
import com.sideproject.musinsa_backend.Chatting.dto.ChatMessageHisDto;
import com.sideproject.musinsa_backend.Chatting.dto.ChatMessageReqDto;
import com.sideproject.musinsa_backend.Chatting.dto.ChatRoomResDto;
import com.sideproject.musinsa_backend.Chatting.exception.ChatRoomNotFoundException;
import com.sideproject.musinsa_backend.Chatting.exception.GroupRoomUnauthorizedException;
import com.sideproject.musinsa_backend.Chatting.repository.ChatMessageRepository;
import com.sideproject.musinsa_backend.Chatting.repository.ChatParticipantRepository;
import com.sideproject.musinsa_backend.Chatting.repository.ChatRoomRepository;
import com.sideproject.musinsa_backend.Chatting.repository.ReadStatusRepository;
import com.sideproject.musinsa_backend.Employee.domain.Employee;
import com.sideproject.musinsa_backend.Employee.domain.Position;
import com.sideproject.musinsa_backend.Employee.exception.EmployeeNotFoundException;
import com.sideproject.musinsa_backend.Employee.exception.InvalidPasswordException;
import com.sideproject.musinsa_backend.Employee.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatServiceImpl implements ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatParticipantRepository chatParticipantRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ReadStatusRepository readStatusRepository;
    private final EmployeeRepository employeeRepository;


    @Override
    public ChatMessageDto saveMessage(Long roomId, ChatMessageDto chatMessageDto) {

//         채팅방 조회
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new ChatRoomNotFoundException("존재 하지 않은 채팅방 입니다."));

//        보낸 사람 조회
        Employee sender = employeeRepository.findByEmail(chatMessageDto.getSenderEmail())
                .orElseThrow(() -> new EmployeeNotFoundException("존재하지 않은 회원 입니다."));

//        메시지 저장
        ChatMessage chatMessage = ChatMessage.builder()
                .chatRoom(chatRoom)
                .employee(sender)
                .content(chatMessageDto.getMessage())
                .messageType(chatMessageDto.getMessageType())
                .build();

        chatMessageRepository.save(chatMessage);

        chatMessageDto.setSenderName(sender.getName());
        chatMessageDto.setSendDate(chatMessage.getCreateTime());


//        사용자별 읽음 처리
        List<ChatParticipant> chatParticipants = chatParticipantRepository.findByChatRoom(chatRoom);
        for (ChatParticipant c : chatParticipants) {
            ReadStatus readStatus = ReadStatus.builder()
                    .chatRoom(chatRoom)
                    .employee(c.getEmployee())
                    .chatMessage(chatMessage)
                    .isRead(c.getEmployee().getId().equals(sender.getId()))
                    .build();
            readStatusRepository.save(readStatus);
        }

        return chatMessageDto;
    }


    @Override
    //    단체 채팅방 생성
    public Long createGroupRoom(String chatRoomName, ChatRoomType chatRoomtype, String floor) {
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
                        .employee(e)
                        .build();

                chatParticipantRepository.save(c);
            }

            //채팅방 만든 관리자도 참가자로 추가
            ChatParticipant made = ChatParticipant.builder()
                    .chatRoom(chatRoom)
                    .employee(employee)
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
                        .employee(e)
                        .build();

                chatParticipantRepository.save(c);
            }
        }
//        사담 단체방일 경우
        else {

            ChatParticipant made = ChatParticipant.builder()
                    .chatRoom(chatRoom)
                    .employee(employee)
                    .build();
            chatParticipantRepository.save(made);

        }

        return chatRoom.getId();
    }

    //그룹 채팅방 목록
    @Override
    public List<ChatRoomResDto> getMyGroupChatRooms() {
        //현재 유저의 정보를 받아옴
        Employee employee = employeeRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new EmployeeNotFoundException("존재 하지 않은 회원 입니다."));

        //반환 객체 생성
        List<ChatRoomResDto> chatRoomResDtos = new ArrayList<>();

        //그룹 채팅방만 리스트로 가져옴
        List<ChatRoom> chatRooms = chatRoomRepository.findByIsGroupChat("Y");

        for (ChatRoom c : chatRooms) {
            Optional<ChatParticipant> chatParticipant = chatParticipantRepository.findByChatRoomAndEmployee(c, employee);

            if (chatParticipant.isPresent()) {
                Long count = readStatusRepository.countByChatRoomAndEmployeeAndIsReadFalse(c, employee);
                //참여한 채팅방일 경우
                ChatRoomResDto dto = ChatRoomResDto
                        .builder()
                        .roomId(c.getId())
                        .roomName(c.getName())
                        .isGroupChat(c.getIsGroupChat())
                        .chatRoomType(c.getChatRoomType())
                        .floor(c.getFloor())
                        .isParticipant(true)
                        .unreadCount(count)
                        .build();

                chatRoomResDtos.add(dto);
            }else{
                //참여하지 않은 채팅방일 경우
                ChatRoomResDto dto = ChatRoomResDto
                        .builder()
                        .roomId(c.getId())
                        .roomName(c.getName())
                        .isGroupChat(c.getIsGroupChat())
                        .chatRoomType(c.getChatRoomType())
                        .floor(c.getFloor())
                        .isParticipant(false)
                        .unreadCount(null)
                        .build();

                chatRoomResDtos.add(dto);
            }

        }

        return chatRoomResDtos;
    }


    //채팅 이전 기록 가져오기
    @Override
    public List<ChatMessageHisDto> getChatHistory(Long roomId) {

        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new ChatRoomNotFoundException("존재하지 않은 채팅방입니다."));


//            특정 룸에 대한 메시지 조회
        List<ChatMessage> chatMessages = chatMessageRepository.findByChatRoomOrderByCreateTimeAsc(chatRoom);

        List<ChatMessageHisDto> chatMessageHisDtos = new ArrayList<>();

        for (ChatMessage c : chatMessages) {
            ChatMessageHisDto chatMessageHisDto = ChatMessageHisDto.builder()
                    .message(c.getContent())
                    .senderEmail(c.getEmployee().getEmail())
                    .messageType(c.getMessageType())
                    .senderName(c.getEmployee().getName())
                    .sendDate(c.getCreateTime())
                    .build();

            chatMessageHisDtos.add(chatMessageHisDto);
        }

        return chatMessageHisDtos;

    }

    //    채팅방 참가하기
    @Override
    public void addParticipantToGroupChat(Long roomId) {
//        채팅방 조회
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new ChatRoomNotFoundException("존재하지 않은 채팅방입니다."));

//        현재 로그인한 유저 조회
        Employee employee = employeeRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new EmployeeNotFoundException("존재 하지 않은 회원 입니다."));

//        그룹 채팅인지 여부 판단
        if (chatRoom.getIsGroupChat().equals("N")) {
            throw new IllegalArgumentException("그룹 채팅이 아닙니다.");
        }

//        이미 참여자 인지 검증
        Optional<ChatParticipant> participant = chatParticipantRepository.findByChatRoomAndEmployee(chatRoom, employee);

//        참여하지 않은 회원이라면, 참가자에 추가
        if (!participant.isPresent()) {
            addParticipantToRoom(chatRoom, employee);
        }
    }

    public void addParticipantToRoom(ChatRoom chatRoom, Employee employee) {
        ChatParticipant chatParticipant = ChatParticipant.builder()
                .chatRoom(chatRoom)
                .employee(employee)
                .build();

        chatParticipantRepository.save(chatParticipant);
    }

    //    메시지 읽음 처리 여부
    @Override
    @Transactional
    public void messageRead(Long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new ChatRoomNotFoundException("존재하지 않은 채팅방입니다."));

        Employee employee = employeeRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new EmployeeNotFoundException("존재하지 않은 회원입니다."));

        List<ReadStatus> readStatuses = readStatusRepository.findByChatRoomAndEmployee(chatRoom, employee);

        if (readStatuses == null || readStatuses.isEmpty()) {
            log.warn("읽음 처리할 메시지가 없음 → roomId: {}, employee: {}", roomId, employee.getEmail());
            return;
        }

        for (ReadStatus rs : readStatuses) {
            if (rs != null) {
                rs.updateIsRead(true);
            }
        }
    }


    //채팅방 자동 참가
    @Override
    public void addUserToEligibleRooms(Employee newUser) {
        List<ChatRoom> allRooms = chatRoomRepository.findByIsGroupChat("Y");

        for (ChatRoom room : allRooms) {
            switch (room.getChatRoomType()) {
                case NOTICE:
                case PROREQ:
                    // 전체 자동 참가
                    addParticipantToRoom(room, newUser);
                    break;
                case FLOOR:
                    // 층 일치 시 자동 참가
                    if (room.getFloor() != null && room.getFloor().equals(newUser.getFloor())) {
                        addParticipantToRoom(room, newUser);
                    }
                    break;
                default:
                    // PRIVATE는 수동 참가
                    break;
            }
        }
    }
}
