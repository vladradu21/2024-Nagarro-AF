package com.nagarro.af24.cinema.repository;

import com.nagarro.af24.cinema.model.Movie;
import com.nagarro.af24.cinema.model.Review;
import com.nagarro.af24.cinema.utils.TestData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

class ReviewRepositoryIntegrationTest extends BaseRepositoryIntegrationTest {
    private List<Review> savedReviews;
    private Movie savedMovie;

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private MovieRepository movieRepository;

    @BeforeEach
    void setup() {
        Movie movieToSave = TestData.getMovie();
        savedMovie = movieRepository.save(movieToSave);
        List<Review> reviewsToSave = TestData.getReviews();
        for (Review review : reviewsToSave) {
            review.setMovie(savedMovie);
        }
        savedReviews = reviewRepository.saveAll(reviewsToSave);
    }

    @Test
    void testFindByMovieTitleAndMovieYear() {
        //Act
        List<Review> foundReviews = reviewRepository.findByMovieTitleAndMovieYear(savedMovie.getTitle(), savedMovie.getYear());

        //Assert
        Assertions.assertEquals(foundReviews.size(), savedReviews.size());
        Assertions.assertEquals(foundReviews.get(0).getMovie().getTitle(), savedMovie.getTitle());
        Assertions.assertEquals(foundReviews.get(0).getMovie().getYear(), savedMovie.getYear());
    }

    @Test
    void testFindByMovieTitleAndMovieYearNotFound() {
        //Act
        List<Review> foundReviews = reviewRepository.findByMovieTitleAndMovieYear("Not Found", 2021);

        //Assert
        Assertions.assertEquals(0, foundReviews.size());
    }
}