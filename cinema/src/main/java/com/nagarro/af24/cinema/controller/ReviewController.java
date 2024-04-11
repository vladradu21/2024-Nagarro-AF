package com.nagarro.af24.cinema.controller;

import com.nagarro.af24.cinema.dto.ReviewDTO;
import com.nagarro.af24.cinema.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
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
    public ReviewDTO addReview(@RequestBody ReviewDTO reviewDTO) {
        return reviewService.addReview(reviewDTO);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
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
    public List<ReviewDTO> getReviews(@RequestParam String movieTitle, @RequestParam int movieProductionYear) {
        return reviewService.getReviews(movieTitle, movieProductionYear);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Update a review for a movie", description = "Update a review for a movie in the database", tags = {"Reviews"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Review updated successfully",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ReviewDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Error.class))),
                    @ApiResponse(responseCode = "404", description = "Review not found",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Error.class)))
            }
    )
    @PutMapping
    public ReviewDTO updateReview(@RequestBody ReviewDTO reviewDTO) {
        return reviewService.updateReview(reviewDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a review for a movie", description = "Delete a review for a movie from the database", tags = {"Reviews"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Review deleted successfully",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ReviewDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Error.class))),
                    @ApiResponse(responseCode = "404", description = "Review not found",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Error.class)))
            }
    )
    @DeleteMapping
    public void deleteReview(@RequestParam String movieTitle, @RequestParam int movieProductionYear, @RequestParam String username) {
        reviewService.deleteReview(movieTitle, movieProductionYear, username);
    }
}