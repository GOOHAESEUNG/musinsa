package com.sideproject.musinsa_backend.Employee.domain;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;


@Entity
@Builder
@AllArgsConstructor //모든 필드를 매개변수로 받는 생성자 생성
@NoArgsConstructor//매개변수가 없는 기본 생성자를 자동으로 생성
@Getter
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    //근무층
    private Integer floor;

    //입사일
    private Date join_date;

    @Column(nullable = false, unique = true)
    private String email;

    private String password;

    //직책
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Position position = Position.FT;

    //근무 형태
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Status status = Status.HOLI;
}
