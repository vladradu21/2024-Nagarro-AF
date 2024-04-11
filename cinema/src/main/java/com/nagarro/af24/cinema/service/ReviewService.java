package com.nagarro.af24.cinema.service;

import com.nagarro.af24.cinema.dto.ReviewDTO;
import com.nagarro.af24.cinema.exception.CustomConflictException;
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
        if (reviewRepository.existsByMovieTitleAndMovieYearAndUserUsername(reviewToSave.getMovie().getTitle(), reviewToSave.getMovie().getYear(), reviewToSave.getUser().getUsername())) {
            throw new CustomConflictException("Review already exists");
        }
        Review savedReview = reviewRepository.save(reviewToSave);
        return reviewMapper.toDTO(savedReview);
    }

    public List<ReviewDTO> getReviews(String movieTitle, int movieYear) {
        List<Review> reviews = reviewRepository.findByMovieTitleAndMovieYear(movieTitle, movieYear);
        return reviewMapper.toDTOs(reviews);
    }

    public ReviewDTO updateReview(ReviewDTO reviewDTO) {
        Review review = reviewRepository.findByMovieTitleAndMovieYearAndUserUsername(reviewDTO.movieTitle(), reviewDTO.movieProductionYear(), reviewDTO.username())
                .orElseThrow(() -> new CustomConflictException("Review not found"));

        updateReviewFromDTO(review, reviewDTO);
        return reviewMapper.toDTO(reviewRepository.save(review));
    }

    private void updateReviewFromDTO(Review review, ReviewDTO reviewDTO) {
        review.setTitle(reviewDTO.title());
        review.setDescription(reviewDTO.description());
        review.setRating(reviewDTO.rating());
    }

    public void deleteReview(String movieTitle, int movieYear, String username) {
        Review review = reviewRepository.findByMovieTitleAndMovieYearAndUserUsername(movieTitle, movieYear, username)
                .orElseThrow(() -> new CustomConflictException("Review not found"));
        reviewRepository.delete(review);
    }
}