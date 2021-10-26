package me.pedrocaires.chapt.exception;

public class UnauthenticatedException extends RuntimeException {

	public UnauthenticatedException() {
		super("Must authenticate");
	}

}
