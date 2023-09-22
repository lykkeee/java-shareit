package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.exception.IllegalStateException;

import static org.junit.jupiter.api.Assertions.*;

class ErrorHandlerTest {

    private final ErrorHandler errorHandler = new ErrorHandler();

    @Test
    void handleNotFoundException() {
        DataNotFoundException exception = new DataNotFoundException();
        assertNotNull(errorHandler.handleNotFoundException(exception));
    }

    @Test
    void handleAlreadyExistsException() {
        DataAlreadyExistsException exception = new DataAlreadyExistsException();
        assertNotNull(errorHandler.handleAlreadyExistsException(exception));
    }

    @Test
    void handleEmailAlreadyExistsException() {
        EmailAlreadyExistsException exception = new EmailAlreadyExistsException();
        assertNotNull(errorHandler.handleEmailAlreadyExistsException(exception));
    }

    @Test
    void handleUnavailableStatusException() {
        UnavailableStatusException exception = new UnavailableStatusException();
        assertNotNull(errorHandler.handleUnavailableStatusException(exception));
    }

    @Test
    void handleIncorrectTimeException() {
        IncorrectTimeException exception = new IncorrectTimeException();
        assertNotNull(errorHandler.handleIncorrectTimeException(exception));
    }

    @Test
    void handleOwnersBookingException() {
        OwnersBookingException exception = new OwnersBookingException();
        assertNotNull(errorHandler.handleOwnersBookingException(exception));
    }

    @Test
    void handleIllegalStateException() {
        ru.practicum.shareit.exception.IllegalStateException exception = new IllegalStateException();
        assertNotNull(errorHandler.handleIllegalStateException(exception));
    }

    @Test
    void handleIncorrectUserCommentException() {
        IncorrectUserCommentException exception = new IncorrectUserCommentException();
        assertNotNull(errorHandler.handleIncorrectUserCommentException(exception));
    }
}