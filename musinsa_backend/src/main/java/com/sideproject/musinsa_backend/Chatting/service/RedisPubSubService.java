package com.sideproject.musinsa_backend.Chatting.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sideproject.musinsa_backend.Chatting.dto.ChatMessageDto;
import com.sideproject.musinsa_backend.Chatting.dto.ChatMessageHisDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;


@RequiredArgsConstructor
@Service
public class RedisPubSubService implements MessageListener {

    private final StringRedisTemplate stringRedisTemplate;
    private final SimpMessagingTemplate messagingTemplate;


// 서버에서 publish("chat", jsonString) 형식으로 메시지를 Redis에 발행함.
// 특정 채널에 발행!!
//    메시지 발행 Publish 용 서비스 로직
    public void publish(String channel, String message) {
        stringRedisTemplate.convertAndSend(channel, message);
    }



//     메시지 수신(Subscribe -> OnMessage)
//    레디스가 "chat"채널에 새로운 메시지가 들어오면
//    RedisConfig에서 설정한 RedisMessageListenerContainer가 감지하고
//    이 메서드를 호출함
    @Override
    public void onMessage(Message message, byte[] pattern) {
        String payload = new String(message.getBody()); //redis로부터 수신된 메시지(문자열)

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        try{
//            reids에서 받은 메시지는 단순 문자열(JSON)이므로
//            ChatMessageDto 형태의 객체로 변환해줌
            ChatMessageDto chatMessageDto = objectMapper.readValue(payload,ChatMessageDto.class);

//            DTO에 포함된 roomID를 통해 STOMP 경로를 만듦
//            해당 방을 구독중인 클라이언트에게 메시지를 브로드 캐스트함
            messagingTemplate.convertAndSend("/topic/" + chatMessageDto.getRoomId(), chatMessageDto);
        }catch (JsonProcessingException e){
            throw new RuntimeException(e);
        }
    }
}