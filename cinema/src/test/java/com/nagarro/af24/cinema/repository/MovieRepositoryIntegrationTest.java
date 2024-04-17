package com.nagarro.af24.cinema.repository;

import com.nagarro.af24.cinema.model.ApplicationUser;
import com.nagarro.af24.cinema.model.Movie;
import com.nagarro.af24.cinema.model.Review;
import com.nagarro.af24.cinema.utils.TestData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

class MovieRepositoryIntegrationTest extends BaseRepositoryIntegrationTest {
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindByTitleAndYear() {
        //Arrange
        Movie movieToSave = TestData.getMovie();
        Movie savedMovie = movieRepository.save(movieToSave);

        //Act
        Movie foundMovie = movieRepository.findByTitleAndYear(movieToSave.getTitle(), movieToSave.getYear()).orElse(null);

        //Assert
        Assertions.assertNotNull(foundMovie);
        Assertions.assertEquals(savedMovie.getId(), foundMovie.getId());
        Assertions.assertEquals(savedMovie.getTitle(), foundMovie.getTitle());
    }

    @Test
    void testFindByTitleAndYearNotFound() {
        //Act
        Movie foundMovie = movieRepository.findByTitleAndYear("Not Found", 2021).orElse(null);

        //Assert
        Assertions.assertNull(foundMovie);
    }

    @Test
    void testFindAllWithReviews() {
        //Arrange
        Movie movieToSave = TestData.getMovie();
        Movie savedMovie = movieRepository.save(movieToSave);
        ApplicationUser user = TestData.getApplicationUser();
        ApplicationUser savedUser = userRepository.save(user);
        Review review = TestData.getReview();
        review.setMovie(savedMovie);
        review.setUser(savedUser);
        Review savedReview = reviewRepository.save(review);

        //Act
        List<Movie> movies = movieRepository.findAllWithReviews();

        //Assert
        Assertions.assertFalse(movies.isEmpty());
    }
}