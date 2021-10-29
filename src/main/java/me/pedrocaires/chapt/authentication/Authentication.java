package me.pedrocaires.chapt.authentication;

public class Authentication {

	private boolean isAuthenticated;

	private int userId;

	public boolean isAuthenticated() {
		return isAuthenticated;
	}

	public void setAuthenticated(boolean authenticated) {
		isAuthenticated = authenticated;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getUserId() {
		return userId;
	}

}
