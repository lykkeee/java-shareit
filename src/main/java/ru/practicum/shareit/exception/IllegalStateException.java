package ru.practicum.shareit.exception;

public class IllegalStateException extends RuntimeException {
    public IllegalStateException() {
    }

    public IllegalStateException(String message) {
        super(message);
    }
}
