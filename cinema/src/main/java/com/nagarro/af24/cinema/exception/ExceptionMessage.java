package com.nagarro.af24.cinema.exception;

public enum ExceptionMessage {
    MOVIE_NOT_FOUND("Movie not found"),
    MOVIE_ALREADY_EXISTS("Movie already exists"),
    ACTOR_NOT_FOUND("Actor not found"),
    ACTOR_ALREADY_EXISTS("Actor already exists"),
    COUNTRY_NOT_FOUND("Country not found %s"),
    GENRE_NOT_FOUND("Genre not found %s"),
    USER_NOT_FOUND("User not found");

    private final String message;

    ExceptionMessage(String message) {
        this.message = message;
    }

    public String formatMessage() {
        return message;
    }

    public String formatMessage(Object... args) {
        return String.format(message, args);
    }
}