package me.pedrocaires.chapt.handler.message.usersearch;

import me.pedrocaires.chapt.handler.BaseMessageDTO;
import me.pedrocaires.chapt.repository.user.User;

import java.util.List;

public class UserSearchResponseDTO extends BaseMessageDTO {

	public UserSearchResponseDTO() {
	}

	public UserSearchResponseDTO(String handler, List<User> users) {
		super(handler);
		this.users = users;
	}

	private List<User> users;

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

}
