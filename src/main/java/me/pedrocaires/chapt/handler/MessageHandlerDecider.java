package me.pedrocaires.chapt.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import me.pedrocaires.chapt.authentication.AuthenticationFilter;
import me.pedrocaires.chapt.exception.UnexpectedException;
import me.pedrocaires.chapt.handler.message.auth.AuthMessageExecutor;
import me.pedrocaires.chapt.handler.message.createuser.CreateUserMessageExecutor;
import me.pedrocaires.chapt.handler.message.direct.DirectMessageExecutor;
import org.java_websocket.WebSocket;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class MessageHandlerDecider {

	private final AuthMessageExecutor authMessageExecutor;

	private final DirectMessageExecutor directMessageExecutor;

	private final CreateUserMessageExecutor createUserMessageExecutor;

	private final ObjectMapper objectMapper;

	private final AuthenticationFilter authenticationFilter;

	public MessageHandlerDecider(AuthMessageExecutor authMessageExecutor, DirectMessageExecutor directMessageExecutor,
			CreateUserMessageExecutor createUserMessageExecutor, ObjectMapper objectMapper,
			AuthenticationFilter authenticationFilter) {
		this.authMessageExecutor = authMessageExecutor;
		this.directMessageExecutor = directMessageExecutor;
		this.createUserMessageExecutor = createUserMessageExecutor;
		this.objectMapper = objectMapper;
		this.authenticationFilter = authenticationFilter;
	}

	public Optional<SerializableBroadcast> decide(String message, WebSocket client, Map<String, WebSocket> clients)
			throws JsonProcessingException {
		var objectMessage = objectMapper.readValue(message, ObjectNode.class);
		var handler = objectMessage.get("handler");
		if (handler == null) {
			throw new UnexpectedException();
		}
		String handlerString = handler.toString();
		var handlerEnum = Handler.fromString(handlerString.replace("\"", ""));
		authenticationFilter.doFilter(client, handlerEnum);
		if (handlerEnum == Handler.AUTH) {
			return authMessageExecutor.execute(message, client, clients);
		}
		else if (handlerEnum == Handler.CREATE_USER) {
			return createUserMessageExecutor.execute(message, client, clients);
		}
		else if (handlerEnum == Handler.DIRECT) {
			return directMessageExecutor.execute(message, client, clients);
		}
		throw new UnexpectedException();
	}

}
