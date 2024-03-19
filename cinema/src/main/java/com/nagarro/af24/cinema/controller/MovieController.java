package com.nagarro.af24.cinema.controller;

import com.nagarro.af24.cinema.dto.MovieDTO;
import com.nagarro.af24.cinema.dto.MovieDetailsDTO;
import com.nagarro.af24.cinema.service.ImageStorageService;
import com.nagarro.af24.cinema.service.MovieService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    public ResponseEntity<byte[]> uploadMovieImage(@RequestParam String title,
                                                   @RequestParam int year,
                                                   @RequestParam("image") MultipartFile file) {
        try {
            String imagePath = imageStorageService.storeImage(file, "movie");
            movieService.updateMovieImagePath(title, year, imagePath); // Actualizează filmul cu noua cale a imaginii

            // Citirea conținutului fișierului și returnarea acestuia ca array de bytes
            byte[] imageBytes = file.getBytes();

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG) // sau MediaType.IMAGE_PNG, depinde de tipul fișierului
                    .body(imageBytes);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
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