package me.pedrocaires.chapt.handler.message.usersearch;

import me.pedrocaires.chapt.handler.BaseMessageDTO;

public class UserSearchRequestDTO extends BaseMessageDTO {

	private String username;

	private Integer page;

	private Integer size;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

}
