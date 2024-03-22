package com.nagarro.af24.cinema.controller;

import com.nagarro.af24.cinema.dto.MovieDTO;
import com.nagarro.af24.cinema.dto.MovieDetailsDTO;
import com.nagarro.af24.cinema.service.MovieService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movies")
public class MovieController implements MovieApi{
    private final MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @Override
    public MovieDTO addMovie(@Valid @RequestBody MovieDTO movieDTO) {
        return movieService.addMovie(movieDTO);
    }

    @Override
    public MovieDTO getMovie(@RequestParam String title, @RequestParam int year) {
        return movieService.getMovie(title, year);
    }

    @Override
    public MovieDetailsDTO getMovieDetails(@RequestParam String movieTitle, @RequestParam int year) {
        return movieService.getMovieDetails(movieTitle, year);
    }

    @Override
    public MovieDTO updateMovie(@Valid @RequestBody MovieDTO movieDTO) {
        return movieService.updateMovie(movieDTO);
    }

    @Override
    public void deleteMovie(@RequestParam String title, @RequestParam int year) {
        movieService.deleteMovie(title, year);
    }
}