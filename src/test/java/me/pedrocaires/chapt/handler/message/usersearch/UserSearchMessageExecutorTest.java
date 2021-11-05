package me.pedrocaires.chapt.handler.message.usersearch;

import me.pedrocaires.chapt.handler.message.history.HistoryRequestDTO;
import me.pedrocaires.chapt.repository.message.Message;
import me.pedrocaires.chapt.repository.user.User;
import me.pedrocaires.chapt.repository.user.UserRepository;
import org.java_websocket.WebSocket;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserSearchMessageExecutorTest {

	@Mock
	WebSocket client;

	@Mock
	UserRepository userRepository;

	@InjectMocks
	UserSearchMessageExecutor userSearchMessageExecutor;

	@Test
	void shouldBroadcastMessageToSelf() {
		var userSearchRequest = new UserSearchRequestDTO();
		userSearchRequest.setPage(1);
		userSearchRequest.setSize(1);
		userSearchRequest.setHandler("");

		var response = userSearchMessageExecutor
				.handleMessage(userSearchRequest, client, Collections.singletonMap(1, client)).get();

		assertTrue(response.getClients().contains(client));
		assertEquals(1, response.getClients().size());
	}

	@Test
	void shouldReturnBroadcastData() {
		List<User> users = Collections.emptyList();
		var handler = "";
		var userSearchRequest = new UserSearchRequestDTO();
		userSearchRequest.setPage(1);
		userSearchRequest.setSize(1);
		userSearchRequest.setHandler(handler);

		var response = userSearchMessageExecutor
				.handleMessage(userSearchRequest, client, Collections.singletonMap(1, client)).get();

		assertTrue(response.getClients().contains(client));
		assertEquals(1, response.getClients().size());

		assertEquals(users, response.getMessage().getUsers());
		assertEquals(handler, response.getMessage().getHandler());
	}

}