package me.pedrocaires.chapt.handler.message.createuser;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.pedrocaires.chapt.handler.Broadcast;
import me.pedrocaires.chapt.handler.message.MessageExecutor;
import me.pedrocaires.chapt.handler.message.auth.AuthMessageExecutor;
import me.pedrocaires.chapt.handler.message.shareddto.UserInfoDTO;
import me.pedrocaires.chapt.handler.message.shareddto.AckResponseDTO;
import me.pedrocaires.chapt.handler.transformer.BroadcastToSerializableBroadcast;
import me.pedrocaires.chapt.repository.user.UserRepository;
import org.java_websocket.WebSocket;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Service
public class CreateUserMessageExecutor extends MessageExecutor<UserInfoDTO, AckResponseDTO> {

	private final UserRepository userRepository;

	private final AuthMessageExecutor authMessageExecutor;

	protected CreateUserMessageExecutor(ObjectMapper objectMapper,
			BroadcastToSerializableBroadcast broadcastToSerializableBroadcast, UserRepository userRepository,
			AuthMessageExecutor authMessageExecutor) {
		super(objectMapper, broadcastToSerializableBroadcast);
		this.userRepository = userRepository;
		this.authMessageExecutor = authMessageExecutor;
	}

	@Override
	public Optional<Broadcast<AckResponseDTO>> handleMessage(UserInfoDTO message, WebSocket client,
			Map<Integer, WebSocket> clients) {
		var ackResponseDTO = new AckResponseDTO();
		ackResponseDTO.setHandler(message.getHandler());
		try {
			userRepository.createUser(message.getUsername(), message.getPassword());
			authMessageExecutor.handleMessage(message, client, clients);
			ackResponseDTO.setOk(true);
		}
		catch (Exception ex) {
			ackResponseDTO.setOk(false);
		}
		return Optional.of(new Broadcast<>(ackResponseDTO, Collections.singletonList(client)));
	}

}
