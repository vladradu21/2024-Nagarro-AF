package com.nagarro.af24.cinema.service;

import com.nagarro.af24.cinema.dto.ActorDTO;
import com.nagarro.af24.cinema.mapper.ActorMapper;
import com.nagarro.af24.cinema.model.Actor;
import com.nagarro.af24.cinema.repository.ActorRepository;
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
class ActorServiceTest {
    @Mock
    private ActorRepository actorRepository;
    @Mock
    private ActorMapper actorMapper;
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
    void deleteActor() {
        Actor actor = TestData.getActor();
        String name = actor.getName();
        when(actorRepository.findByName(name)).thenReturn(Optional.of(actor));

        actorService.deleteActor(name);

        Mockito.verify(actorRepository).delete(actor);
    }
}