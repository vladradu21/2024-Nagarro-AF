package com.nagarro.af24.cinema.service;

import com.nagarro.af24.cinema.dto.ActorDTO;
import com.nagarro.af24.cinema.dto.MovieDTO;
import com.nagarro.af24.cinema.dto.MovieDetailsDTO;
import com.nagarro.af24.cinema.exception.CustomConflictException;
import com.nagarro.af24.cinema.exception.CustomNotFoundException;
import com.nagarro.af24.cinema.exception.ExceptionMessage;
import com.nagarro.af24.cinema.mapper.ActorMapper;
import com.nagarro.af24.cinema.mapper.MovieMapper;
import com.nagarro.af24.cinema.model.Actor;
import com.nagarro.af24.cinema.model.Gender;
import com.nagarro.af24.cinema.model.Movie;
import com.nagarro.af24.cinema.repository.ActorRepository;
import com.nagarro.af24.cinema.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActorService {
    private final ActorRepository actorRepository;
    private final ActorMapper actorMapper;
    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;

    @Autowired
    public ActorService(ActorRepository actorRepository, ActorMapper actorMapper, MovieRepository movieRepository, MovieMapper movieMapper) {
        this.actorRepository = actorRepository;
        this.actorMapper = actorMapper;
        this.movieRepository = movieRepository;
        this.movieMapper = movieMapper;
    }

    public ActorDTO addActor(ActorDTO actorDTO) {
        if (actorRepository.findByName(actorDTO.name()).isPresent()) {
            throw new CustomConflictException(ExceptionMessage.ACTOR_ALREADY_EXISTS.formatMessage());
        }

        Actor actorToSave = actorMapper.toEntity(actorDTO);
        Actor savedActor = actorRepository.save(actorToSave);
        return actorMapper.toDTO(savedActor);
    }

    public ActorDTO getActor(String name) {
        Actor actor = actorRepository.findByName(name)
                .orElseThrow(() -> new CustomNotFoundException(ExceptionMessage.ACTOR_NOT_FOUND.formatMessage()));
        return actorMapper.toDTO(actor);
    }

    public ActorDTO updateActor(ActorDTO actorDTO) {
        Actor actor = actorRepository.findByName(actorDTO.name())
                .orElseThrow(() -> new CustomNotFoundException(ExceptionMessage.ACTOR_NOT_FOUND.formatMessage()));

        updateActorFromDTO(actor, actorDTO);

        return actorMapper.toDTO(actorRepository.save(actor));
    }

    private void updateActorFromDTO(Actor actor, ActorDTO actorDTO) {
        actor.setAge(actorDTO.age());
        actor.setGender(Gender.valueOf(actorDTO.gender()));
        actor.setCountry(actorMapper.toEntity(actorDTO).getCountry());
    }

    public MovieDetailsDTO assignActorsToMovie(String movieTitle, int year, List<String> actorsNames) {
        Movie movie = movieRepository.findByTitleAndYear(movieTitle, year)
                .orElseThrow(() -> new CustomNotFoundException(ExceptionMessage.MOVIE_NOT_FOUND.formatMessage()));

        List<Actor> actors = actorRepository.findByNameIn(actorsNames);
        movie.setActors(actors);

        Movie savedMovie = movieRepository.save(movie);
        MovieDTO movieDTO = movieMapper.toDTO(savedMovie);
        List<Actor> savedActors = actorRepository.findByMovieTitleAndYear(movieTitle, year);
        List<ActorDTO> actorDTOS = actorMapper.toDTOs(savedActors);
        return new MovieDetailsDTO(movieDTO, actorDTOS);
    }

    public void deleteActor(String name) {
        Actor actor = actorRepository.findByName(name)
                .orElseThrow(() -> new CustomNotFoundException(ExceptionMessage.ACTOR_NOT_FOUND.formatMessage()));
        actorRepository.delete(actor);
    }
}