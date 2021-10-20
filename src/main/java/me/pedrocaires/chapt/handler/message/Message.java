package me.pedrocaires.chapt.handler.message;

import me.pedrocaires.chapt.handler.BaseMessageDTO;
import me.pedrocaires.chapt.handler.Broadcast;
import org.java_websocket.WebSocket;

import java.util.Map;
import java.util.Optional;

public interface Message<REQ extends BaseMessageDTO, RES extends BaseMessageDTO> {

	Optional<Broadcast<RES>> handleMessage(REQ message, WebSocket webSocket, Map<String, WebSocket> clients);

}
