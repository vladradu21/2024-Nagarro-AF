package com.nagarro.af24.cinema.controller;

import com.nagarro.af24.cinema.dto.MovieDTO;
import com.nagarro.af24.cinema.dto.MovieDetailsDTO;
import com.nagarro.af24.cinema.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieController {
    private final MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Add a new movie", description = "Add a new movie to the database", tags = {"Movies"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Movie added successfully",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = MovieDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Error.class)))
            })
    @PostMapping
    public MovieDTO addMovie(@Valid @RequestBody MovieDTO movieDTO) {
        return movieService.addMovie(movieDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Upload images to a movie", description = "Add images paths to a movie in the database", tags = {"Movies"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Images added successfully",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = MovieDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Error.class)))
            })
    @PostMapping("/add-images")
    public ResponseEntity<List<String>> uploadMovieImages(@RequestParam String title,
                                                          @RequestParam int year,
                                                          @RequestParam("images") List<MultipartFile> files) {
        return ResponseEntity.ok(movieService.uploadMovieImages(title, year, files));
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Get a movie", description = "Get a movie from the database", tags = {"Movies"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Movie found",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = MovieDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Error.class))),
                    @ApiResponse(responseCode = "404", description = "Movie not found",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Error.class)))
            })
    @GetMapping
    public MovieDTO getMovie(@RequestParam String title, @RequestParam int year) {
        return movieService.getMovie(title, year);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Get all movies", description = "Get all movies from the database", tags = {"Movies"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Movies found",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = MovieDTO.class))),
                    @ApiResponse(responseCode = "404", description = "No movies found",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Error.class)))
            })
    @GetMapping("/all")
    public List<MovieDTO> getAllMovies() {
        return movieService.getAllMovies();
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Get a movie with details", description = "Get a movie with details from the database", tags = {"Movies"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Movie found",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = MovieDetailsDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Error.class))),
                    @ApiResponse(responseCode = "404", description = "Movie not found",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Error.class)))
            })
    @GetMapping("/with-details")
    public MovieDetailsDTO getMovieDetails(@RequestParam String movieTitle, @RequestParam int year) {
        return movieService.getMovieDetails(movieTitle, year);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Get images of a movie", description = "Get images paths of a movie from the database", tags = {"Movies"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Images found",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = MovieDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Error.class))),
                    @ApiResponse(responseCode = "404", description = "Images not found",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Error.class)))
            })
    @GetMapping("/get-images")
    public ResponseEntity<List<String>> getMovieImageUrls(@RequestParam String movieTitle, @RequestParam int year) {
        List<String> imageUrls = movieService.getMovieImagesUrls(movieTitle, year);
        return ResponseEntity.ok().body(imageUrls);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update a movie", description = "Update a movie in the database", tags = {"Movies"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Movie updated successfully",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = MovieDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Error.class))),
                    @ApiResponse(responseCode = "404", description = "Movie not found",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Error.class)))
            })
    @PutMapping
    public MovieDTO updateMovie(@Valid @RequestBody MovieDTO movieDTO) {
        return movieService.updateMovie(movieDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a movie", description = "Delete a movie from the database", tags = {"Movies"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Movie deleted successfully",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = MovieDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Error.class))),
                    @ApiResponse(responseCode = "404", description = "Movie not found",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Error.class)))
            })
    @DeleteMapping
    public void deleteMovie(@RequestParam String title, @RequestParam int year) {
        movieService.deleteMovie(title, year);
    }
}