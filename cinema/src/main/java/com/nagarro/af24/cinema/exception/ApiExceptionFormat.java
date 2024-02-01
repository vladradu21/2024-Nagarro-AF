package com.nagarro.af24.cinema.exception;

import java.time.LocalDateTime;

public record ApiExceptionFormat(
        LocalDateTime timestamp,

        int status,

        String message
) {
}