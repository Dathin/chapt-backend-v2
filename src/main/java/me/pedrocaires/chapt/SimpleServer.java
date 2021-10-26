package me.pedrocaires.chapt;

import com.fasterxml.jackson.core.JsonProcessingException;
import me.pedrocaires.chapt.authentication.WebSocketAttachment;
import me.pedrocaires.chapt.exception.UnexpectedException;
import me.pedrocaires.chapt.handler.MessageHandlerDecider;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.Hashtable;
import java.util.Map;

@Component
public class SimpleServer extends WebSocketServer {

	private final MessageHandlerDecider messageHandlerDecider;

	private final Map<String, WebSocket> authenticatedSessions = new Hashtable<>();

	public SimpleServer(InetSocketAddress address, MessageHandlerDecider messageHandlerDecider) {
		super(address);
		this.messageHandlerDecider = messageHandlerDecider;
	}

	@Override
	public void onOpen(WebSocket client, ClientHandshake handshake) {
		// client.send("Welcome to the server!"); //This method sends a message to the new
		// client
		// broadcast( "new clientection: " + handshake.getResourceDescriptor() ); //This
		// method sends a message to all clients clientected
		client.setAttachment(new WebSocketAttachment());
		// System.out.println("new clientection to " + client.getRemoteSocketAddress());
	}

	@Override
	public void onClose(WebSocket client, int code, String reason, boolean remote) {
		System.out.println("closed " + client.getRemoteSocketAddress() + " with exit code " + code
				+ " additional info: " + reason);
	}

	@Override
	public void onMessage(WebSocket client, String message) {
		System.out.println("Received message");
		try {
			var optionalBroadcast = messageHandlerDecider.decide(message, client, authenticatedSessions);
			optionalBroadcast.ifPresent(broadcast -> broadcast(broadcast.getMessage(), broadcast.getClients()));
			System.out.println("Broadcasted");
		}
		catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new UnexpectedException();
		}
	}

	@Override
	public void onError(WebSocket client, Exception ex) {
		ex.printStackTrace();
		System.err.println("an error occurred on clientection " + ":" + ex);
	}

	@Override
	public void onStart() {
		System.out.println("server started successfully");
	}

}
