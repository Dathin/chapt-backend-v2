package me.pedrocaires.chapt;

import com.fasterxml.jackson.core.JsonProcessingException;
import me.pedrocaires.chapt.authentication.WebSocketAttachment;
import me.pedrocaires.chapt.exception.UnexpectedException;
import me.pedrocaires.chapt.handler.MessageHandlerDecider;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.Hashtable;
import java.util.Map;

@Component
public class SimpleServer extends WebSocketServer {

	public static final Logger LOGGER = LoggerFactory.getLogger(SimpleServer.class);

	private final MessageHandlerDecider messageHandlerDecider;

	private final Map<Integer, WebSocket> authenticatedSessions = new Hashtable<>();

	public SimpleServer(InetSocketAddress address, MessageHandlerDecider messageHandlerDecider) {
		super(address);
		this.messageHandlerDecider = messageHandlerDecider;
	}

	@Override
	public void onOpen(WebSocket client, ClientHandshake handshake) {
		client.setAttachment(new WebSocketAttachment());
	}

	@Override
	public void onClose(WebSocket client, int code, String reason, boolean remote) {
		LOGGER.info(String.format("closed %s with exit code %s additional info: %s", client.getRemoteSocketAddress(),
				code, reason));
	}

	@Override
	public void onMessage(WebSocket client, String message) {
		LOGGER.info("Received message");
		try {
			var optionalBroadcast = messageHandlerDecider.decide(message, client, authenticatedSessions);
			optionalBroadcast.ifPresent(broadcast -> broadcast(broadcast.getMessage(), broadcast.getClients()));
			LOGGER.info("Broadcasted");
		}
		catch (JsonProcessingException ex) {
			throw new UnexpectedException();
		}
	}

	@Override
	public void onError(WebSocket client, Exception ex) {
		LOGGER.error("an error occurred", ex);
		client.close();
	}

	@Override
	public void onStart() {
		LOGGER.info("server started successfully");
	}

}
