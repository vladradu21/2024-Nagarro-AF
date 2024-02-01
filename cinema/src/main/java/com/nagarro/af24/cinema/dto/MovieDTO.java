package com.nagarro.af24.cinema.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record MovieDTO(
        @Size(min = 3, max = 100)
        @NotNull
        String title,

        @NotNull
        Set<String> genres,

        @NotNull
        @Min(1900)
        int year,

        @NotNull
        @DecimalMin("0.0")
        double score
) {
}