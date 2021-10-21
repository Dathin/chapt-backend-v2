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

	WebSocketImpl webSocket;

	WebSocketAttachment webSocketAttachment;

	@BeforeEach
	void beforeEach() {
		webSocket = new WebSocketImpl(webSocketListener, Arrays.asList());
		webSocketAttachment = new WebSocketAttachment();
		webSocket.setAttachment(webSocketAttachment);
	}

	@Test
	void shouldPassAuthenticationFilter() {
		webSocketAttachment.getAuthenticationFilter().setAuthenticated(true);

		assertDoesNotThrow(() -> authenticationFilter.doFilter(webSocket, Handler.DIRECT));
	}

	@Test
	void shouldPassAuthenticationFilterWhenTryingTo() {
		webSocketAttachment.getAuthenticationFilter().setAuthenticated(false);

		assertDoesNotThrow(() -> authenticationFilter.doFilter(webSocket, Handler.AUTH));
	}

	@Test
	void shouldThrowWhenNotAuthenticatedAndNotTryingTo() {
		webSocketAttachment.getAuthenticationFilter().setAuthenticated(false);

		assertThrows(UnauthenticatedException.class, () -> authenticationFilter.doFilter(webSocket, Handler.DIRECT));
	}

}