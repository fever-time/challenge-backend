package shop.fevertime.backend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@RequiredArgsConstructor
@Configuration
@EnableWebSocketMessageBroker // EnableWebsocket 알아보기. , 자료구조 Queue, STOMP 검색해서 통신 방법 알아보기
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer { // websoket.io, 만약 댓글을 10만명이 달게 된다면?, port를 잡는다. 확장이 어렵다.

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/sub"); // 메모리 기반 메세지 브로커가 해당 api를 구독하고 있는 클라이언트에게 메세지를 전달한다.
        registry.setApplicationDestinationPrefixes("/pub"); // 서버에서 클라이언트로부터의 메세지를 받을 api를 prefix 설정한다.
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) { // 클라이언트에서 websoket을 연결할 api를 설정한다.
        registry.addEndpoint("/ws-stomp").setAllowedOrigins("https://api.fevertime.shop", "https://www.fevertime.shop", "http://localhost:8080", "http://localhost:63342").withSockJS();
    }
}
