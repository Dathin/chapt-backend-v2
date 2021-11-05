package me.pedrocaires.chapt.handler.message.history;

import me.pedrocaires.chapt.handler.BaseMessageDTO;

public class HistoryRequestDTO extends BaseMessageDTO {

	private Integer from;

	private Long previousThanId;

	private Integer size;

	public Integer getFrom() {
		return from;
	}

	public void setFrom(Integer from) {
		this.from = from;
	}

	public Long getPreviousThanId() {
		return previousThanId;
	}

	public void setPreviousThanId(Long previousThanId) {
		this.previousThanId = previousThanId;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

}
