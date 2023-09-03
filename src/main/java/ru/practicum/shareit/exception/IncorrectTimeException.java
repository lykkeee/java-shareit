package ru.practicum.shareit.exception;

public class IncorrectTimeException extends RuntimeException {
    public IncorrectTimeException() {
    }

    public IncorrectTimeException(String message) {
        super(message);
    }
}
