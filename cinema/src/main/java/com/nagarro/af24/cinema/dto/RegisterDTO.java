package com.nagarro.af24.cinema.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterDTO(
        @NotNull
        String firstname,

        @NotNull
        String lastname,

        @NotNull
        String email,

        @Size(min = 3, max = 20)
        @NotNull
        String username,

        @Size(min = 3, max = 20)
        @NotNull
        String password
) {
}