package com.nagarro.af24.cinema.dto;

import java.util.List;

public record MovieDetailsDTO(
        MovieDTO movie,
        List<ActorDTO> actors
) {
}