package com.nagarro.af24.cinema.service.impl;

import com.nagarro.af24.cinema.dto.ActorDTO;
import com.nagarro.af24.cinema.exception.CustomConflictException;
import com.nagarro.af24.cinema.exception.CustomNotFoundException;
import com.nagarro.af24.cinema.mapper.ActorMapper;
import com.nagarro.af24.cinema.model.Actor;
import com.nagarro.af24.cinema.repository.ActorRepository;
import com.nagarro.af24.cinema.service.ActorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActorServiceImpl implements ActorService {
    private final ActorRepository actorRepository;
    private final ActorMapper actorMapper;

    @Autowired
    public ActorServiceImpl(ActorRepository actorRepository, ActorMapper actorMapper) {
        this.actorRepository = actorRepository;
        this.actorMapper = actorMapper;
    }

    @Override
    public ActorDTO addActor(ActorDTO actorDTO) {
        if (actorRepository.findByName(actorDTO.name()).isPresent()) {
            throw new CustomConflictException("Actor already exists");
        }

        Actor actorToSave = actorMapper.toEntity(actorDTO);
        Actor savedActor = actorRepository.save(actorToSave);
        return actorMapper.toDTO(savedActor);
    }

    @Override
    public ActorDTO getActor(String name) {
        Actor actor = actorRepository.findByName(name)
                .orElseThrow(() -> new CustomNotFoundException("Actor not found"));
        return actorMapper.toDTO(actor);
    }

    @Override
    public void deleteActor(String name) {
        Actor actor = actorRepository.findByName(name)
                .orElseThrow(() -> new CustomNotFoundException("Actor not found"));
        actorRepository.delete(actor);
    }
}