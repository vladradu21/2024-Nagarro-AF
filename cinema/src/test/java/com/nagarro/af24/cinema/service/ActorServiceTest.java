package com.nagarro.af24.cinema.service;

import com.nagarro.af24.cinema.dto.ActorDTO;
import com.nagarro.af24.cinema.dto.MovieDTO;
import com.nagarro.af24.cinema.dto.MovieDetailsDTO;
import com.nagarro.af24.cinema.mapper.ActorMapper;
import com.nagarro.af24.cinema.mapper.MovieMapper;
import com.nagarro.af24.cinema.model.Actor;
import com.nagarro.af24.cinema.model.Movie;
import com.nagarro.af24.cinema.repository.ActorRepository;
import com.nagarro.af24.cinema.repository.MovieRepository;
import com.nagarro.af24.cinema.utils.TestData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ActorServiceTest {
    @Mock
    private ActorRepository actorRepository;
    @Mock
    private ActorMapper actorMapper;
    @Mock
    private MovieRepository movieRepository;
    @Mock
    private MovieMapper movieMapper;
    @InjectMocks
    private ActorService actorService;

    @Test
    void addActor() {
        Actor actor = TestData.getActor();
        ActorDTO actorDTO = TestData.getActorDTO();
        Actor savedActor = TestData.getActor();
        ActorDTO savedActorDTO = TestData.getActorDTO();
        when(actorRepository.findByName(actorDTO.name())).thenReturn(Optional.empty());
        when(actorMapper.toEntity(actorDTO)).thenReturn(actor);
        when(actorRepository.save(actor)).thenReturn(savedActor);
        when(actorMapper.toDTO(savedActor)).thenReturn(savedActorDTO);

        ActorDTO result = actorService.addActor(actorDTO);

        Assertions.assertEquals(savedActorDTO, result);
    }

    @Test
    void getActor() {
        Actor actor = TestData.getActor();
        ActorDTO actorDTO = TestData.getActorDTO();
        when(actorRepository.findByName(actorDTO.name())).thenReturn(Optional.of(actor));
        when(actorMapper.toDTO(actor)).thenReturn(actorDTO);

        ActorDTO result = actorService.getActor(actorDTO.name());

        Assertions.assertEquals(actorDTO, result);
    }

    @Test
    void updateActor() {
        Actor actor = TestData.getActor();
        ActorDTO actorDTO = TestData.getActorDTO();
        Actor updatedActor = TestData.getActor();
        ActorDTO updatedActorDTO = TestData.getActorDTO();
        when(actorRepository.findByName(actorDTO.name())).thenReturn(Optional.of(actor));
        when(actorMapper.toEntity(actorDTO)).thenReturn(actor);
        when(actorRepository.save(actor)).thenReturn(updatedActor);
        when(actorMapper.toDTO(updatedActor)).thenReturn(updatedActorDTO);

        ActorDTO result = actorService.updateActor(actorDTO);

        Assertions.assertEquals(updatedActorDTO, result);
    }

    @Test
    void assignActorsToMovie() {
        Movie movie = TestData.getMovie();
        String title = movie.getTitle();
        int year = movie.getYear();
        List<String> actorsNames = TestData.getActorsNames();
        List<Actor> foundActors = TestData.getActors();
        Movie savedMovie = TestData.getMovie();
        MovieDTO movieDTO = TestData.getMovieDTO();
        List<Actor> savedActors = TestData.getActors();
        List<ActorDTO> actorDTOS = TestData.getActorDTOs();
        when(movieRepository.findByTitleAndYear(title, year)).thenReturn(Optional.of(movie));
        when(actorRepository.findByNameIn(actorsNames)).thenReturn(foundActors);
        when(movieRepository.save(movie)).thenReturn(savedMovie);
        when(movieMapper.toDTO(savedMovie)).thenReturn(movieDTO);
        when(actorRepository.findByMovieTitleAndYear(title, year)).thenReturn(savedActors);
        when(actorMapper.toDTOs(savedActors)).thenReturn(actorDTOS);
        MovieDetailsDTO expected = new MovieDetailsDTO(movieDTO, actorDTOS);

        MovieDetailsDTO result = actorService.assignActorsToMovie(title, year, actorsNames);

        Assertions.assertEquals(expected, result);
    }

    @Test
    void deleteActor() {
        Actor actor = TestData.getActor();
        String name = actor.getName();
        when(actorRepository.findByName(name)).thenReturn(Optional.of(actor));

        actorService.deleteActor(name);

        Mockito.verify(actorRepository).delete(actor);
    }
}