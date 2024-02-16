package com.nagarro.af24.cinema.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record MovieDTO(
        @Size(min = 3, max = 100, message = "Titlul trebuie să aiba intre 3 si 100 de caractere.")
        @NotBlank(message = "Titlul nu poate fi gol.")
        String title,

        @NotNull(message = "Genurile nu pot fi nule.")
        Set<String> genres,

        @NotNull(message = "Anul nu poate fi nul.")
        @Min(value = 1900, message = "Anul trebuie să fie cel putin 1900.")
        int year,

        @NotNull(message = "Scorul nu poate fi nul.")
        @DecimalMin(value = "0.0", message = "Scorul trebuie sa fie cel putin 0.0.")
        double score
) {
}