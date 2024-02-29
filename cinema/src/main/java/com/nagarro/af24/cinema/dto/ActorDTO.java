package com.nagarro.af24.cinema.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ActorDTO(
        @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters.")
        @NotBlank(message = "Name cannot be blank.")
        String name,

        @NotNull(message = "Age cannot be null.")
        @Min(value = 0, message = "Age must be at least 0.")
        int age,

        @NotNull(message = "Gender cannot be null.")
        String gender,

        @NotNull(message = "Country cannot be null.")
        String country
) {
}