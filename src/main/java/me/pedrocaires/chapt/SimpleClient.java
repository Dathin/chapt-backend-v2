package me.pedrocaires.chapt;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

public class SimpleClient extends WebSocketClient {

	public SimpleClient(URI serverUri, Draft draft) {
		super(serverUri, draft);
	}

	public SimpleClient(URI serverURI) {
		super(serverURI);
	}

	@Override
	public void onOpen(ServerHandshake handshakedata) {
		send("{\"handler\": \"AUTH\", \"username\": \"pedro\", \"password\": \"caires\"}");
		System.out.println("new connection opened");
		send("{\"handler\": \"DIRECT\", \"to\": \"daniel\", \"uuid\": \"1\", \"content\": \"petter1\"}");
	}

	@Override
	public void onClose(int code, String reason, boolean remote) {
		System.out.println("closed with exit code " + code + " additional info: " + reason);
	}

	@Override
	public void onMessage(String message) {
		System.out.println("received message: " + message);
		// send("{\"handler\": \"AUTH\", \"username\": \"pedro\", \"password\":
		// \"caires\"}");
		// send(message); //echo
	}

	@Override
	public void onMessage(ByteBuffer message) {
		System.out.println("received ByteBuffer");
	}

	@Override
	public void onError(Exception ex) {
		System.err.println("an error occurred:" + ex);
	}

	public static void main(String[] args) throws URISyntaxException {
		WebSocketClient client = new SimpleClient(new URI("ws://localhost:8087"));
		client.connect();
	}

}