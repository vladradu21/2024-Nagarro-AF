package com.nagarro.af24.cinema.service;

import com.nagarro.af24.cinema.dto.ActorDTO;
import com.nagarro.af24.cinema.exception.CustomConflictException;
import com.nagarro.af24.cinema.exception.CustomNotFoundException;
import com.nagarro.af24.cinema.exception.ExceptionMessage;
import com.nagarro.af24.cinema.mapper.ActorMapper;
import com.nagarro.af24.cinema.model.Actor;
import com.nagarro.af24.cinema.model.Gender;
import com.nagarro.af24.cinema.repository.ActorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActorService {
    private final ActorRepository actorRepository;
    private final ActorMapper actorMapper;

    @Autowired
    public ActorService(ActorRepository actorRepository, ActorMapper actorMapper) {
        this.actorRepository = actorRepository;
        this.actorMapper = actorMapper;
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
        return actorMapper.toDTO(
                actorRepository.findByName(actorDTO.name()).map(actor -> {
                    actor.setAge(actorDTO.age());
                    actor.setGender(Gender.valueOf(actorDTO.gender()));
                    actor.setCountry(actorMapper.toEntity(actorDTO).getCountry());
                    return actorRepository.save(actor);
                }).orElseThrow(() -> new CustomNotFoundException(ExceptionMessage.ACTOR_NOT_FOUND.formatMessage()))
        );
    }

    public void deleteActor(String name) {
        Actor actor = actorRepository.findByName(name)
                .orElseThrow(() -> new CustomNotFoundException(ExceptionMessage.ACTOR_NOT_FOUND.formatMessage()));
        actorRepository.delete(actor);
    }
}