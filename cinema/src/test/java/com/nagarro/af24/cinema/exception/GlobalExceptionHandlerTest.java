package com.nagarro.af24.cinema.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {
    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Test
    void testHandleCustomNotFoundException() {
        CustomNotFoundException ex = new CustomNotFoundException(ExceptionMessage.MOVIE_NOT_FOUND.formatMessage());

        ApiExceptionFormat result = globalExceptionHandler.handleCustomNotFoundException(ex);

        Assertions.assertEquals(404, result.status());
        Assertions.assertEquals(ExceptionMessage.MOVIE_NOT_FOUND.formatMessage(), result.message());
    }

    @Test
    void testHandleCustomConflictException() {
        CustomConflictException ex = new CustomConflictException(ExceptionMessage.MOVIE_ALREADY_EXISTS.formatMessage());

        ApiExceptionFormat result = globalExceptionHandler.handleCustomConflictException(ex);

        Assertions.assertEquals(409, result.status());
        Assertions.assertEquals(ExceptionMessage.MOVIE_ALREADY_EXISTS.formatMessage(), result.message());
    }
}