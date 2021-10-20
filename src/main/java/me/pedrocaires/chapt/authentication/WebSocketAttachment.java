package me.pedrocaires.chapt.authentication;

public class WebSocketAttachment {

	private final Authentication authentication;

	public WebSocketAttachment() {
		this.authentication = new Authentication();
	}

	public Authentication getAuthenticationFilter() {
		return authentication;
	}

}
