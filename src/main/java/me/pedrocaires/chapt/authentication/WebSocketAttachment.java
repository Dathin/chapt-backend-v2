package me.pedrocaires.chapt.authentication;

public class WebSocketAttachment {

	private final Authentication authentication;

	public WebSocketAttachment() {
		this.authentication = new Authentication();
	}

	public WebSocketAttachment(Authentication authentication) {
		this.authentication = authentication;
	}

	public Authentication getAuthenticationFilter() {
		return authentication;
	}

}
