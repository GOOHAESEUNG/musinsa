package com.sideproject.musinsa_backend.Employee.service;

import com.sideproject.musinsa_backend.Employee.domain.Employee;
import com.sideproject.musinsa_backend.Employee.domain.Position;
import com.sideproject.musinsa_backend.Employee.dto.EmployeeSaveDto;
import com.sideproject.musinsa_backend.Employee.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Employee create(EmployeeSaveDto dto){

        //이미 가입이 되어있는 이메일인지 검증
        if(employeeRepository.findByEmail(dto.getEmail()).isPresent()){
            throw new IllegalArgumentException("이미 존재하는 이메일 입니다.");
        }

        Employee newEmployee = Employee.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword())) //비밀번호를 암호화시킴
                .position(Position.valueOf(dto.getPosition().toUpperCase()))
                .floor(dto.getFloor())
                .join_date(dto.getJoin_date())
                .build();

        Employee savedEmployee = employeeRepository.save(newEmployee);
        return savedEmployee;
    }
}
