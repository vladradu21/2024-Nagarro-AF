package com.nagarro.af24.cinema.controller;

import com.nagarro.af24.cinema.dto.ReviewDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface ReviewApi {
    @Operation(summary = "Add a review for a movie", description = "Add a review for a movie to the database", tags = {"Reviews"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Review added successfully",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ReviewDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Error.class)))
            }
    )
    @PostMapping
    ReviewDTO addReview(@RequestBody ReviewDTO reviewDTO);

    @Operation(summary = "Get reviews for a movie", description = "Get reviews for a movie from the database", tags = {"Reviews"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Reviews found",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ReviewDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Error.class))),
                    @ApiResponse(responseCode = "404", description = "Reviews not found",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Error.class)))
            }
    )
    @GetMapping
    List<ReviewDTO> getReviews(@RequestParam String movieTitle, @RequestParam int movieProductionYear);
}