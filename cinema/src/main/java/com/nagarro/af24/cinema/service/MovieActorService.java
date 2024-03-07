package com.nagarro.af24.cinema.service;

import com.nagarro.af24.cinema.dto.ActorDTO;
import com.nagarro.af24.cinema.dto.MovieDTO;
import com.nagarro.af24.cinema.dto.MovieDetailsDTO;
import com.nagarro.af24.cinema.exception.CustomNotFoundException;
import com.nagarro.af24.cinema.exception.ExceptionMessage;
import com.nagarro.af24.cinema.mapper.ActorMapper;
import com.nagarro.af24.cinema.mapper.MovieMapper;
import com.nagarro.af24.cinema.model.Actor;
import com.nagarro.af24.cinema.model.Movie;
import com.nagarro.af24.cinema.repository.ActorRepository;
import com.nagarro.af24.cinema.repository.MovieRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieActorService {
    private final MovieRepository movieRepository;
    private final ActorRepository actorRepository;
    private final MovieMapper movieMapper;
    private final ActorMapper actorMapper;

    public MovieActorService(MovieRepository movieRepository, ActorRepository actorRepository, MovieMapper movieMapper, ActorMapper actorMapper) {
        this.movieRepository = movieRepository;
        this.actorRepository = actorRepository;
        this.movieMapper = movieMapper;
        this.actorMapper = actorMapper;
    }

    @Transactional
    public MovieDetailsDTO assignActorsToMovie(String movieTitle, int year, List<String> actorsNames) {
        Movie movie = movieRepository.findByTitleAndYear(movieTitle, year)
                .orElseThrow(() -> new CustomNotFoundException(ExceptionMessage.MOVIE_NOT_FOUND.formatMessage()));

        List<Actor> actors = actorRepository.findByNameIn(actorsNames);
        movie.setActors(actors);

        Movie savedMovie = movieRepository.save(movie);
        MovieDTO movieDTO = movieMapper.toDTO(savedMovie);
        List<ActorDTO> actorDTOS = actorMapper.toDTOs(savedMovie.getActors());
        return new MovieDetailsDTO(movieDTO, actorDTOS);
    }

    public MovieDetailsDTO getMovieDetails(String movieTitle, int year) {
        Movie movie = movieRepository.findByTitleAndYear(movieTitle, year)
                .orElseThrow(() -> new CustomNotFoundException(ExceptionMessage.MOVIE_NOT_FOUND.formatMessage()));

        MovieDTO movieDTO = movieMapper.toDTO(movie);
        List<ActorDTO> actorDTOS = actorMapper.toDTOs(movie.getActors());
        return new MovieDetailsDTO(movieDTO, actorDTOS);
    }
}