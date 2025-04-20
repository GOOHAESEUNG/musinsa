package com.sideproject.musinsa_backend.Chatting.exception;

public class ChatParticipantNotFoundException extends RuntimeException {
    public ChatParticipantNotFoundException(String message) {
        super(message);
    }
}
