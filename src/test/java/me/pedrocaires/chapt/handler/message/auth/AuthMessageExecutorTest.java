package me.pedrocaires.chapt.handler.message.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.pedrocaires.chapt.authentication.Authentication;
import me.pedrocaires.chapt.authentication.WebSocketAttachment;
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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
	Map<String, WebSocket> clients;

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
		var authRequest = new AuthRequestDTO();
		authRequest.setUsername("pedro");
		authRequest.setPassword("caires");
		when(userRepository.findUserByUsernameAndPassword(authRequest.getUsername(), authRequest.getPassword()))
				.thenReturn(Optional.of(user));

		authMessageExecutor.handleMessage(authRequest, client, clients);

		verify(authentication).setAuthenticated(true);
	}

	@Test
	void shouldPutClientAtListOfAuthenticatedClients() {
		var authRequest = new AuthRequestDTO();
		var username = "pedro";
		authRequest.setUsername(username);
		authRequest.setPassword("caires");
		when(userRepository.findUserByUsernameAndPassword(authRequest.getUsername(), authRequest.getPassword()))
				.thenReturn(Optional.of(user));

		authMessageExecutor.handleMessage(authRequest, client, clients);

		verify(clients).put(username, client);
	}

	@Test
	void shouldNotPersistAtUserContextWhenAuthenticated() {
		var authRequest = new AuthRequestDTO();
		authRequest.setUsername("not_pedro");
		authRequest.setPassword("caires");
		when(userRepository.findUserByUsernameAndPassword(authRequest.getUsername(), authRequest.getPassword()))
				.thenReturn(Optional.empty());

		authMessageExecutor.handleMessage(authRequest, client, clients);

		verify(authentication).setAuthenticated(false);
	}

	@Test
	void shouldNotPutClientAtListOfAuthenticatedClients() {
		var authRequest = new AuthRequestDTO();
		var username = "not_pedro";
		authRequest.setUsername(username);
		authRequest.setPassword("caires");
		when(userRepository.findUserByUsernameAndPassword(authRequest.getUsername(), authRequest.getPassword()))
				.thenReturn(Optional.empty());

		authMessageExecutor.handleMessage(authRequest, client, clients);

		verify(clients, never()).put(any(), any());
	}

	@Test
	void shouldReturnAuthenticatedResponse() {
		var authRequest = new AuthRequestDTO();
		authRequest.setUsername("pedro");
		authRequest.setPassword("caires");
		when(userRepository.findUserByUsernameAndPassword(authRequest.getUsername(), authRequest.getPassword()))
				.thenReturn(Optional.of(user));

		var authResponse = authMessageExecutor.handleMessage(authRequest, client, clients).get();

		assertTrue(authResponse.getMessage().isOk());
		assertTrue(authResponse.getClients().contains(client));
	}

	@Test
	void shouldReturnUnauthenticatedResponse() {
		var authRequest = new AuthRequestDTO();
		authRequest.setUsername("not_pedro");
		authRequest.setPassword("caires");
		when(userRepository.findUserByUsernameAndPassword(authRequest.getUsername(), authRequest.getPassword()))
				.thenReturn(Optional.empty());

		var authResponse = authMessageExecutor.handleMessage(authRequest, client, clients).get();

		assertFalse(authResponse.getMessage().isOk());
		assertTrue(authResponse.getClients().contains(client));
	}

}