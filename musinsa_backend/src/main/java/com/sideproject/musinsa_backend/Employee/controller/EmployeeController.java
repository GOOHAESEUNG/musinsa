package com.sideproject.musinsa_backend.Employee.controller;

import com.sideproject.musinsa_backend.Common.auth.JwtTokenProvider;
import com.sideproject.musinsa_backend.Employee.domain.Employee;
import com.sideproject.musinsa_backend.Employee.dto.EmployeeLoginDto;
import com.sideproject.musinsa_backend.Employee.dto.EmployeeSaveDto;
import com.sideproject.musinsa_backend.Employee.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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
        loginInfo.put("token", jwtToken);
        return new ResponseEntity<>(loginInfo, HttpStatus.OK);
    }

    //내 정보 조회하기
    @GetMapping("/myInfo")
    public ResponseEntity<?> getMyInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName(); // 로그인한 사용자의 이메일
        Employee employee = employeeService.findByEmail(email);

        Map<String, Object> info = new HashMap<>();
        info.put("id", employee.getId());
        info.put("name", employee.getName());
        info.put("email", employee.getEmail());
        info.put("position", employee.getPosition());
        info.put("floor", employee.getFloor());

        return ResponseEntity.ok(info);
    }
}
