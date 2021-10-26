package me.pedrocaires.chapt.handler.message.direct;

import me.pedrocaires.chapt.handler.BaseMessageDTO;

public class DirectDTO extends BaseMessageDTO {

	private String to;

	private String content;

	private String uuid;

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

}
