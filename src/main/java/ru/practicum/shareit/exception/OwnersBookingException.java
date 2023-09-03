package ru.practicum.shareit.exception;

public class OwnersBookingException extends RuntimeException {
    public OwnersBookingException() {
    }

    public OwnersBookingException(String message) {
        super(message);
    }
}
