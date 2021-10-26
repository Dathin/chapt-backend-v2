package me.pedrocaires.chapt.handler.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.pedrocaires.chapt.handler.BaseMessageDTO;
import me.pedrocaires.chapt.handler.SerializableBroadcast;
import me.pedrocaires.chapt.handler.transformer.BroadcastToSerializableBroadcast;
import org.java_websocket.WebSocket;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class MessageExecutor {

	private final ObjectMapper objectMapper;

	private final BroadcastToSerializableBroadcast broadcastToSerializableBroadcast;

	public MessageExecutor(ObjectMapper objectMapper,
			BroadcastToSerializableBroadcast broadcastToSerializableBroadcast) {
		this.objectMapper = objectMapper;
		this.broadcastToSerializableBroadcast = broadcastToSerializableBroadcast;
	}

	public <I extends BaseMessageDTO, O extends BaseMessageDTO> Optional<SerializableBroadcast> execute(
			MessageHandler<I, O> messageHandler, String message, WebSocket client, Map<String, WebSocket> clients,
			Class<I> iClass, Class<O> oClass) throws JsonProcessingException {
		var parsedMessage = (I) objectMapper.readValue(message, iClass);
		var messageResponse = messageHandler.handleMessage(parsedMessage, client, clients);
		if (messageResponse.isPresent()) {
			return broadcastToSerializableBroadcast.transform(messageResponse);
		}
		return Optional.empty();
	}

}
