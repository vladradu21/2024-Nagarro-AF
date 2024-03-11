package com.nagarro.af24.cinema.repository;

import com.nagarro.af24.cinema.model.Actor;
import com.nagarro.af24.cinema.model.Movie;
import com.nagarro.af24.cinema.utils.TestData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

class ActorRepositoryIntegrationTest extends BaseRepositoryIntegrationTest {
    private List<Actor> savedActors;
    private Movie savedMovie;

    @Autowired
    private ActorRepository actorRepository;
    @Autowired
    private MovieRepository movieRepository;

    @BeforeEach
    void setup() {
        Movie movieToSave = TestData.getMovie();
        savedMovie = movieRepository.save(movieToSave);
        List<Actor> actorsToSave = TestData.getActors();
        savedActors = actorRepository.saveAll(actorsToSave);
        savedActors.forEach(actor -> actor.setMovies(Set.of(savedMovie)));
    }

    @Test
    void testFindByName() {
        //Act
        Actor foundActor = actorRepository.findByName(savedActors.get(0).getName()).orElse(null);

        //Assert
        Assertions.assertNotNull(foundActor);
        Assertions.assertEquals(savedActors.get(0).getId(), foundActor.getId());
        Assertions.assertEquals(savedActors.get(0).getName(), foundActor.getName());
    }

    @Test
    void testFindByNameNotFound() {
        //Act
        Actor foundActor = actorRepository.findByName("Not Found").orElse(null);

        //Assert
        Assertions.assertNull(foundActor);
    }

    @Test
    void testFindByNameIn() {
        //Act
        List<Actor> foundActors = actorRepository.findByNameIn(List.of(savedActors.get(0).getName(), savedActors.get(1).getName()));

        //Assert
        Assertions.assertNotNull(foundActors);
        Assertions.assertEquals(savedActors.size(), foundActors.size());
    }

    @Test
    void testFindByNameInNotFound() {
        //Act
        List<Actor> foundActors = actorRepository.findByNameIn(List.of("Not Found"));

        //Assert
        Assertions.assertNotNull(foundActors);
        Assertions.assertEquals(0, foundActors.size());
    }

    @Test
    void testFindByMovieTitleAndYear() {
        //Act
        List<Actor> foundActors = actorRepository.findByMovieTitleAndYear(savedMovie.getTitle(), savedMovie.getYear());

        //Assert
        Assertions.assertNotNull(foundActors);
        Assertions.assertEquals(savedActors.size(), foundActors.size());
    }

    @Test
    void testFindByMovieTitleAndYearNotFound() {
        //Act
        List<Actor> foundActors = actorRepository.findByMovieTitleAndYear("Not Found", 2021);

        //Assert
        Assertions.assertNotNull(foundActors);
        Assertions.assertEquals(0, foundActors.size());
    }
}