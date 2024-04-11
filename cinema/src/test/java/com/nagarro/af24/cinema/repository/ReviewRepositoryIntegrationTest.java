package com.nagarro.af24.cinema.repository;

import com.nagarro.af24.cinema.model.ApplicationUser;
import com.nagarro.af24.cinema.model.Movie;
import com.nagarro.af24.cinema.model.Review;
import com.nagarro.af24.cinema.utils.TestData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.assertj.ApplicationContextAssert;

import java.util.List;

class ReviewRepositoryIntegrationTest extends BaseRepositoryIntegrationTest {
    private List<Movie> savedMovies;
    private ApplicationUser savedUser;

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        List<Movie> moviesToSave = TestData.getMovies();
        savedMovies = movieRepository.saveAll(moviesToSave);
        ApplicationUser userToSave = TestData.getApplicationUser();
        savedUser = userRepository.save(userToSave);
    }

    @Test
    void testFindByMovieTitleAndMovieYear() {
        //Arrange
        List<Review> reviewsToSave = TestData.getReviews();
        reviewsToSave.forEach(review -> {
            review.setUser(savedUser);
        });
        reviewsToSave.get(0).setMovie(savedMovies.get(0));
        reviewsToSave.get(1).setMovie(savedMovies.get(1));
        List<Review> savedReviews = reviewRepository.saveAll(reviewsToSave);

        //Act
        List<Review> foundReviews = reviewRepository.findByMovieTitleAndMovieYear(savedMovies.get(0).getTitle(), savedMovies.get(0).getYear());

        //Assert
        Assertions.assertEquals(1, foundReviews.size());
        Assertions.assertEquals(foundReviews.get(0).getMovie().getTitle(), savedMovies.get(0).getTitle());
        Assertions.assertEquals(foundReviews.get(0).getMovie().getYear(), savedMovies.get(0).getYear());
    }

    @Test
    void testFindByMovieTitleAndMovieYearNotFound() {
        //Arrange
        List<Review> reviewsToSave = TestData.getReviews();
        reviewsToSave.forEach(review -> {
            review.setUser(savedUser);
        });
        reviewsToSave.get(0).setMovie(savedMovies.get(0));
        reviewsToSave.get(1).setMovie(savedMovies.get(1));
        List<Review> savedReviews = reviewRepository.saveAll(reviewsToSave);

        //Act
        List<Review> foundReviews = reviewRepository.findByMovieTitleAndMovieYear("Not Found", 2021);

        //Assert
        Assertions.assertEquals(0, foundReviews.size());
    }

    @Test
    void testExistsByMovieTitleAndMovieYearAndUserUsername() {
        //Arrange
        List<Review> reviewsToSave = TestData.getReviews();
        reviewsToSave.forEach(review -> {
            review.setUser(savedUser);
        });
        reviewsToSave.get(0).setMovie(savedMovies.get(0));
        reviewsToSave.get(1).setMovie(savedMovies.get(1));
        List<Review> savedReviews = reviewRepository.saveAll(reviewsToSave);
        Review existingReview = savedReviews.get(0);

        //Act
        boolean exists = reviewRepository.existsByMovieTitleAndMovieYearAndUserUsername(existingReview.getMovie().getTitle(), existingReview.getMovie().getYear(), existingReview.getUser().getUsername());

        //Assert
        Assertions.assertTrue(exists);
    }

    @Test
    void testExistsByMovieTitleAndMovieYearAndUserUsernameFalse() {
        //Act
        boolean exists = reviewRepository.existsByMovieTitleAndMovieYearAndUserUsername("Not Found", 404, "Not Found");

        //Assert
        Assertions.assertFalse(exists);
    }

    @Test
    void testFindByMovieTitleAndMovieYearAndUserUsername() {
        //Arrange
        List<Review> reviewsToSave = TestData.getReviews();
        reviewsToSave.forEach(review -> {
            review.setUser(savedUser);
        });
        reviewsToSave.get(0).setMovie(savedMovies.get(0));
        reviewsToSave.get(1).setMovie(savedMovies.get(1));
        List<Review> savedReviews = reviewRepository.saveAll(reviewsToSave);
        Review existingReview = savedReviews.get(0);

        //Act
        Review foundReview = reviewRepository.findByMovieTitleAndMovieYearAndUserUsername(existingReview.getMovie().getTitle(), existingReview.getMovie().getYear(), existingReview.getUser().getUsername()).orElse(null);

        //Assert
        Assertions.assertNotNull(foundReview);
        Assertions.assertEquals(existingReview.getMovie().getTitle(), foundReview.getMovie().getTitle());
        Assertions.assertEquals(existingReview.getMovie().getYear(), foundReview.getMovie().getYear());
        Assertions.assertEquals(existingReview.getUser().getUsername(), foundReview.getUser().getUsername());
    }

    @Test
    void testFindByMovieTitleAndMovieYearAndUserUsernameNotFound() {
        //Act
        Review foundReview = reviewRepository.findByMovieTitleAndMovieYearAndUserUsername("Not Found", 404, "Not Found").orElse(null);

        //Assert
        Assertions.assertNull(foundReview);
    }
}