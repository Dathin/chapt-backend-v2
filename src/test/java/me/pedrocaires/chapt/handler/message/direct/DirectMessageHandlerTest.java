package me.pedrocaires.chapt.handler.message.direct;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.pedrocaires.chapt.handler.transformer.BroadcastToSerializableBroadcast;
import org.java_websocket.WebSocket;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class DirectMessageHandlerTest {

	@Mock
	WebSocket client;

	@Mock
	ObjectMapper objectMapper;

	@Mock
	BroadcastToSerializableBroadcast broadcastToSerializableBroadcast;

	@InjectMocks
	DirectMessageExecutor directMessageExecutor;

	@Test
	void shouldReturnUserToBroadcastMessage() {
		var directDTO = new DirectDTO();
		directDTO.setTo("user123");

		var response = directMessageExecutor
				.handleMessage(directDTO, client, Collections.singletonMap("user123", client)).get();

		assertEquals(directDTO, response.getMessage());
		assertEquals(1, response.getClients().size());
	}

	@Test
	void shouldNotBroadcastToAnyoneIfNoAuthenticatedUsers() {
		var directDTO = new DirectDTO();
		directDTO.setTo("user123");

		var response = directMessageExecutor.handleMessage(directDTO, client, Collections.emptyMap()).get();

		assertEquals(0, response.getClients().size());
	}

}