package com.nagarro.af24.cinema.controller;

import com.nagarro.af24.cinema.dto.ActorDTO;
import com.nagarro.af24.cinema.dto.MovieDetailsDTO;
import com.nagarro.af24.cinema.service.ActorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/actors")
public class ActorController implements ActorApi{
    private final ActorService actorService;

    @Autowired
    public ActorController(ActorService actorService) {
        this.actorService = actorService;
    }

    @Override
    public ActorDTO addActor(@Valid @RequestBody ActorDTO actorDTO) {
        return actorService.addActor(actorDTO);
    }

    @Override
    public ActorDTO getActor(@RequestParam String name) {
        return actorService.getActor(name);
    }

    @Override
    public ActorDTO updateActor(@Valid @RequestBody ActorDTO actorDTO) {
        return actorService.updateActor(actorDTO);
    }

    @Override
    public MovieDetailsDTO assignActorsToMovie(@RequestParam String movieTitle, @RequestParam int year, @RequestParam List<String> actorsNames) {
        return actorService.assignActorsToMovie(movieTitle, year, actorsNames);
    }

    @Override
    public void deleteActor(@RequestParam String name) {
        actorService.deleteActor(name);
    }
}