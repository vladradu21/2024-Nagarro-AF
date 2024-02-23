package com.nagarro.af24.cinema.service;

import com.nagarro.af24.cinema.dto.ActorDTO;
import com.nagarro.af24.cinema.exception.CustomConflictException;
import com.nagarro.af24.cinema.exception.CustomNotFoundException;
import com.nagarro.af24.cinema.mapper.ActorMapper;
import com.nagarro.af24.cinema.model.Actor;
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
            throw new CustomConflictException("Actor already exists");
        }

        Actor actorToSave = actorMapper.toEntity(actorDTO);
        Actor savedActor = actorRepository.save(actorToSave);
        return actorMapper.toDTO(savedActor);
    }

    public ActorDTO getActor(String name) {
        Actor actor = actorRepository.findByName(name)
                .orElseThrow(() -> new CustomNotFoundException("Actor not found"));
        return actorMapper.toDTO(actor);
    }

    public void deleteActor(String name) {
        Actor actor = actorRepository.findByName(name)
                .orElseThrow(() -> new CustomNotFoundException("Actor not found"));
        actorRepository.delete(actor);
    }
}