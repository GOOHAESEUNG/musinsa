package com.sideproject.musinsa_backend.Chatting.config;

import com.sideproject.musinsa_backend.Chatting.service.RedisPubSubService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;


@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private Integer port;


    //연결 기본 객체(레디스 연결을 위한 기본 객체)
    @Bean
    @Qualifier("chatPubSub")
    public RedisConnectionFactory chatPubSubFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(host);
        configuration.setPort(port);
//        redis pub/sub에는 특정 데이터베이스에 의존적이지 않음
        return new LettuceConnectionFactory(configuration);
    }


//    publish 객체 - 발행
//    레디스에 메시지를 보낼 때 사용하는 템플릿
//    스트링 형태로 채널에 메시지를 publish 할 수 있음
    @Bean
    @Qualifier("chatPubSub")
//    일반적으로 RedisTemplate<키 데이터 타입, 벨류 데이터 타입>을 씀
    public StringRedisTemplate stringRedisTemplate(
            @Qualifier("chatPubSub")RedisConnectionFactory connectionFactory)
    {
        return new StringRedisTemplate(connectionFactory);
    }


//    subsribe 객체 - 구독, 메시지 수신용
//    누군가 메시지를 발행하면 이 컨테이너가 감지함
//    메시지를 수신하면 지정된 메서드로 위임
//    레디스 채널을 구독하고, 메시지를 받아서 지정된 로직으로 넘겨줌
    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
            @Qualifier("chatPubSub") RedisConnectionFactory connectionFactory,
            MessageListenerAdapter messagelistenerAdapter) // 레디스 메시지를 수신하면 실행할 로직(메서드)을 포함하고 있는 어댑터 클래스, 이걸 통해 메시지를 수신한 후 어떤 메서드로 위임할지 설정
    {
//      레디스 메시지를 수신하는 핵심 객체인 RedisMessageContainer 생성
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();

//      레디스에 연결할 수 있도록 connectionFactory 주입
        container.setConnectionFactory(connectionFactory);

//        "chat"이라는 주제(topic, 채널명)을 구독하겠다는 의미
//        해당 채널에 메시지가 발행되면 messageListenerAdapter로 위임됨
        container.addMessageListener(messagelistenerAdapter, new PatternTopic("chat"));

//        이 redisMessageListenerContainer 인스턴스를 빈으로 등록해, redis pub/sub 기능이 스프링 환경에서 작동하도록 함
        return container;
    }

//    redis에서 수신된 메시지를 처리하는 객체 생성
    @Bean
    public MessageListenerAdapter messageListenerAdapter(RedisPubSubService redisPubSubService) {
//        redispubsubService 특정 메서드가 수신된 메시지를 처리할 수 있도록 지정
        return new MessageListenerAdapter(redisPubSubService, "onMessage");
    }

}
