package me.pedrocaires.chapt.handler;

public enum Handler {

    AUTH, DIRECT;

    public static Handler fromString(String name) {
        try {
            return valueOf(name);
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }

}
