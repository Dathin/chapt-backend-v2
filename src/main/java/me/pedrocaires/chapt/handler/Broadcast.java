package me.pedrocaires.chapt.handler;

import org.java_websocket.WebSocket;

import java.util.Collection;

public class Broadcast<O> {

	private O message;

	private Collection<WebSocket> clients;

	public Broadcast(O message, Collection<WebSocket> clients) {
		this.message = message;
		this.clients = clients;
	}

	public O getMessage() {
		return message;
	}

	public Collection<WebSocket> getClients() {
		return clients;
	}

}
