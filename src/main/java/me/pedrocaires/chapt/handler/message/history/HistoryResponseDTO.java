package me.pedrocaires.chapt.handler.message.history;

import me.pedrocaires.chapt.handler.BaseMessageDTO;
import me.pedrocaires.chapt.repository.message.Message;

import java.util.List;

public class HistoryResponseDTO extends BaseMessageDTO {

	public HistoryResponseDTO() {
		this.messages = messages;
	}

	public HistoryResponseDTO(String handler, List<Message> messages) {
		super(handler);
		this.messages = messages;
	}

	private List<Message> messages;

	public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

}
