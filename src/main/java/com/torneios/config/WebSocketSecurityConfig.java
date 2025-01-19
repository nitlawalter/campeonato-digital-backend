package com.torneios.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

@Configuration
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages
            .simpTypeMatchers(SimpMessageType.CONNECT, 
                            SimpMessageType.HEARTBEAT, 
                            SimpMessageType.UNSUBSCRIBE,
                            SimpMessageType.DISCONNECT).permitAll()
            .simpDestMatchers("/topic/public/**").permitAll()
            .simpDestMatchers("/topic/campeonatos/**").authenticated()
            .simpDestMatchers("/topic/fases/**").authenticated()
            .simpDestMatchers("/topic/inscricoes/**").authenticated()
            .anyMessage().authenticated();
    }

    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }
} 