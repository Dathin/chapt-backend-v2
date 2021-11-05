package me.pedrocaires.chapt.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import me.pedrocaires.chapt.authentication.AuthenticationFilter;
import me.pedrocaires.chapt.exception.UnexpectedException;
import me.pedrocaires.chapt.handler.message.auth.AuthMessageExecutor;
import me.pedrocaires.chapt.handler.message.createuser.CreateUserMessageExecutor;
import me.pedrocaires.chapt.handler.message.direct.DirectMessageExecutor;
import me.pedrocaires.chapt.handler.message.history.HistoryMessageExecutor;
import org.java_websocket.WebSocket;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MessageHandlerDeciderTest {

	@Mock
	AuthMessageExecutor authMessageExecutor;

	@Mock
	DirectMessageExecutor directMessageExecutor;

	@Mock
	CreateUserMessageExecutor createUserMessageExecutor;

	@Mock
	HistoryMessageExecutor historyMessageExecutor;

	@Mock
	AuthenticationFilter authenticationFilter;

	@Mock
	ObjectMapper objectMapper;

	@Mock
	ObjectNode objectNode;

	@Mock
	WebSocket client;

	@Mock
	JsonNode jsonNode;

	@InjectMocks
	MessageHandlerDecider messageHandlerDecider;

	@Test
	void shouldThrowUnexpectedExceptionWhenHandleIsNull() throws JsonProcessingException {
		var message = "";
		when(objectMapper.readValue(message, ObjectNode.class)).thenReturn(objectNode);
		when(objectNode.get("handler")).thenReturn(null);

		assertThrows(UnexpectedException.class,
				() -> messageHandlerDecider.decide(message, client, Collections.singletonMap(1, client)));
	}

	@Test
	void shouldFilterRequests() throws JsonProcessingException {
		var message = "";
		when(objectMapper.readValue(message, ObjectNode.class)).thenReturn(objectNode);
		when(objectNode.get("handler")).thenReturn(jsonNode);
		when(jsonNode.toString()).thenReturn("\"AUTH\"");

		messageHandlerDecider.decide(message, client, Collections.singletonMap(1, client));

		verify(authenticationFilter).doFilter(any(), any());
	}

	@Test
	void shouldThrowUnexpectedExceptionWhenUnknownHandle() throws JsonProcessingException {
		var message = "";
		when(objectMapper.readValue(message, ObjectNode.class)).thenReturn(objectNode);
		when(objectNode.get("handler")).thenReturn(jsonNode);
		when(jsonNode.toString()).thenReturn("\"NOTSURE\"");

		assertThrows(UnexpectedException.class,
				() -> messageHandlerDecider.decide(message, client, Collections.singletonMap(1, client)));
	}

	@Test
	void shouldCallAuthExecutor() throws JsonProcessingException {
		var message = "";
		var clients = Collections.singletonMap(1, client);
		when(objectMapper.readValue(message, ObjectNode.class)).thenReturn(objectNode);
		when(objectNode.get("handler")).thenReturn(jsonNode);
		when(jsonNode.toString()).thenReturn("\"AUTH\"");

		messageHandlerDecider.decide(message, client, clients);

		verify(authMessageExecutor).execute(message, client, clients);
	}

	@Test
	void shouldCallDirectExecutor() throws JsonProcessingException {
		var message = "";
		var clients = Collections.singletonMap(1, client);
		when(objectMapper.readValue(message, ObjectNode.class)).thenReturn(objectNode);
		when(objectNode.get("handler")).thenReturn(jsonNode);
		when(jsonNode.toString()).thenReturn("\"DIRECT\"");

		messageHandlerDecider.decide(message, client, clients);

		verify(directMessageExecutor).execute(message, client, clients);
	}

	@Test
	void shouldCallCreateUserExecutor() throws JsonProcessingException {
		var message = "";
		var clients = Collections.singletonMap(1, client);
		when(objectMapper.readValue(message, ObjectNode.class)).thenReturn(objectNode);
		when(objectNode.get("handler")).thenReturn(jsonNode);
		when(jsonNode.toString()).thenReturn("\"CREATE_USER\"");

		messageHandlerDecider.decide(message, client, clients);

		verify(createUserMessageExecutor).execute(message, client, clients);
	}

	@Test
	void shouldCallHistoryMessageExecutor() throws JsonProcessingException {
		var message = "";
		var clients = Collections.singletonMap(1, client);
		when(objectMapper.readValue(message, ObjectNode.class)).thenReturn(objectNode);
		when(objectNode.get("handler")).thenReturn(jsonNode);
		when(jsonNode.toString()).thenReturn("\"HISTORY\"");

		messageHandlerDecider.decide(message, client, clients);

		verify(historyMessageExecutor).execute(message, client, clients);
	}

}