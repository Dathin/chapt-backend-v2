package me.pedrocaires.chapt.handler.message.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.pedrocaires.chapt.authentication.Authentication;
import me.pedrocaires.chapt.authentication.WebSocketAttachment;
import me.pedrocaires.chapt.handler.message.shareddto.UserInfoDTO;
import me.pedrocaires.chapt.handler.transformer.BroadcastToSerializableBroadcast;
import me.pedrocaires.chapt.repository.user.User;
import me.pedrocaires.chapt.repository.user.UserRepository;
import org.java_websocket.WebSocket;
import org.java_websocket.WebSocketImpl;
import org.java_websocket.WebSocketListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthMessageExecutorTest {

	WebSocketImpl client;

	WebSocketAttachment webSocketAttachment;

	@Mock
	User user;

	@Mock
	UserRepository userRepository;

	@Mock
	ObjectMapper objectMapper;

	@Mock
	BroadcastToSerializableBroadcast broadcastToSerializableBroadcast;

	@Mock
	WebSocketListener webSocketListener;

	@Mock
	Authentication authentication;

	@Mock
	Map<Integer, WebSocket> clients;

	@InjectMocks
	AuthMessageExecutor authMessageExecutor;

	@BeforeEach
	void beforeEach() {
		client = new WebSocketImpl(webSocketListener, Arrays.asList());
		webSocketAttachment = new WebSocketAttachment(authentication);
		client.setAttachment(webSocketAttachment);
	}

	@Test
	void shouldPersistAtUserContextWhenAuthenticated() {
		var authRequest = new UserInfoDTO();
		authRequest.setUsername("pedro");
		authRequest.setPassword("caires");
		when(userRepository.findUserByUsernameAndPassword(authRequest.getUsername(), authRequest.getPassword()))
				.thenReturn(Optional.of(user));

		authMessageExecutor.handleMessage(authRequest, client, clients);

		verify(authentication).setAuthenticated(true);
	}

	@Test
	void shouldPutClientAtListOfAuthenticatedClients() {
		var authRequest = new UserInfoDTO();
		var username = "pedro";
		var userId = 1;
		authRequest.setUsername(username);
		authRequest.setPassword("caires");
		when(userRepository.findUserByUsernameAndPassword(authRequest.getUsername(), authRequest.getPassword()))
				.thenReturn(Optional.of(user));
		when(user.getId()).thenReturn(userId);

		authMessageExecutor.handleMessage(authRequest, client, clients);

		verify(clients).put(userId, client);
	}

	@Test
	void shouldNotPersistAtUserContextWhenAuthenticated() {
		var authRequest = new UserInfoDTO();
		authRequest.setUsername("not_pedro");
		authRequest.setPassword("caires");
		when(userRepository.findUserByUsernameAndPassword(authRequest.getUsername(), authRequest.getPassword()))
				.thenReturn(Optional.empty());

		authMessageExecutor.handleMessage(authRequest, client, clients);

		verify(authentication).setAuthenticated(false);
	}

	@Test
	void shouldNotPutClientAtListOfAuthenticatedClients() {
		var authRequest = new UserInfoDTO();
		var username = "not_pedro";
		authRequest.setUsername(username);
		authRequest.setPassword("caires");
		when(userRepository.findUserByUsernameAndPassword(authRequest.getUsername(), authRequest.getPassword()))
				.thenReturn(Optional.empty());

		authMessageExecutor.handleMessage(authRequest, client, clients);

		verify(clients, never()).put(any(), any());
	}

	@Test
	void shouldReturnOkAckResponse() {
		var authRequest = new UserInfoDTO();
		authRequest.setUsername("pedro");
		authRequest.setPassword("caires");
		authRequest.setHandler("AUTH");
		when(userRepository.findUserByUsernameAndPassword(authRequest.getUsername(), authRequest.getPassword()))
				.thenReturn(Optional.of(user));

		var ackResponse = authMessageExecutor.handleMessage(authRequest, client, clients).get();

		assertEquals(authRequest.getHandler(), ackResponse.getMessage().getHandler());
		assertTrue(ackResponse.getMessage().isOk());
		assertTrue(ackResponse.getClients().contains(client));
	}

	@Test
	void shouldReturnNotOkAckResponse() {
		var authRequest = new UserInfoDTO();
		authRequest.setUsername("not_pedro");
		authRequest.setPassword("caires");
		when(userRepository.findUserByUsernameAndPassword(authRequest.getUsername(), authRequest.getPassword()))
				.thenReturn(Optional.empty());

		var ackResponse = authMessageExecutor.handleMessage(authRequest, client, clients).get();

		assertFalse(ackResponse.getMessage().isOk());
		assertTrue(ackResponse.getClients().contains(client));
	}

}