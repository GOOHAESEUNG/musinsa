package com.sideproject.musinsa_backend.Chatting.domain;


import com.sideproject.musinsa_backend.Common.domain.BaseTimeEntity;
import com.sideproject.musinsa_backend.Employee.domain.Employee;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "message_confirm",
        uniqueConstraints = @UniqueConstraint(columnNames = {"chat_message_id", "employee_id"})
)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class MessageConfirm extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_message_id", nullable = false)
    private ChatMessage chatMessage;

    //확인한 직원
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;




}
