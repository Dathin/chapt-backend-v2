package me.pedrocaires.chapt.handler.message.shareddto;

import me.pedrocaires.chapt.handler.BaseMessageDTO;
import me.pedrocaires.chapt.handler.Handler;

public class AckResponseDTO extends BaseMessageDTO {

	private boolean ok;

	public AckResponseDTO() {
	}

	public AckResponseDTO(boolean ok) {
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
