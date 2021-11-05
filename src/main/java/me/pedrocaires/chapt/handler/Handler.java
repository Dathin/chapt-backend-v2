package me.pedrocaires.chapt.handler;

public enum Handler {

	AUTH, DIRECT, CREATE_USER, HISTORY;

	public static Handler fromString(String name) {
		try {
			return valueOf(name);
		}
		catch (IllegalArgumentException ignored) {
			return null;
		}
	}

}
