package me.pedrocaires.chapt.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import me.pedrocaires.chapt.authentication.AuthenticationFilter;
import me.pedrocaires.chapt.exception.UnexpectedException;
import me.pedrocaires.chapt.handler.message.MessageExecutor;
import me.pedrocaires.chapt.handler.message.auth.AuthMessageHandler;
import me.pedrocaires.chapt.handler.message.auth.AuthRequestDTO;
import me.pedrocaires.chapt.handler.message.auth.AuthResponseDTO;
import me.pedrocaires.chapt.handler.message.direct.DirectMessageHandler;
import me.pedrocaires.chapt.handler.message.direct.DirectDTO;
import me.pedrocaires.chapt.handler.transformer.BroadcastToSerializableBroadcast;
import org.java_websocket.WebSocket;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class MessageHandlerDecider {

	private final MessageExecutor messageExecutor;

	private final AuthMessageHandler authMessageHandler;

	private final DirectMessageHandler directMessageHandler;

	private final ObjectMapper objectMapper;

	private final AuthenticationFilter authenticationFilter;

	private final BroadcastToSerializableBroadcast broadcastToSerializableBroadcast;

	public MessageHandlerDecider(MessageExecutor messageExecutor, AuthMessageHandler authMessageHandler,
			DirectMessageHandler directMessageHandler, ObjectMapper objectMapper,
			AuthenticationFilter authenticationFilter,
			BroadcastToSerializableBroadcast broadcastToSerializableBroadcast) {
		this.messageExecutor = messageExecutor;
		this.authMessageHandler = authMessageHandler;
		this.directMessageHandler = directMessageHandler;
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
			return messageExecutor.execute(authMessageHandler, message, client, clients, AuthRequestDTO.class,
					AuthResponseDTO.class);
			// var parsedMessage = objectMapper.readValue(message, AuthRequestDTO.class);
			// var messageResponse = authMessage.handleMessage(parsedMessage, client,
			// clients);
			// if (messageResponse.isPresent()) {
			// return broadcastToSerializableBroadcast.transform(messageResponse);
			// }
			// return Optional.empty();
		}
		else if (handlerEnum == Handler.DIRECT) {
			return messageExecutor.execute(directMessageHandler, message, client, clients, DirectDTO.class,
					DirectDTO.class);
			// var parsedMessage = objectMapper.readValue(message, DirectDTO.class);
			// var messageResponse = directMessageHandler.handleMessage(parsedMessage,
			// client, clients);
			// if (messageResponse.isPresent()) {
			// return broadcastToSerializableBroadcast.transform(messageResponse);
			// }
			// return Optional.empty();
		}
		throw new UnexpectedException();
	}

}
