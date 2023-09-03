package ru.practicum.shareit.exception;

public class UnavailableStatusException extends RuntimeException {
    public UnavailableStatusException() {
    }

    public UnavailableStatusException(String message) {
        super(message);
    }
}
