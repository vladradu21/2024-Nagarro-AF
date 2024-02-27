package com.nagarro.af24.cinema.utils;

import com.nagarro.af24.cinema.dto.ActorDTO;
import com.nagarro.af24.cinema.dto.MovieDTO;
import com.nagarro.af24.cinema.model.Actor;
import com.nagarro.af24.cinema.model.Gender;
import com.nagarro.af24.cinema.model.Movie;

import java.util.List;
import java.util.Set;

public class TestData {
    public static Movie getMovie() {
        return new Movie(
                null,
                "The Shawshank Redemption",
                null,
                1994,
                9.3,
                null
        );
    }

    public static List<Actor> getActors() {
        return List.of(
                new Actor(null, "Tim Robbins", 30, Gender.MALE, null),
                new Actor(null, "Morgan Freeman", 70, Gender.MALE, null)
        );
    }

    public static MovieDTO getMovieDTO() {
        return new MovieDTO(
                "Shawshank Redemption",
                Set.of("Drama", "Crime"),
                1994,
                9.3
        );
    }

    public static MovieDTO getMovieDTOWithWrongGenre() {
        return new MovieDTO(
                "Shawshank Redemption",
                Set.of("Drama", "Wrong Genre"),
                1994,
                9.3
        );
    }

    public static ActorDTO getActorDTO() {
        return new ActorDTO(
                "Tim Robbins",
                30,
                "MALE",
                "United States"
        );
    }

    public static ActorDTO getActorDTOWithWrongCountry() {
        return new ActorDTO(
                "Tim Robbins",
                30,
                "MALE",
                "Wrong Country"
        );
    }
}