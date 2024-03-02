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
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovieActorServiceTest {
    @Mock
    private MovieRepository movieRepository;
    @Mock
    private ActorRepository actorRepository;
    @Mock
    private MovieMapper movieMapper;
    @Mock
    private ActorMapper actorMapper;
    @InjectMocks
    private MovieActorService movieActorService;

    @Test
    void assignActorsToMovie() {
        Movie movie = TestData.getMovie();
        String title = movie.getTitle();
        int year = movie.getYear();
        Movie savedMovie = TestData.getMovie();
        List<String> actorsNames = TestData.getActorsNames();
        List<Actor> actors = TestData.getActors();
        MovieDTO movieDTO = TestData.getMovieDTO();
        List<ActorDTO> actorDTOS = TestData.getActorDTOs();
        when(movieRepository.findByTitleAndYear(title, year)).thenReturn(Optional.of(movie));
        when(actorRepository.findByNameIn(actorsNames)).thenReturn(actors);
        when(movieRepository.save(movie)).thenReturn(savedMovie);
        when(movieMapper.toDTO(savedMovie)).thenReturn(movieDTO);
        when(actorMapper.toDTOs(savedMovie.getActors())).thenReturn(actorDTOS);
        MovieDetailsDTO expected = new MovieDetailsDTO(movieDTO, actorDTOS);

        MovieDetailsDTO result = movieActorService.assignActorsToMovie(title, year, actorsNames);

        Assertions.assertEquals(expected, result);
    }

    @Test
    void getMovieDetails() {
        Movie movie = TestData.getMovie();
        String title = movie.getTitle();
        int year = movie.getYear();
        MovieDTO movieDTO = TestData.getMovieDTO();
        List<ActorDTO> actorDTOS = TestData.getActorDTOs();
        when(movieRepository.findByTitleAndYear(title, year)).thenReturn(Optional.of(movie));
        when(movieMapper.toDTO(movie)).thenReturn(movieDTO);
        when(actorMapper.toDTOs(movie.getActors())).thenReturn(actorDTOS);
        MovieDetailsDTO expected = new MovieDetailsDTO(movieDTO, actorDTOS);

        MovieDetailsDTO result = movieActorService.getMovieDetails(title, year);

        Assertions.assertEquals(expected, result);
    }
}