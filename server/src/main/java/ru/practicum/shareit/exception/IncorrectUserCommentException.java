package ru.practicum.shareit.exception;

public class IncorrectUserCommentException extends RuntimeException {
    public IncorrectUserCommentException() {
    }

    public IncorrectUserCommentException(String message) {
        super(message);
    }
}
