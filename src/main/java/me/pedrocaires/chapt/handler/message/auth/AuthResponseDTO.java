package me.pedrocaires.chapt.handler.message.auth;

import me.pedrocaires.chapt.handler.BaseMessageDTO;
import me.pedrocaires.chapt.handler.Handler;

public class AuthResponseDTO extends BaseMessageDTO {

	private boolean ok;

	public AuthResponseDTO() {
	}

	public AuthResponseDTO(boolean ok) {
		super(Handler.AUTH.toString());
		this.ok = ok;
	}

	public boolean isOk() {
		return ok;
	}

	public void setOk(boolean ok) {
		this.ok = ok;
	}

}
