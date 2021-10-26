package me.pedrocaires.chapt.handler.message;

import me.pedrocaires.chapt.handler.BaseMessageDTO;
import me.pedrocaires.chapt.handler.Broadcast;
import org.java_websocket.WebSocket;

import java.util.Map;
import java.util.Optional;

public interface MessageHandler<I extends BaseMessageDTO, O extends BaseMessageDTO> {

	Optional<Broadcast<O>> handleMessage(I message, WebSocket webSocket, Map<String, WebSocket> clients);

}
