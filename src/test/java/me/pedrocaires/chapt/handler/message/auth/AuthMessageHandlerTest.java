package me.pedrocaires.chapt.handler.message.auth;

import me.pedrocaires.chapt.authentication.Authentication;
import me.pedrocaires.chapt.authentication.WebSocketAttachment;
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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthMessageHandlerTest {

	WebSocketImpl client;

	WebSocketAttachment webSocketAttachment;

	@Mock
	WebSocketListener webSocketListener;

	@Mock
	Authentication authentication;

	@Mock
	Map<String, WebSocket> clients;

	@InjectMocks
	AuthMessageHandler authMessageHandler;

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

		authMessageHandler.handleMessage(authRequest, client, clients);

		verify(authentication).setAuthenticated(true);
	}

	@Test
	void shouldPutClientAtListOfAuthenticatedClients() {
		var authRequest = new AuthRequestDTO();
		var username = "pedro";
		authRequest.setUsername(username);
		authRequest.setPassword("caires");

		authMessageHandler.handleMessage(authRequest, client, clients);

		verify(clients).put(username, client);
	}

	@Test
	void shouldNotPersistAtUserContextWhenAuthenticated() {
		var authRequest = new AuthRequestDTO();
		authRequest.setUsername("not_pedro");
		authRequest.setPassword("caires");

		authMessageHandler.handleMessage(authRequest, client, clients);

		verify(authentication).setAuthenticated(false);
	}

	@Test
	void shouldNotPutClientAtListOfAuthenticatedClients() {
		var authRequest = new AuthRequestDTO();
		var username = "not_pedro";
		authRequest.setUsername(username);
		authRequest.setPassword("caires");

		authMessageHandler.handleMessage(authRequest, client, clients);

		verify(clients, never()).put(any(), any());
	}

	@Test
	void shouldReturnAuthenticatedResponse() {
		var authRequest = new AuthRequestDTO();
		authRequest.setUsername("pedro");
		authRequest.setPassword("caires");

		var authResponse = authMessageHandler.handleMessage(authRequest, client, clients).get();

		assertTrue(authResponse.getMessage().isOk());
		assertTrue(authResponse.getClients().contains(client));
	}

	@Test
	void shouldReturnUnauthenticatedResponse() {
		var authRequest = new AuthRequestDTO();
		authRequest.setUsername("not_pedro");
		authRequest.setPassword("caires");

		var authResponse = authMessageHandler.handleMessage(authRequest, client, clients).get();

		assertFalse(authResponse.getMessage().isOk());
		assertTrue(authResponse.getClients().contains(client));
	}

}