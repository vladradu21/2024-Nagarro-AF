package com.nagarro.af24.cinema.controller;

import com.nagarro.af24.cinema.dto.MovieDTO;
import com.nagarro.af24.cinema.dto.MovieDetailsDTO;
import com.nagarro.af24.cinema.service.ImageStorageService;
import com.nagarro.af24.cinema.service.MovieService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieController {
    private final MovieService movieService;
    private final ImageStorageService imageStorageService;

    @Autowired
    public MovieController(MovieService movieService, ImageStorageService imageStorageService) {
        this.movieService = movieService;
        this.imageStorageService = imageStorageService;
    }

    @PostMapping
    public MovieDTO addMovie(@Valid @RequestBody MovieDTO movieDTO) {
        return movieService.addMovie(movieDTO);
    }

    @PostMapping("/add-image")
    public ResponseEntity<String> uploadMovieImage(@RequestParam String title,
                                              @RequestParam int year,
                                              @RequestParam("images") List<MultipartFile> files) throws IOException {

        List<String> imagesPaths = imageStorageService.storeImages(files, "movie");
        movieService.updateMovieImagesPaths(title, year, imagesPaths);

        return ResponseEntity.ok().body("Images uploaded successfully!");
    }

    @GetMapping
    public MovieDTO getMovie(@RequestParam String title, @RequestParam int year) {
        return movieService.getMovie(title, year);
    }

    @GetMapping("/all-with-details")
    public MovieDetailsDTO getMovieDetails(@RequestParam String movieTitle, @RequestParam int year) {
        return movieService.getMovieDetails(movieTitle, year);
    }

    @PutMapping
    public MovieDTO updateMovie(@Valid @RequestBody MovieDTO movieDTO) {
        return movieService.updateMovie(movieDTO);
    }

    @DeleteMapping
    public void deleteMovie(@RequestParam String title, @RequestParam int year) {
        movieService.deleteMovie(title, year);
    }
}