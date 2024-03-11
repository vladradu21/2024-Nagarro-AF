package com.nagarro.af24.cinema.service;

import com.nagarro.af24.cinema.dto.ReviewDTO;
import com.nagarro.af24.cinema.mapper.ReviewMapper;
import com.nagarro.af24.cinema.model.Review;
import com.nagarro.af24.cinema.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, ReviewMapper reviewMapper) {
        this.reviewRepository = reviewRepository;
        this.reviewMapper = reviewMapper;
    }

    public ReviewDTO addReview(ReviewDTO reviewDTO) {
        Review reviewToSave = reviewMapper.toEntity(reviewDTO);
        Review savedReview = reviewRepository.save(reviewToSave);
        return reviewMapper.toDTO(savedReview);
    }

    public List<ReviewDTO> getReviews(String movieTitle, int movieYear) {
        List<Review> reviews = reviewRepository.findByMovieTitleAndMovieYear(movieTitle, movieYear);
        return reviewMapper.toDTOs(reviews);
    }
}