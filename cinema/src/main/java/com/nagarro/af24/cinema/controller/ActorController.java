package com.nagarro.af24.cinema.controller;

import com.nagarro.af24.cinema.dto.ActorDTO;
import com.nagarro.af24.cinema.service.ActorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/actors")
public class ActorController {
    private final ActorService actorService;

    @Autowired
    public ActorController(ActorService actorService) {
        this.actorService = actorService;
    }

    @PostMapping
    public ActorDTO addActor(@Valid @RequestBody ActorDTO actorDTO) {
        return actorService.addActor(actorDTO);
    }

    @GetMapping
    public ActorDTO getActor(@RequestParam String name) {
        return actorService.getActor(name);
    }

    @DeleteMapping
    public void deleteActor(@RequestParam String name) {
        actorService.deleteActor(name);
    }
}