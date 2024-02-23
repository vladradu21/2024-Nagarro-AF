package com.nagarro.af24.cinema.controller;

import com.nagarro.af24.cinema.dto.MovieDetailsDTO;
import com.nagarro.af24.cinema.service.MovieActorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/movie-actor")
public class MovieActorController {
    private final MovieActorService movieActorService;

    @Autowired
    public MovieActorController(MovieActorService movieActorService) {
        this.movieActorService = movieActorService;
    }

    @PostMapping
    public MovieDetailsDTO addActorToMovie(@RequestParam String movieTitle, @RequestParam int year, @RequestParam List<String> actorsNames) {
        return movieActorService.addActorToMovie(movieTitle, year, actorsNames);
    }

    @GetMapping
    public MovieDetailsDTO getMovieDetails(@RequestParam String movieTitle, @RequestParam int year) {
        return movieActorService.getMovieDetails(movieTitle, year);
    }
}