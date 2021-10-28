package me.pedrocaires.chapt.handler.message.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.pedrocaires.chapt.authentication.WebSocketAttachment;
import me.pedrocaires.chapt.handler.Broadcast;
import me.pedrocaires.chapt.handler.message.MessageExecutor;
import me.pedrocaires.chapt.handler.message.shareddto.AckResponseDTO;
import me.pedrocaires.chapt.handler.message.shareddto.UserInfoDTO;
import me.pedrocaires.chapt.handler.transformer.BroadcastToSerializableBroadcast;
import me.pedrocaires.chapt.repository.user.UserRepository;
import org.java_websocket.WebSocket;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthMessageExecutor extends MessageExecutor<UserInfoDTO, AckResponseDTO> {

	private final UserRepository userRepository;

	public AuthMessageExecutor(ObjectMapper objectMapper,
			BroadcastToSerializableBroadcast broadcastToSerializableBroadcast, UserRepository userRepository) {
		super(objectMapper, broadcastToSerializableBroadcast);
		this.userRepository = userRepository;
	}

	@Override
	public Optional<Broadcast<AckResponseDTO>> handleMessage(UserInfoDTO message, WebSocket client,
			Map<String, WebSocket> clients) {
		var authenticated = userRepository.findUserByUsernameAndPassword(message.getUsername(), message.getPassword())
				.isPresent();
		var webSocketAttachment = (WebSocketAttachment) client.getAttachment();
		var authentication = webSocketAttachment.getAuthenticationFilter();
		authentication.setAuthenticated(authenticated);
		var authResponse = new AckResponseDTO(authenticated);
		if (authenticated) {
			clients.put(message.getUsername(), client);
		}
		return Optional.of(new Broadcast<>(authResponse, Collections.singleton(client)));
	}

}
