package me.pedrocaires.chapt.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import me.pedrocaires.chapt.authentication.AuthenticationFilter;
import me.pedrocaires.chapt.exception.UnexpectedException;
import me.pedrocaires.chapt.handler.message.auth.AuthMessage;
import me.pedrocaires.chapt.handler.message.auth.AuthRequestDTO;
import me.pedrocaires.chapt.handler.message.direct.DirectMessage;
import me.pedrocaires.chapt.handler.message.direct.DirectDTO;
import me.pedrocaires.chapt.handler.transformer.BroadcastToSerializableBroadcast;
import org.java_websocket.WebSocket;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class MessageHandlerDecider {

	private final AuthMessage authMessage;

	private final DirectMessage directMessage;

	private final ObjectMapper objectMapper;

	private final AuthenticationFilter authenticationFilter;

	private final BroadcastToSerializableBroadcast broadcastToSerializableBroadcast;

	public MessageHandlerDecider(AuthMessage authMessage, DirectMessage directMessage, ObjectMapper objectMapper,
			AuthenticationFilter authenticationFilter,
			BroadcastToSerializableBroadcast broadcastToSerializableBroadcast) {
		this.authMessage = authMessage;
		this.directMessage = directMessage;
		this.objectMapper = objectMapper;
		this.authenticationFilter = authenticationFilter;
		this.broadcastToSerializableBroadcast = broadcastToSerializableBroadcast;
	}

	public Optional<SerializableBroadcast> decide(String message, WebSocket client, Map<String, WebSocket> clients)
			throws JsonProcessingException {
		var objectMessage = objectMapper.readValue(message, ObjectNode.class);
		var handler = objectMessage.get("handler");
		if (handler == null) {
			throw new UnexpectedException();
		}
		String handlerString = handler.toString();
		var handlerEnum = Handler.fromString(handlerString.replaceAll("\"", ""));
		authenticationFilter.doFilter(client, handlerEnum);
		if (handlerEnum == Handler.AUTH) {
			var parsedMessage = objectMapper.readValue(message, AuthRequestDTO.class);
			var messageResponse = authMessage.handleMessage(parsedMessage, client, clients);
			if (messageResponse.isPresent()) {
				return broadcastToSerializableBroadcast.transform(messageResponse);
			}
			return Optional.empty();
		}
		else if (handlerEnum == Handler.DIRECT) {
			var parsedMessage = objectMapper.readValue(message, DirectDTO.class);
			var messageResponse = directMessage.handleMessage(parsedMessage, client, clients);
			if (messageResponse.isPresent()) {
				return broadcastToSerializableBroadcast.transform(messageResponse);
			}
			return Optional.empty();
		}
		throw new UnexpectedException();
	}

}
