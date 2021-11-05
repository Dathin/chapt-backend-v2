package me.pedrocaires.chapt.handler.message.direct;

public class DirectResponseDTO extends DirectRequestDTO {

	public DirectResponseDTO() {
	}

	public DirectResponseDTO(DirectRequestDTO directRequestDTO, int messageId) {
		this.handler = directRequestDTO.getHandler();
		this.to = directRequestDTO.to;
		this.content = directRequestDTO.content;
		this.messageId = messageId;
	}

	private int messageId;

	public int getMessageId() {
		return messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}

}
