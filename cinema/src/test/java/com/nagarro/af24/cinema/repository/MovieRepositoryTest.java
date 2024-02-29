package com.nagarro.af24.cinema.repository;

import com.nagarro.af24.cinema.model.Movie;
import com.nagarro.af24.cinema.utils.TestData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MovieRepositoryTest extends BaseRepositoryTest {
    private Movie movieToSave;
    private Movie savedMovie;

    @Autowired
    private MovieRepository movieRepository;

    @BeforeEach
    void setup() {
        movieToSave = TestData.getMovie();
        savedMovie = movieRepository.save(movieToSave);
    }

    @Test
    void testFindByTitleAndYear() {
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
}