package com.sideproject.musinsa_backend.Employee.controller;

import com.sideproject.musinsa_backend.Common.auth.JwtTokenProvider;
import com.sideproject.musinsa_backend.Employee.domain.Employee;
import com.sideproject.musinsa_backend.Employee.dto.EmployeeLoginDto;
import com.sideproject.musinsa_backend.Employee.dto.EmployeeSaveDto;
import com.sideproject.musinsa_backend.Employee.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/employee")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final JwtTokenProvider jwtTokenProvider;

    //회원가입
    @PostMapping("/create")
    public ResponseEntity<?> employeeCreate(
            @RequestBody EmployeeSaveDto employeeSaveDto) {
        Employee employee = employeeService.create(employeeSaveDto);
        return new ResponseEntity<>(employee.getId(), HttpStatus.CREATED);
    }

    //로그인
    @PostMapping("/doLogin")
    public ResponseEntity<?> doLogin(
            @RequestBody EmployeeLoginDto employeeLoginDto){

        //email, pw 검증
       Employee employee =  employeeService.login(employeeLoginDto);

       //일치할 경우 엑세스 토큰 발급
       String jwtToken = jwtTokenProvider.createToken(employee.getEmail(),employee.getPosition().toString());

        Map<String, Object> loginInfo = new HashMap<>();
        loginInfo.put("id", employee.getId());
        loginInfo.put("toekn", jwtToken);
        return new ResponseEntity<>(loginInfo, HttpStatus.OK);
    }

}
