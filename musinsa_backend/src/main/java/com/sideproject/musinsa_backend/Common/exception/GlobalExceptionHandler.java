package com.sideproject.musinsa_backend.Common.exception;

import com.sideproject.musinsa_backend.Chatting.exception.ChatParticipantNotFoundException;
import com.sideproject.musinsa_backend.Chatting.exception.ChatRoomNotFoundException;
import com.sideproject.musinsa_backend.Employee.exception.EmployeeNotFoundException;
import com.sideproject.musinsa_backend.Employee.exception.InvalidPasswordException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleEmployeeNotFound(EmployeeNotFoundException e) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "Employee Not Found");
        errorResponse.put("message", e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception e) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "Internal Server Error");
        errorResponse.put("message", e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidPassword(InvalidPasswordException e) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Invalid Password");
        response.put("message", e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ChatRoomNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleChatRoomNotFound(ChatRoomNotFoundException e) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Chat Room Not Found");
        response.put("message", e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ChatParticipantNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleChatParticipantNotFound(ChatParticipantNotFoundException e) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Chat Participant Not Found");
        response.put("message", e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}