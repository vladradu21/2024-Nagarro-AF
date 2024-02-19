package com.nagarro.af24.cinema.service;

import com.nagarro.af24.cinema.dto.ActorDTO;
import org.springframework.stereotype.Service;

@Service
public interface ActorService {
    ActorDTO addActor(ActorDTO actorDTO);

    ActorDTO getActor(String name);

    void deleteActor(String name);
}