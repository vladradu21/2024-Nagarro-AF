package com.nagarro.af24.cinema.controller;

import com.nagarro.af24.cinema.dto.ActorDTO;
import com.nagarro.af24.cinema.dto.MovieActorsDTO;
import com.nagarro.af24.cinema.dto.MovieDetailsDTO;
import com.nagarro.af24.cinema.service.ActorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/actors")
public class ActorController {
    private final ActorService actorService;

    @Autowired
    public ActorController(ActorService actorService) {
        this.actorService = actorService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Add a new actor", description = "Add a new actor to the database", tags = {"Actors"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Actor added successfully",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ActorDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Error.class)))
            }
    )
    @PostMapping
    public ActorDTO addActor(@Valid @RequestBody ActorDTO actorDTO) {
        return actorService.addActor(actorDTO);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Get an actor", description = "Get an actor from the database", tags = {"Actors"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Actor found",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ActorDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Error.class))),
                    @ApiResponse(responseCode = "404", description = "Actor not found",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Error.class)))
            }
    )
    @GetMapping
    public ActorDTO getActor(@RequestParam String name) {
        return actorService.getActor(name);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update an actor", description = "Update an actor in the database", tags = {"Actors"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Actor updated successfully",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ActorDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Error.class))),
                    @ApiResponse(responseCode = "404", description = "Actor not found",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Error.class)))
            }
    )
    @PutMapping
    public ActorDTO updateActor(@Valid @RequestBody ActorDTO actorDTO) {
        return actorService.updateActor(actorDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Assign actors to a movie", description = "Assign actors to a movie in the database", tags = {"Actors"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Actors assigned successfully",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = MovieDetailsDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Error.class))),
                    @ApiResponse(responseCode = "404", description = "Movie not found",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Error.class)))
            }
    )
    @PutMapping("/assign-to-movie")
    public MovieActorsDTO assignActorsToMovie(@RequestParam String movieTitle, @RequestParam int year, @RequestParam List<String> actorsNames) {
        return actorService.assignActorsToMovie(movieTitle, year, actorsNames);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete an actor", description = "Delete an actor from the database", tags = {"Actors"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Actor deleted successfully",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ActorDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Error.class))),
                    @ApiResponse(responseCode = "404", description = "Actor not found",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Error.class)))
            }
    )
    @DeleteMapping
    public void deleteActor(@RequestParam String name) {
        actorService.deleteActor(name);
    }
}