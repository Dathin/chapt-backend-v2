package me.pedrocaires.chapt.handler.message.direct;

import me.pedrocaires.chapt.handler.Broadcast;
import me.pedrocaires.chapt.handler.message.MessageHandler;
import org.java_websocket.WebSocket;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Component
public class DirectMessageHandler implements MessageHandler<DirectDTO, DirectDTO> {

	@Override
	public Optional<Broadcast<DirectDTO>> handleMessage(DirectDTO message, WebSocket webSocket,
			Map<String, WebSocket> clients) {
		var receipting = clients.get(message.getTo());
		var clientsToBroadcast = receipting == null ? Collections.emptyList() : Collections.singletonList(receipting);
		return Optional.of(new Broadcast(message, clientsToBroadcast));
	}

}
