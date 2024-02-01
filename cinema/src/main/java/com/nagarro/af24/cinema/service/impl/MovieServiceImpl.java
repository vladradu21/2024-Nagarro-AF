package com.nagarro.af24.cinema.service.impl;

import com.nagarro.af24.cinema.dto.MovieDTO;
import com.nagarro.af24.cinema.exception.CustomConflictException;
import com.nagarro.af24.cinema.exception.CustomNotFoundException;
import com.nagarro.af24.cinema.mapper.MovieMapper;
import com.nagarro.af24.cinema.model.Movie;
import com.nagarro.af24.cinema.repository.MovieRepository;
import com.nagarro.af24.cinema.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;
    @Autowired
    public MovieServiceImpl(MovieRepository movieRepository, MovieMapper movieMapper) {
        this.movieRepository = movieRepository;
        this.movieMapper = movieMapper;
    }

    @Override
    public MovieDTO addMovie(MovieDTO movieDTO) {
        if(movieRepository.findByTitle(movieDTO.title()).isPresent()) {
            throw new CustomConflictException("Movie already exists");
        }

        Movie movieToSave = movieMapper.toEntity(movieDTO);
        Movie savedMovie = movieRepository.save(movieToSave);
        return movieMapper.toDTO(savedMovie);
    }

    @Override
    public MovieDTO getMovie(String title) {
        Movie movie = movieRepository.findByTitle(title)
                .orElseThrow(() -> new CustomNotFoundException("Movie not found"));
        return movieMapper.toDTO(movie);
    }

    @Override
    public void deleteMovie(String title) {
        Movie movie = movieRepository.findByTitle(title)
                .orElseThrow(() -> new CustomNotFoundException("Movie not found"));
        movieRepository.delete(movie);
    }
}