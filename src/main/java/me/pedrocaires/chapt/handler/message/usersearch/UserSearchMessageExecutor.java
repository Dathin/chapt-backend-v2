package me.pedrocaires.chapt.handler.message.usersearch;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.pedrocaires.chapt.handler.Broadcast;
import me.pedrocaires.chapt.handler.message.MessageExecutor;
import me.pedrocaires.chapt.handler.transformer.BroadcastToSerializableBroadcast;
import me.pedrocaires.chapt.repository.user.UserRepository;
import org.java_websocket.WebSocket;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public class UserSearchMessageExecutor extends MessageExecutor<UserSearchRequestDTO, UserSearchResponseDTO> {

	private final UserRepository userRepository;

	protected UserSearchMessageExecutor(ObjectMapper objectMapper,
			BroadcastToSerializableBroadcast broadcastToSerializableBroadcast, UserRepository userRepository) {
		super(objectMapper, broadcastToSerializableBroadcast);
		this.userRepository = userRepository;
	}

	@Override
	public Optional<Broadcast<UserSearchResponseDTO>> handleMessage(UserSearchRequestDTO message, WebSocket client,
			Map<Integer, WebSocket> clients) {
		var users = userRepository.searchUsers(message.getUsername(), message.getPage(), message.getSize());
		return Optional.of(new Broadcast<>(new UserSearchResponseDTO(message.getHandler(), users),
				Collections.singletonList(client)));
	}

}
