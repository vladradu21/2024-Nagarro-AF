package com.nagarro.af24.cinema.service;

import com.nagarro.af24.cinema.dto.MovieDTO;
import com.nagarro.af24.cinema.mapper.MovieMapper;
import com.nagarro.af24.cinema.model.Movie;
import com.nagarro.af24.cinema.repository.MovieRepository;
import com.nagarro.af24.cinema.utils.TestData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {
    @Mock
    private MovieRepository movieRepository;
    @Mock
    private MovieMapper movieMapper;
    @InjectMocks
    private MovieService movieService;

    @Test
    void addMovie() {
        Movie movie = TestData.getMovie();
        MovieDTO movieDTO = TestData.getMovieDTO();
        Movie savedMovie = TestData.getMovie();
        MovieDTO savedMovieDTO = TestData.getMovieDTO();
        when(movieRepository.findByTitleAndYear(movieDTO.title(), movieDTO.year())).thenReturn(Optional.empty());
        when(movieMapper.toEntity(movieDTO)).thenReturn(movie);
        when(movieRepository.save(movie)).thenReturn(savedMovie);
        when(movieMapper.toDTO(savedMovie)).thenReturn(savedMovieDTO);

        MovieDTO result = movieService.addMovie(movieDTO);

        Assertions.assertEquals(savedMovieDTO, result);
    }

    @Test
    void getMovie() {
        Movie movie = TestData.getMovie();
        MovieDTO movieDTO = TestData.getMovieDTO();
        when(movieRepository.findByTitleAndYear(movieDTO.title(), movieDTO.year())).thenReturn(Optional.of(movie));
        when(movieMapper.toDTO(movie)).thenReturn(movieDTO);

        MovieDTO result = movieService.getMovie(movieDTO.title(), movieDTO.year());

        Assertions.assertEquals(movieDTO, result);
    }

    @Test
    void updateMovie() {
        Movie movie = TestData.getMovie();
        MovieDTO movieDTO = TestData.getMovieDTO();
        Movie updatedMovie = TestData.getMovie();
        MovieDTO updatedMovieDTO = TestData.getMovieDTO();
        when(movieRepository.findByTitleAndYear(movieDTO.title(), movieDTO.year())).thenReturn(Optional.of(movie));
        when(movieMapper.toEntity(movieDTO)).thenReturn(movie);
        when(movieRepository.save(movie)).thenReturn(updatedMovie);
        when(movieMapper.toDTO(updatedMovie)).thenReturn(updatedMovieDTO);

        MovieDTO result = movieService.updateMovie(movieDTO);

        Assertions.assertEquals(updatedMovieDTO, result);
    }

    @Test
    void deleteMovie() {
        Movie movie = TestData.getMovie();
        String title = movie.getTitle();
        int year = movie.getYear();
        when(movieRepository.findByTitleAndYear(title, year)).thenReturn(Optional.of(movie));

        movieService.deleteMovie(title, year);

        Mockito.verify(movieRepository).delete(movie);
    }
}