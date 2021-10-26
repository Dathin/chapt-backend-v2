package me.pedrocaires.chapt.handler.message.auth;

import me.pedrocaires.chapt.handler.BaseMessageDTO;

public class AuthRequestDTO extends BaseMessageDTO {

	private String username;

	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
