package com.nagarro.af24.cinema.controller;

import com.nagarro.af24.cinema.dto.ActorDTO;
import com.nagarro.af24.cinema.dto.MovieDetailsDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface ActorApi {
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
    ActorDTO addActor(@Valid @RequestBody ActorDTO actorDTO);

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
    ActorDTO getActor(@RequestParam String name);

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
    ActorDTO updateActor(@Valid @RequestBody ActorDTO actorDTO);

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
    MovieDetailsDTO assignActorsToMovie(@RequestParam String movieTitle, @RequestParam int year, @RequestParam List<String> actorsNames);

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
    void deleteActor(@RequestParam String name);
}