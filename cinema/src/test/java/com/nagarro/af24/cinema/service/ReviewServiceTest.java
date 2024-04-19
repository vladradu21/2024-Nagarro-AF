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

import static org.mockito.Mockito.verify;
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
        ReviewDTO reviewDTO = TestData.getReviewDTO();
        Review reviewToSave = TestData.getReview();
        reviewToSave.setMovie(TestData.getMovie());
        reviewToSave.setUser(TestData.getApplicationUser());
        when(reviewMapper.toEntity(reviewDTO)).thenReturn(reviewToSave);
        when(reviewRepository.existsByMovieTitleAndMovieYearAndUserUsername(reviewToSave.getMovie().getTitle(), reviewToSave.getMovie().getYear(), reviewToSave.getUser().getUsername())).thenReturn(false);
        Review savedReview = TestData.getReview();
        when(reviewRepository.save(reviewToSave)).thenReturn(savedReview);
        ReviewDTO savedReviewDTO = TestData.getReviewDTO();
        when(reviewMapper.toDTO(savedReview)).thenReturn(savedReviewDTO);

        ReviewDTO result = reviewService.addReview(reviewDTO);

        Assertions.assertEquals(savedReviewDTO, result);
    }

    @Test
    void getReviews() {
        List<Review> reviews = TestData.getReviews();
        String movieTitle = TestData.getMovie().getTitle();
        int movieYear = TestData.getMovie().getYear();
        when(reviewRepository.findByMovieTitleAndMovieYear(movieTitle, movieYear)).thenReturn(reviews);
        List<ReviewDTO> reviewDTOs = TestData.getReviewDTOs();
        when(reviewMapper.toDTOs(reviews)).thenReturn(reviewDTOs);

        List<ReviewDTO> result = reviewService.getReviews(movieTitle, movieYear);

        Assertions.assertEquals(reviewDTOs, result);
    }

    @Test
    void updateReview() {
        ReviewDTO reviewDTO = TestData.getReviewDTO();
        Review review = TestData.getReview();
        when(reviewRepository.findByMovieTitleAndMovieYearAndUserUsername(reviewDTO.movieTitle(), reviewDTO.movieProductionYear(), reviewDTO.username())).thenReturn(java.util.Optional.of(review));
        Review updatedReview = TestData.getReview();
        when(reviewRepository.save(review)).thenReturn(updatedReview);
        ReviewDTO updatedReviewDTO = TestData.getReviewDTO();
        when(reviewMapper.toDTO(updatedReview)).thenReturn(updatedReviewDTO);

        ReviewDTO result = reviewService.updateReview(reviewDTO);

        Assertions.assertEquals(updatedReviewDTO, result);
    }

    @Test
    void deleteReview() {
        Review review = TestData.getReview();
        String movieTitle = TestData.getMovie().getTitle();
        int movieYear = TestData.getMovie().getYear();
        String username = TestData.getApplicationUser().getUsername();
        when(reviewRepository.findByMovieTitleAndMovieYearAndUserUsername(movieTitle, movieYear, username)).thenReturn(java.util.Optional.of(review));

        reviewService.deleteReview(movieTitle, movieYear, username);

        verify(reviewRepository).delete(review);
    }
}