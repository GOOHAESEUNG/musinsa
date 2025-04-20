package com.sideproject.musinsa_backend.Chatting.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;

@Component
public class StompHandler implements ChannelInterceptor {

    @Value("${jwt.secretKey}")
    private String secretKey;

    private Key key;

    @PostConstruct
    public void initKey() {
        this.key = new SecretKeySpec(
                Base64.getDecoder().decode(secretKey),
                SignatureAlgorithm.HS512.getJcaName()
        );
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        //사용자 요청은 메시지에 담겨 있음 -> 여기서 토큰 꺼내봄

        final StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        //웹소켓 연결 요청(CONNECT)이 들어올 때 HTTP처럼 JWT 토큰을 검증하는 인터셉터
        if(StompCommand.CONNECT == accessor.getCommand()) {
            System.out.println("connect 요청시 토큰 유효성 검증");
            String bearerToken = accessor.getFirstNativeHeader("Authorization");

            String token = bearerToken.substring(7);

            //토큰 검증 및 claims 추출
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            System.out.println("토큰 검증 완료");
        }

        return message;
    }
}
