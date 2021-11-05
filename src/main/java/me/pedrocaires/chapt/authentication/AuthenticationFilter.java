package me.pedrocaires.chapt.authentication;

import me.pedrocaires.chapt.exception.UnauthenticatedException;
import me.pedrocaires.chapt.handler.Handler;
import org.java_websocket.WebSocket;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFilter {

	public void doFilter(WebSocket client, Handler handler) {
		var webSocketAttachment = (WebSocketAttachment) client.getAttachment();
		var authentication = webSocketAttachment.getAuthentication();
		if (!authentication.isAuthenticated() && (handler != Handler.AUTH && handler != Handler.CREATE_USER)) {
			throw new UnauthenticatedException();
		}
	}

}
