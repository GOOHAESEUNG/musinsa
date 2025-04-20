package com.sideproject.musinsa_backend.Chatting.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;


//스프링 웹소켓, 스톰프 설정 파일(서버 입구)
@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class StompWebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final StompHandler stompHandler;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/connect")
                .setAllowedOrigins("http://localhost:8081")
//                ws://가 아닌 http:// 엔드포인트를 사용할 있게 해주는 sockJs 라이브버리를 통한 요청을 허용하는 설정
                .withSockJS();
    }

//    STOMP 메시지 통신 구조에서, 어떤 경로로 메시지를 받으면 우리가 처리하고, 어떤 경로는 메시지 브로커가 자동으로 처리할지를 정의만! 해주는 메서드
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
//        메시지를 발행할 때는 ex publish/1 이런 룰을 가지고 발행
//        publish로 시작하는 url 패턴으로 메시지가 발행되면 @Controller 객체의 @MessageMapping 메서드로 라우팅
        registry.setApplicationDestinationPrefixes("/publish");
//        /topic/1 형태로 메시지를 수신(sub)해야함을 설정
        registry.enableSimpleBroker("/topic");
    }


//사용자의 모든 요청(CONNECT, SUBSCRIBE, SEND, DISCONNECT)을 가로채는 인터셉터 등록
//    여기서 JWT 같은 인증/검증 로직 실행
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompHandler);
    }
}
