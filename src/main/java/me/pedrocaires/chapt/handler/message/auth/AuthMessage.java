package me.pedrocaires.chapt.handler.message.auth;

import me.pedrocaires.chapt.authentication.WebSocketAttachment;
import me.pedrocaires.chapt.handler.Broadcast;
import me.pedrocaires.chapt.handler.message.Message;
import org.java_websocket.WebSocket;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class AuthMessage implements Message<AuthRequestDTO, AuthResponseDTO> {

	@Override
	public Optional<Broadcast<AuthResponseDTO>> handleMessage(AuthRequestDTO message, WebSocket client,
			Map<String, WebSocket> clients) {
		var authenticated = authenticate(message);
		var webSocketAttachment = (WebSocketAttachment) client.getAttachment();
		var authentication = webSocketAttachment.getAuthenticationFilter();
		authentication.setAuthenticated(authenticated);
		var authResponse = new AuthResponseDTO(authenticated);
		clients.put(message.getUsername(), client);
		return Optional.of(new Broadcast(authResponse, Collections.singleton(client)));
	}

	private boolean authenticate(AuthRequestDTO message) {
		if (Objects.equals("pedro", message.getUsername()) && Objects.equals("caires", message.getPassword())) {
			return true;
		}
		if (Objects.equals("daniel", message.getUsername()) && Objects.equals("caires", message.getPassword())) {
			return true;
		}
		return false;
	}

}
