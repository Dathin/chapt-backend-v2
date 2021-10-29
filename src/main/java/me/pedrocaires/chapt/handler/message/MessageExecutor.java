package me.pedrocaires.chapt.handler.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.pedrocaires.chapt.handler.BaseMessageDTO;
import me.pedrocaires.chapt.handler.Broadcast;
import me.pedrocaires.chapt.handler.SerializableBroadcast;
import me.pedrocaires.chapt.handler.transformer.BroadcastToSerializableBroadcast;
import org.java_websocket.WebSocket;

import java.lang.reflect.ParameterizedType;
import java.util.Map;
import java.util.Optional;

public abstract class MessageExecutor<I extends BaseMessageDTO, O extends BaseMessageDTO> {

	private final Class<I> iClass;

	private final ObjectMapper objectMapper;

	private final BroadcastToSerializableBroadcast broadcastToSerializableBroadcast;

	protected MessageExecutor(ObjectMapper objectMapper,
			BroadcastToSerializableBroadcast broadcastToSerializableBroadcast) {
		this.iClass = ((Class<I>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
		this.objectMapper = objectMapper;
		this.broadcastToSerializableBroadcast = broadcastToSerializableBroadcast;
	}

	public abstract Optional<Broadcast<O>> handleMessage(I message, WebSocket client, Map<Integer, WebSocket> clients);

	public Optional<SerializableBroadcast> execute(String message, WebSocket client, Map<Integer, WebSocket> clients)
			throws JsonProcessingException {
		var parsedMessage = objectMapper.readValue(message, iClass);
		var messageResponse = handleMessage(parsedMessage, client, clients);
		if (messageResponse.isPresent()) {
			return broadcastToSerializableBroadcast.transform(messageResponse);
		}
		return Optional.empty();
	}

}
