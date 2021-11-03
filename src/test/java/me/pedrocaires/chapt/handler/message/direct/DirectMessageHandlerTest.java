package me.pedrocaires.chapt.handler.message.direct;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.pedrocaires.chapt.authentication.WebSocketAttachment;
import me.pedrocaires.chapt.handler.transformer.BroadcastToSerializableBroadcast;
import me.pedrocaires.chapt.repository.message.MessageRepository;
import org.java_websocket.WebSocket;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DirectMessageHandlerTest {

	@Mock
	WebSocket client;

	@Mock
	WebSocket toClient;

	@Mock
	ObjectMapper objectMapper;

	@Mock
	MessageRepository messageRepository;

	@Mock
	BroadcastToSerializableBroadcast broadcastToSerializableBroadcast;

	@InjectMocks
	DirectMessageExecutor directMessageExecutor;

	@Test
	void shouldBroadcastMessageToUserAndSelfIfOnline() {
		var directDTO = new DirectRequestDTO();
		directDTO.setTo(1);
		when(client.getAttachment()).thenReturn(new WebSocketAttachment());

		var response = directMessageExecutor.handleMessage(directDTO, client, Collections.singletonMap(1, toClient))
				.get();

		assertTrue(response.getClients().contains(client));
		assertTrue(response.getClients().contains(toClient));
	}

	@Test
	void shouldBroadcastMessageToSelfIfOffline() {
		var directDTO = new DirectRequestDTO();
		directDTO.setTo(2);
		when(client.getAttachment()).thenReturn(new WebSocketAttachment());

		var response = directMessageExecutor.handleMessage(directDTO, client, Collections.singletonMap(1, client))
				.get();

		assertTrue(response.getClients().contains(client));
		assertEquals(1, response.getClients().size());
	}

	@Test
	void shouldReturnBroadcastData() {
		var directDTO = new DirectRequestDTO();
		directDTO.setTo(1);
		directDTO.setContent("");
		directDTO.setHandler("HANDLER");
		when(client.getAttachment()).thenReturn(new WebSocketAttachment());
		when(messageRepository.insertMessage(directDTO.getTo(), 0, directDTO.getContent())).thenReturn(10);

		var response = directMessageExecutor.handleMessage(directDTO, client, Collections.singletonMap(1, toClient))
				.get();

		assertEquals(directDTO.getHandler(), response.getMessage().getHandler());
		assertEquals(directDTO.getTo(), response.getMessage().getTo());
		assertEquals(directDTO.getContent(), response.getMessage().getContent());
		assertEquals(10, response.getMessage().getMessageId());
	}

	@Test
	void shouldPersistMessageInDatabase() {
		var directDTO = new DirectRequestDTO();
		directDTO.setTo(1);
		when(client.getAttachment()).thenReturn(new WebSocketAttachment());

		var response = directMessageExecutor.handleMessage(directDTO, client, Collections.singletonMap(1, toClient))
				.get();

		verify(messageRepository).insertMessage(directDTO.getTo(), 0, directDTO.getContent());
	}

}