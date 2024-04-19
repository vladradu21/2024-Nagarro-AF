package com.nagarro.af24.cinema.controller;

import com.nagarro.af24.cinema.dto.MovieDTO;
import com.nagarro.af24.cinema.dto.MovieDetailsDTO;
import com.nagarro.af24.cinema.service.MovieService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieController implements MovieApi {
    private final MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public MovieDTO addMovie(@Valid @RequestBody MovieDTO movieDTO) {
        return movieService.addMovie(movieDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public ResponseEntity<List<String>> uploadMovieImages(@RequestParam String title,
                                                          @RequestParam int year,
                                                          @RequestParam("images") List<MultipartFile> files) {
        return ResponseEntity.ok(movieService.uploadMovieImages(title, year, files));
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Override
    public MovieDTO getMovie(@RequestParam String title, @RequestParam int year) {
        return movieService.getMovie(title, year);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Override
    public MovieDetailsDTO getMovieDetails(@RequestParam String movieTitle, @RequestParam int year) {
        return movieService.getMovieDetails(movieTitle, year);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Override
    public ResponseEntity<List<String>> getMovieImageUrls(@RequestParam String movieTitle, @RequestParam int year) {
        List<String> imageUrls = movieService.getMovieImagesUrls(movieTitle, year);
        return ResponseEntity.ok().body(imageUrls);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public MovieDTO updateMovie(@Valid @RequestBody MovieDTO movieDTO) {
        return movieService.updateMovie(movieDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public void deleteMovie(@RequestParam String title, @RequestParam int year) {
        movieService.deleteMovie(title, year);
    }
}