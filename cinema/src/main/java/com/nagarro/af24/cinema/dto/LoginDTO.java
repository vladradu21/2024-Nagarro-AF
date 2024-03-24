package com.nagarro.af24.cinema.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LoginDTO(
        @Size(min = 3, max = 20)
        @NotNull
        String username,

        @Size(min = 3, max = 20)
        @NotNull
        String password
) {
}