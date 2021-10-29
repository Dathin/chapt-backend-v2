package me.pedrocaires.chapt.handler.message.direct;

import me.pedrocaires.chapt.handler.BaseMessageDTO;

public class DirectRequestDTO extends BaseMessageDTO {

	protected Integer to;

	protected String content;

	public Integer getTo() {
		return to;
	}

	public void setTo(Integer to) {
		this.to = to;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
