package com.blog.api.configuration;

import com.sun.security.auth.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.util.LinkedMultiValueMap;

import java.util.Objects;

@Slf4j
public class UserInterceptor implements ChannelInterceptor {
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT.equals(accessor.getCommand()) || Objects.isNull(accessor.getUser())) {
            System.out.println("connect ");
            Object raw = message.getHeaders().get(SimpMessageHeaderAccessor.NATIVE_HEADERS);
            String userId = (String) ((LinkedMultiValueMap) raw).get("userId").get(0);
            accessor.setUser(new UserPrincipal(userId));
        }

        System.out.println("USER ID --- " + accessor.getUser());

        return ChannelInterceptor.super.preSend(message, channel);
    }
}
