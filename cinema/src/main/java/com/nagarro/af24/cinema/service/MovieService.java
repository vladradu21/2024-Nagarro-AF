package com.nagarro.af24.cinema.service;

import com.nagarro.af24.cinema.dto.MovieDTO;
import com.nagarro.af24.cinema.exception.CustomConflictException;
import com.nagarro.af24.cinema.exception.CustomNotFoundException;
import com.nagarro.af24.cinema.exception.ExceptionMessage;
import com.nagarro.af24.cinema.mapper.MovieMapper;
import com.nagarro.af24.cinema.model.Movie;
import com.nagarro.af24.cinema.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MovieService {
    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;

    @Autowired
    public MovieService(MovieRepository movieRepository, MovieMapper movieMapper) {
        this.movieRepository = movieRepository;
        this.movieMapper = movieMapper;
    }

    public MovieDTO addMovie(MovieDTO movieDTO) {
        if (movieRepository.findByTitleAndYear(movieDTO.title(), movieDTO.year()).isPresent()) {
            throw new CustomConflictException(ExceptionMessage.MOVIE_ALREADY_EXISTS.formatMessage());
        }

        Movie movieToSave = movieMapper.toEntity(movieDTO);
        Movie savedMovie = movieRepository.save(movieToSave);
        return movieMapper.toDTO(savedMovie);
    }

    public MovieDTO getMovie(String title, int year) {
        Movie movie = movieRepository.findByTitleAndYear(title, year)
                .orElseThrow(() -> new CustomNotFoundException(ExceptionMessage.MOVIE_NOT_FOUND.formatMessage()));
        return movieMapper.toDTO(movie);
    }

    public MovieDTO updateMovie(MovieDTO movieDTO) {
        return movieMapper.toDTO(
                movieRepository.findByTitleAndYear(movieDTO.title(), movieDTO.year()).map(movie -> {
                    movie.setGenres(movieMapper.toEntity(movieDTO).getGenres());
                    movie.setScore(movieDTO.score());
                    return movieRepository.save(movie);
                }).orElseThrow(() -> new CustomNotFoundException(ExceptionMessage.MOVIE_NOT_FOUND.formatMessage()))
        );
    }

    public void deleteMovie(String title, int year) {
        Movie movie = movieRepository.findByTitleAndYear(title, year)
                .orElseThrow(() -> new CustomNotFoundException(ExceptionMessage.MOVIE_NOT_FOUND.formatMessage()));
        movieRepository.delete(movie);
    }
}