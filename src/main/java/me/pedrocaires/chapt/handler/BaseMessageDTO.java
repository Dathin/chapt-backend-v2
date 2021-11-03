package me.pedrocaires.chapt.handler;

public class BaseMessageDTO {

	protected String handler;

	public BaseMessageDTO() {
	}

	public BaseMessageDTO(String handler) {
		this.handler = handler;
	}

	public String getHandler() {
		return handler;
	}

	public void setHandler(String handler) {
		this.handler = handler;
	}

}
