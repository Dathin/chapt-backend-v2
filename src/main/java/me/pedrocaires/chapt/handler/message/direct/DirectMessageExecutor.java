package me.pedrocaires.chapt.handler.message.direct;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.pedrocaires.chapt.handler.Broadcast;
import me.pedrocaires.chapt.handler.message.MessageExecutor;
import me.pedrocaires.chapt.handler.transformer.BroadcastToSerializableBroadcast;
import org.java_websocket.WebSocket;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Component
public class DirectMessageExecutor extends MessageExecutor<DirectDTO, DirectDTO> {

	public DirectMessageExecutor(ObjectMapper objectMapper,
			BroadcastToSerializableBroadcast broadcastToSerializableBroadcast) {
		super(objectMapper, broadcastToSerializableBroadcast);
	}

	@Override
	public Optional<Broadcast<DirectDTO>> handleMessage(DirectDTO message, WebSocket client,
			Map<String, WebSocket> clients) {
		var receipting = clients.get(message.getTo());
		Collection<WebSocket> clientsToBroadcast = receipting == null ? Collections.emptyList() : Collections.singletonList(receipting);
		return Optional.of(new Broadcast<>(message, clientsToBroadcast));
	}

}
