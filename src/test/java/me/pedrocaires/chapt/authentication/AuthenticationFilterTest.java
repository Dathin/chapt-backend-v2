package me.pedrocaires.chapt.authentication;

import me.pedrocaires.chapt.exception.UnauthenticatedException;
import me.pedrocaires.chapt.handler.Handler;
import org.java_websocket.WebSocketImpl;
import org.java_websocket.WebSocketListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class AuthenticationFilterTest {

	@Mock
	WebSocketListener webSocketListener;

	@InjectMocks
	AuthenticationFilter authenticationFilter;

	WebSocketImpl client;

	WebSocketAttachment webSocketAttachment;

	@BeforeEach
	void beforeEach() {
		client = new WebSocketImpl(webSocketListener, Arrays.asList());
		webSocketAttachment = new WebSocketAttachment();
		client.setAttachment(webSocketAttachment);
	}

	@Test
	void shouldPassAuthenticationFilter() {
		webSocketAttachment.getAuthentication().setAuthenticated(true);

		assertDoesNotThrow(() -> authenticationFilter.doFilter(client, Handler.DIRECT));
	}

	@Test
	void shouldPassAuthenticationFilterWhenTryingToAuhenticate() {
		webSocketAttachment.getAuthentication().setAuthenticated(false);

		assertDoesNotThrow(() -> authenticationFilter.doFilter(client, Handler.AUTH));
	}

	@Test
	void shouldPassAuthenticationFilterWhenTryingToRegister() {
		webSocketAttachment.getAuthentication().setAuthenticated(false);

		assertDoesNotThrow(() -> authenticationFilter.doFilter(client, Handler.CREATE_USER));
	}

	@Test
	void shouldThrowWhenNotAuthenticatedAndNotTryingTo() {
		webSocketAttachment.getAuthentication().setAuthenticated(false);

		assertThrows(UnauthenticatedException.class, () -> authenticationFilter.doFilter(client, Handler.DIRECT));
	}

}