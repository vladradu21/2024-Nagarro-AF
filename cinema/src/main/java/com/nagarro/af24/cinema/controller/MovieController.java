package com.nagarro.af24.cinema.controller;

import com.nagarro.af24.cinema.dto.MovieDTO;
import com.nagarro.af24.cinema.service.MovieService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movies")
public class MovieController {
    private final MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping
    public MovieDTO addMovie(@Valid @RequestBody MovieDTO movieDTO) {
        return movieService.addMovie(movieDTO);
    }

    @GetMapping
    public MovieDTO getMovie(@RequestParam String title) {
        return movieService.getMovie(title);
    }

    @DeleteMapping
    public void deleteMovie(@RequestParam String title) {
        movieService.deleteMovie(title);
    }
}