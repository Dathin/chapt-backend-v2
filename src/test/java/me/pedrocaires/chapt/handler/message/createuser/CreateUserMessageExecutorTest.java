package me.pedrocaires.chapt.handler.message.createuser;

import me.pedrocaires.chapt.handler.message.auth.AuthMessageExecutor;
import me.pedrocaires.chapt.handler.message.shareddto.UserInfoDTO;
import me.pedrocaires.chapt.repository.user.UserRepository;
import org.java_websocket.WebSocket;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CreateUserMessageExecutorTest {

	@Mock
	WebSocket client;

	@Mock
	UserRepository userRepository;

	@Mock
	AuthMessageExecutor authMessageExecutor;

	@InjectMocks
	CreateUserMessageExecutor createUserMessageExecutor;

	@Test
	void shouldReturnOkAckResponse() {
		var userInfoDTO = new UserInfoDTO();

		var ackResponse = createUserMessageExecutor
				.handleMessage(userInfoDTO, client, Collections.singletonMap(1, client)).get();

		assertTrue(ackResponse.getMessage().isOk());
		assertTrue(ackResponse.getClients().contains(client));
	}

	@Test
	void shouldAuthenticateAfterCreatingUser() {
		var userInfoDTO = new UserInfoDTO();
		var clients = Collections.singletonMap(1, client);

		var ackResponse = createUserMessageExecutor.handleMessage(userInfoDTO, client, clients).get();

		verify(authMessageExecutor).handleMessage(userInfoDTO, client, clients);
	}

	@Test
	void shouldReturnNotOkAckResponse() {
		var userInfoDTO = new UserInfoDTO();
		doThrow(RuntimeException.class).when(userRepository).createUser(userInfoDTO.getUsername(),
				userInfoDTO.getPassword());

		var ackResponse = createUserMessageExecutor
				.handleMessage(userInfoDTO, client, Collections.singletonMap(1, client)).get();

		assertFalse(ackResponse.getMessage().isOk());
		assertTrue(ackResponse.getClients().contains(client));
	}

}