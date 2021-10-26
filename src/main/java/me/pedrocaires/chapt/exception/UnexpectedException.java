package me.pedrocaires.chapt.exception;

public class UnexpectedException extends RuntimeException {

    public UnexpectedException() {
        super("Something unusual happened, closing connection");
    }

}
