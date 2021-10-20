package me.pedrocaires.chapt.handler;

import org.java_websocket.WebSocket;

import java.util.Collection;

public class Broadcast<RES> {

	private RES message;

	private Collection<WebSocket> clients;

	public Broadcast(RES message, Collection<WebSocket> clients) {
		this.message = message;
		this.clients = clients;
	}

	public RES getMessage() {
		return message;
	}

	public Collection<WebSocket> getClients() {
		return clients;
	}

}
