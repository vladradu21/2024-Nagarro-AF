package com.nagarro.af24.cinema.dto;

import java.util.List;

public record MovieActorsDTO(
        MovieDTO movie,
        List<ActorDTO> actors
) {
}
