package me.pedrocaires.chapt;

import com.fasterxml.jackson.core.JsonProcessingException;
import me.pedrocaires.chapt.authentication.WebSocketAttachment;
import me.pedrocaires.chapt.exception.UnexpectedException;
import me.pedrocaires.chapt.handler.MessageHandlerDecider;
import org.java_websocket.WebSocket;
import org.java_websocket.WebSocketImpl;
import org.java_websocket.WebSocketListener;
import org.java_websocket.handshake.ClientHandshake;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.InetSocketAddress;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SimpleServerTest {

	@Mock
	MessageHandlerDecider messageHandlerDecider;

	@Mock
	InetSocketAddress inetSocketAddress;

	@Mock
	WebSocket client;

	@Mock
	ClientHandshake clientHandshake;

	@Mock
	WebSocketListener webSocketListener;

	@InjectMocks
	SimpleServer simpleServer;

	@BeforeEach
	void beforeEach() {
		client = new WebSocketImpl(webSocketListener, Arrays.asList());
	}

	@Test
	void shouldSetAttachmentOnOpen() {
		simpleServer.onOpen(client, clientHandshake);

		assertNotNull(client.getAttachment());
	}

	@Test
	void shouldThrowWhenSomethingHappenOnMessage() throws JsonProcessingException {
		when(messageHandlerDecider.decide(any(), any(), any())).thenThrow(JsonProcessingException.class);

		assertThrows(UnexpectedException.class, () -> simpleServer.onMessage(client, "message"));
	}

	@Test
	void shouldOnMessage() {
		simpleServer.onMessage(client, "message");
	}

	@Test
	void shouldOnError() {
		simpleServer.onError(client, new RuntimeException());
	}

	@Test
	void shouldOnStart() {
		simpleServer.onStart();
	}

	@Test
	void shouldOnClose() {
		simpleServer.onClose(client, 1, "", false);
	}
}