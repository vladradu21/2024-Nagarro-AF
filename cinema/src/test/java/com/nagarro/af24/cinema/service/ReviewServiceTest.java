package com.nagarro.af24.cinema.service;

import com.nagarro.af24.cinema.dto.ReviewDTO;
import com.nagarro.af24.cinema.mapper.ReviewMapper;
import com.nagarro.af24.cinema.model.Review;
import com.nagarro.af24.cinema.repository.ReviewRepository;
import com.nagarro.af24.cinema.utils.TestData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private ReviewMapper reviewMapper;
    @InjectMocks
    private ReviewService reviewService;

    @Test
    void addReview() {
        Review review = TestData.getReview();
        ReviewDTO reviewDTO = TestData.getReviewDTO();
        Review savedReview = TestData.getReview();
        ReviewDTO savedReviewDTO = TestData.getReviewDTO();
        when(reviewMapper.toEntity(reviewDTO)).thenReturn(review);
        when(reviewRepository.save(review)).thenReturn(savedReview);
        when(reviewMapper.toDTO(savedReview)).thenReturn(savedReviewDTO);

        ReviewDTO result = reviewService.addReview(reviewDTO);

        Assertions.assertEquals(savedReviewDTO, result);
    }

    @Test
    void getReviews() {
        List<Review> reviews = TestData.getReviews();
        String movieTitle = TestData.getMovie().getTitle();
        int movieYear = TestData.getMovie().getYear();
        List<ReviewDTO> reviewDTOs = TestData.getReviewDTOs();
        when(reviewRepository.findByMovieTitleAndMovieYear(movieTitle, movieYear)).thenReturn(reviews);
        when(reviewMapper.toDTOs(reviews)).thenReturn(reviewDTOs);

        List<ReviewDTO> result = reviewService.getReviews(movieTitle, movieYear);

        Assertions.assertEquals(reviewDTOs, result);
    }
}