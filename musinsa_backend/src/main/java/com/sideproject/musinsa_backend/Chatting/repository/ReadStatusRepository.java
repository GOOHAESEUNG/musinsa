package com.sideproject.musinsa_backend.Chatting.repository;

import com.sideproject.musinsa_backend.Chatting.domain.ChatRoom;
import com.sideproject.musinsa_backend.Chatting.domain.ReadStatus;
import com.sideproject.musinsa_backend.Employee.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReadStatusRepository extends JpaRepository<ReadStatus, Long> {
    List<ReadStatus> findByChatRoomAndEmployee(ChatRoom chatRoom, Employee employee);

    Long countByChatRoomAndEmployeeAndIsReadFalse(ChatRoom chatRoom, Employee employee);
}
