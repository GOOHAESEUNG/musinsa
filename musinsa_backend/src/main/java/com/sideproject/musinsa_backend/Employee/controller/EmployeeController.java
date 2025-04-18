package com.sideproject.musinsa_backend.Employee.controller;

import com.sideproject.musinsa_backend.Common.auth.JwtTokenProvider;
import com.sideproject.musinsa_backend.Employee.domain.Employee;
import com.sideproject.musinsa_backend.Employee.dto.EmployeeSaveDto;
import com.sideproject.musinsa_backend.Employee.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/employee")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/create")
    public ResponseEntity<?> employeeCreate(
            @RequestBody EmployeeSaveDto employeeSaveDto) {
        Employee employee = employeeService.create(employeeSaveDto);
        return new ResponseEntity<>(employee.getId(), HttpStatus.CREATED);
    }
}
