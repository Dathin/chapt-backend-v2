package me.pedrocaires.chapt.handler.message.direct;

import org.java_websocket.WebSocket;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class DirectMessageTest {

	@Mock
	WebSocket client;

	@InjectMocks
	DirectMessage directMessage;

	@Test
	void shouldReturnUserToBroadcastMessage() {
		var directDTO = new DirectDTO();
		directDTO.setTo("user123");

		var response = directMessage.handleMessage(directDTO, client, Collections.singletonMap("user123", client))
				.get();

		assertEquals(directDTO, response.getMessage());
		assertEquals(1, response.getClients().size());
	}

	@Test
	void shouldNotBroadcastToAnyoneIfNoAuthenticatedUsers() {
		var directDTO = new DirectDTO();
		directDTO.setTo("user123");

		var response = directMessage.handleMessage(directDTO, client, Collections.emptyMap()).get();

		assertEquals(0, response.getClients().size());
	}

}