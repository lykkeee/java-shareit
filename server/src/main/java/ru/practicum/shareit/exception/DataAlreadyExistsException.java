package ru.practicum.shareit.exception;

public class DataAlreadyExistsException extends RuntimeException {
    public DataAlreadyExistsException() {
        super();
    }

    public DataAlreadyExistsException(String message) {
        super(message);
    }
}
