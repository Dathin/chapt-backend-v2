package me.pedrocaires.chapt.handler.message.history;

import me.pedrocaires.chapt.authentication.Authentication;
import me.pedrocaires.chapt.authentication.WebSocketAttachment;
import me.pedrocaires.chapt.repository.message.Message;
import me.pedrocaires.chapt.repository.message.MessageRepository;
import org.java_websocket.WebSocket;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HistoryMessageExecutorTest {

	@Mock
	WebSocket client;

	@Mock
	WebSocketAttachment webSocketAttachment;

	@Mock
	Authentication authentication;

	@Mock
	MessageRepository messageRepository;

	@InjectMocks
	HistoryMessageExecutor historyMessageExecutor;

	@Test
	void shouldBroadcastMessageToSelf() {
		var userId = 1;
		var historyRequest = new HistoryRequestDTO();
		historyRequest.setFrom(1);
		historyRequest.setSize(1);
		historyRequest.setPreviousThanId(1L);
		historyRequest.setHandler("");
		when(client.getAttachment()).thenReturn(webSocketAttachment);
		when(webSocketAttachment.getAuthentication()).thenReturn(authentication);
		when(authentication.getUserId()).thenReturn(userId);

		var response = historyMessageExecutor.handleMessage(historyRequest, client, Collections.singletonMap(1, client))
				.get();

		assertTrue(response.getClients().contains(client));
		assertEquals(1, response.getClients().size());
	}

	@Test
	void shouldReturnBroadcastData() {
		var userId = 1;
		var historyRequest = new HistoryRequestDTO();
		var handler = "";
		var from = 1;
		var size = 1;
		var previousThanId = 1L;
		historyRequest.setHandler(handler);
		historyRequest.setFrom(from);
		historyRequest.setSize(size);
		historyRequest.setPreviousThanId(previousThanId);
		var messageHistory = Arrays.asList(new Message());
		when(client.getAttachment()).thenReturn(webSocketAttachment);
		when(webSocketAttachment.getAuthentication()).thenReturn(authentication);
		when(authentication.getUserId()).thenReturn(userId);
		when(messageRepository.getMessageHistory(userId, from, previousThanId, size)).thenReturn(messageHistory);

		var response = historyMessageExecutor.handleMessage(historyRequest, client, Collections.singletonMap(1, client))
				.get();

		assertEquals(messageHistory, response.getMessage().getMessages());
		assertEquals(handler, response.getMessage().getHandler());
	}

}