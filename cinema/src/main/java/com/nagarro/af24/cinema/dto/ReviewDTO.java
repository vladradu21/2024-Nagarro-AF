package com.nagarro.af24.cinema.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ReviewDTO(
        @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters.")
        @NotBlank(message = "Title cannot be blank.")
        String title,

        @DecimalMin(value = "0.0", message = "Rating must be greater than 0.0")
        @DecimalMax(value = "10.0", message = "Rating must be less than 10.0")
        double rating,

        @NotBlank(message = "Description cannot be blank.")
        String description,

        @NotNull(message = "Movie title cannot be null.")
        String movieTitle,

        @NotNull(message = "Movie year cannot be null.")
        int movieProductionYear,

        String username
) {
}