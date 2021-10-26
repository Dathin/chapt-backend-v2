package me.pedrocaires.chapt.handler.message.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.pedrocaires.chapt.authentication.WebSocketAttachment;
import me.pedrocaires.chapt.handler.Broadcast;
import me.pedrocaires.chapt.handler.message.MessageExecutor;
import me.pedrocaires.chapt.handler.transformer.BroadcastToSerializableBroadcast;
import org.java_websocket.WebSocket;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Component
public class AuthMessageExecutor extends MessageExecutor<AuthRequestDTO, AuthResponseDTO> {

    public AuthMessageExecutor(ObjectMapper objectMapper,
                               BroadcastToSerializableBroadcast broadcastToSerializableBroadcast) {
        super(objectMapper, broadcastToSerializableBroadcast);
    }

    @Override
    public Optional<Broadcast<AuthResponseDTO>> handleMessage(AuthRequestDTO message, WebSocket client,
                                                              Map<String, WebSocket> clients) {
        var authenticated = authenticate(message);
        var webSocketAttachment = (WebSocketAttachment) client.getAttachment();
        var authentication = webSocketAttachment.getAuthenticationFilter();
        authentication.setAuthenticated(authenticated);
        var authResponse = new AuthResponseDTO(authenticated);
        if (authenticated) {
            clients.put(message.getUsername(), client);
        }
        return Optional.of(new Broadcast(authResponse, Collections.singleton(client)));
    }

    private boolean authenticate(AuthRequestDTO message) {
        if (Objects.equals("pedro", message.getUsername()) && Objects.equals("caires", message.getPassword())) {
            return true;
        }
		return Objects.equals("daniel", message.getUsername()) && Objects.equals("caires", message.getPassword());
	}

}
