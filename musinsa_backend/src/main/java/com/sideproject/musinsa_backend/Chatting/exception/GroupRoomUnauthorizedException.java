package com.sideproject.musinsa_backend.Chatting.exception;

public class GroupRoomUnauthorizedException extends RuntimeException {
    public GroupRoomUnauthorizedException(String message) {
        super(message);
    }
}
