package com.nagarro.af24.cinema.utils;

import com.nagarro.af24.cinema.dto.ActorDTO;
import com.nagarro.af24.cinema.dto.MovieDTO;
import com.nagarro.af24.cinema.dto.ReviewDTO;
import com.nagarro.af24.cinema.model.Actor;
import com.nagarro.af24.cinema.model.Gender;
import com.nagarro.af24.cinema.model.Movie;
import com.nagarro.af24.cinema.model.Review;

import java.util.List;
import java.util.Set;

public class TestData {
    public static Movie getMovie() {
        return new Movie(null, "The Shawshank Redemption", null, 1994, 9.3, null, null, null);
    }

    public static Actor getActor() {
        return new Actor(null, "Tim Robbins", 30, Gender.MALE, null, null);
    }

    public static List<Actor> getActors() {
        return List.of(
                new Actor(null, "Tim Robbins", 30, Gender.MALE, null, null),
                new Actor(null, "Morgan Freeman", 70, Gender.MALE, null, null));
    }

    public static MovieDTO getMovieDTO() {
        return new MovieDTO("Shawshank Redemption", Set.of("Drama", "Crime"), 1994, 9.3);
    }

    public static MovieDTO getMovieDTOWithWrongGenre() {
        return new MovieDTO("Shawshank Redemption", Set.of("Drama", "Wrong Genre"), 1994, 9.3);
    }

    public static ActorDTO getActorDTO() {
        return new ActorDTO("Tim Robbins", 30, "MALE", "United States");
    }

    public static ActorDTO getActorDTOWithWrongCountry() {
        return new ActorDTO("Tim Robbins", 30, "MALE", "Wrong Country");
    }

    public static List<ActorDTO> getActorDTOs() {
        return List.of(
                new ActorDTO("Tim Robbins", 30, "MALE", "United States"),
                new ActorDTO("Morgan Freeman", 70, "MALE", "United States"));
    }

    public static List<String> getActorsNames() {
        return List.of("Tim Robbins", "Morgan Freeman");
    }

    public static Review getReview() {
        return new Review(null, "The Shawshank Redemption, review", 9.3, "Liked it!", null);
    }

    public static List<Review> getReviews() {
        return List.of(
                new Review(null, "Let's talk about The Shawshank Redemption", 9.3, "Great movie!", null),
                new Review(null, "The Shawshank Redemption, review", 9.3, "Liked it!", null));
    }

    public static ReviewDTO getReviewDTO() {
        return new ReviewDTO("The Shawshank Redemption, review", 9.3, "Liked it!", "Shawshank Redemption", 1994);
    }

    public static List<ReviewDTO> getReviewDTOs() {
        return List.of(
                new ReviewDTO("Let's talk about The Shawshank Redemption", 9.3, "Great movie!", "Shawshank Redemption", 1994),
                new ReviewDTO("The Shawshank Redemption, review", 9.3, "Liked it!", "Shawshank Redemption", 1994));
    }
}