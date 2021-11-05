package me.pedrocaires.chapt.handler.message.direct;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.pedrocaires.chapt.authentication.WebSocketAttachment;
import me.pedrocaires.chapt.handler.Broadcast;
import me.pedrocaires.chapt.handler.message.MessageExecutor;
import me.pedrocaires.chapt.handler.transformer.BroadcastToSerializableBroadcast;
import me.pedrocaires.chapt.repository.message.MessageRepository;
import org.java_websocket.WebSocket;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DirectMessageExecutor extends MessageExecutor<DirectRequestDTO, DirectResponseDTO> {

	private final MessageRepository messageRepository;

	public DirectMessageExecutor(ObjectMapper objectMapper,
			BroadcastToSerializableBroadcast broadcastToSerializableBroadcast, MessageRepository messageRepository) {
		super(objectMapper, broadcastToSerializableBroadcast);
		this.messageRepository = messageRepository;
	}

	@Override
	public Optional<Broadcast<DirectResponseDTO>> handleMessage(DirectRequestDTO message, WebSocket client,
			Map<Integer, WebSocket> clients) {
		var receipting = clients.get(message.getTo());
		Collection<WebSocket> clientsToBroadcast = receipting == null ? Collections.singletonList(client)
				: Arrays.asList(receipting, client);
		var userId = ((WebSocketAttachment) client.getAttachment()).getAuthentication().getUserId();
		var messageId = messageRepository.insertMessage(message.getTo(), userId, message.getContent());
		return Optional.of(new Broadcast<>(new DirectResponseDTO(message, messageId), clientsToBroadcast));
	}

}
