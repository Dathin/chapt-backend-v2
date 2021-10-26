package me.pedrocaires.chapt.handler;

import org.java_websocket.WebSocket;

import java.util.Collection;

public class SerializableBroadcast {

	private final String message;

	private final Collection<WebSocket> clients;

	public SerializableBroadcast(String message, Collection<WebSocket> clients) {
		this.message = message;
		this.clients = clients;
	}

	public String getMessage() {
		return message;
	}

	public Collection<WebSocket> getClients() {
		return clients;
	}

}
