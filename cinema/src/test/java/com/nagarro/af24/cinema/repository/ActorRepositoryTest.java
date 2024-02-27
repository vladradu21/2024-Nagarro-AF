package com.nagarro.af24.cinema.repository;

import com.nagarro.af24.cinema.model.Actor;
import com.nagarro.af24.cinema.utils.TestData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Repository.class))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("inttest")
class ActorRepositoryTest extends AbstractMySQLContainer {
    private List<Actor> actorsToSave;
    private List<Actor> savedActors;

    @Autowired
    private ActorRepository actorRepository;

    @BeforeEach
    void setup() {
        actorsToSave = TestData.getActors();
        savedActors = actorRepository.saveAll(actorsToSave);
    }

    @Test
    void testFindByName() {
        //Act
        Actor foundActor = actorRepository.findByName(actorsToSave.get(0).getName()).orElse(null);

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
        List<Actor> foundActors = actorRepository.findByNameIn(List.of(actorsToSave.get(0).getName(), actorsToSave.get(1).getName()));

        //Assert
        Assertions.assertNotNull(foundActors);
        Assertions.assertEquals(2, foundActors.size());
    }

    @Test
    void testFindByNameInNotFound() {
        //Act
        List<Actor> foundActors = actorRepository.findByNameIn(List.of("Not Found"));

        //Assert
        Assertions.assertNotNull(foundActors);
        Assertions.assertEquals(0, foundActors.size());
    }
}