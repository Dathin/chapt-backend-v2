package me.pedrocaires.chapt.handler.message.direct;

import me.pedrocaires.chapt.handler.Broadcast;
import me.pedrocaires.chapt.handler.message.Message;
import org.java_websocket.WebSocket;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Component
public class DirectMessage implements Message<DirectResponseDTO, DirectResponseDTO> {

	@Override
	public Optional<Broadcast<DirectResponseDTO>> handleMessage(DirectResponseDTO message, WebSocket webSocket,
			Map<String, WebSocket> clients) {
		return Optional.of(new Broadcast(message, Collections.singletonList(clients.get(message.getTo()))));
	}

}
