package com.nagarro.af24.cinema.controller;

import com.nagarro.af24.cinema.dto.ReviewDTO;
import com.nagarro.af24.cinema.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController implements ReviewApi {
    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @Override
    public ReviewDTO addReview(@RequestBody ReviewDTO reviewDTO) {
        return reviewService.addReview(reviewDTO);
    }

    @Override
    public List<ReviewDTO> getReviews(@RequestParam String movieTitle, @RequestParam int movieProductionYear) {
        return reviewService.getReviews(movieTitle, movieProductionYear);
    }
}