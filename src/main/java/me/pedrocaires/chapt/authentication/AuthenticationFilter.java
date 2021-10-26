package me.pedrocaires.chapt.authentication;

import me.pedrocaires.chapt.exception.UnauthenticatedException;
import me.pedrocaires.chapt.handler.Handler;
import org.java_websocket.WebSocket;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFilter {

    public void doFilter(WebSocket client, Handler handler) {
        var webSocketAttachment = (WebSocketAttachment) client.getAttachment();
        var authentication = webSocketAttachment.getAuthenticationFilter();
        if (!authentication.isAuthenticated() && handler != Handler.AUTH) {
            throw new UnauthenticatedException();
        }
    }

}
