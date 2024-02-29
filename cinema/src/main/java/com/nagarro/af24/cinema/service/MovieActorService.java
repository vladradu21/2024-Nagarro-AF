package com.nagarro.af24.cinema.service;

import com.nagarro.af24.cinema.dto.ActorDTO;
import com.nagarro.af24.cinema.dto.MovieDTO;
import com.nagarro.af24.cinema.dto.MovieDetailsDTO;
import com.nagarro.af24.cinema.exception.CustomNotFoundException;
import com.nagarro.af24.cinema.mapper.ActorMapper;
import com.nagarro.af24.cinema.mapper.MovieMapper;
import com.nagarro.af24.cinema.model.Actor;
import com.nagarro.af24.cinema.model.Movie;
import com.nagarro.af24.cinema.repository.ActorRepository;
import com.nagarro.af24.cinema.repository.MovieRepository;
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

    public MovieDetailsDTO addActorToMovie(String movieTitle, List<String> actorsNames) {
        Movie movie = movieRepository.findByTitle(movieTitle)
                .orElseThrow(() -> new CustomNotFoundException("Movie not found"));

        List<Actor> actors = actorRepository.findByNameIn(actorsNames);
        List<Actor> existingActors = movie.getActors();
        existingActors.addAll(actors);

        Movie savedMovie = movieRepository.save(movie);
        MovieDTO movieDTO = movieMapper.toDTO(savedMovie);
        List<ActorDTO> actorDTOS = actorMapper.toDTOs(savedMovie.getActors());
        return new MovieDetailsDTO(movieDTO, actorDTOS);
    }

    public MovieDetailsDTO getMovieDetails(String movieTitle) {
        Movie movie = movieRepository.findByTitle(movieTitle)
                .orElseThrow(() -> new CustomNotFoundException("Movie not found"));

        MovieDTO movieDTO = movieMapper.toDTO(movie);
        List<ActorDTO> actorDTOS = actorMapper.toDTOs(movie.getActors());
        return new MovieDetailsDTO(movieDTO, actorDTOS);
    }
}