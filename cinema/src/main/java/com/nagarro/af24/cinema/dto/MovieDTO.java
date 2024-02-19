package com.nagarro.af24.cinema.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record MovieDTO(
        @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters.")
        @NotBlank(message = "Title cannot be blank.")
        String title,

        @NotNull(message = "Genres cannot be null.")
        Set<String> genres,

        @NotNull(message = "Year cannot be null.")
        @Min(value = 1900, message = "Year must be at least 1900.")
        int year,

        @NotNull(message = "Score cannot be null.")
        @DecimalMin(value = "0.0", message = "Score must be at least 0.0.")
        double score
) {
}