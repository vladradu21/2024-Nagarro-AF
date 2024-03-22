package com.nagarro.af24.cinema.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nagarro.af24.cinema.dto.ActorDTO;
import com.nagarro.af24.cinema.dto.MovieDetailsDTO;
import com.nagarro.af24.cinema.service.ActorService;
import com.nagarro.af24.cinema.utils.TestData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ActorControllerUnitTest {
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Mock
    private ActorService actorService;
    @InjectMocks
    private ActorController actorController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(actorController).build();
    }

    @Test
    void testAddActor() throws Exception {
        ActorDTO actorDTO = TestData.getActorDTO();
        ActorDTO savedActorDTO = TestData.getActorDTO();
        when(actorService.addActor(actorDTO)).thenReturn(savedActorDTO);

        MvcResult mvcResult = mockMvc.perform(post("/actors")
                        .content(objectMapper.writeValueAsString(actorDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        Assertions.assertEquals(objectMapper.writeValueAsString(savedActorDTO), mvcResult.getResponse().getContentAsString());
    }

    @Test
    void testGetActor() throws Exception {
        ActorDTO actorDTO = TestData.getActorDTO();
        String name = actorDTO.name();
        when(actorService.getActor(name)).thenReturn(actorDTO);

        MvcResult mvcResult = mockMvc.perform(get("/actors")
                        .param("name", name))
                .andExpect(status().isOk())
                .andReturn();

        ActorDTO foundActorDTO = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ActorDTO.class);

        Assertions.assertEquals(actorDTO.name(), foundActorDTO.name());
        Assertions.assertEquals(actorDTO.age(), foundActorDTO.age());
    }

    @Test
    void testUpdateActor() throws Exception {
        ActorDTO actorDTO = TestData.getActorDTO();
        ActorDTO updatedActorDTO = TestData.getActorDTO();
        when(actorService.updateActor(actorDTO)).thenReturn(updatedActorDTO);

        MvcResult mvcResult = mockMvc.perform(put("/actors")
                        .content(objectMapper.writeValueAsString(actorDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        ActorDTO foundUpdatedActorDTO = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ActorDTO.class);

        Assertions.assertEquals(updatedActorDTO.name(), foundUpdatedActorDTO.name());
        Assertions.assertEquals(updatedActorDTO.age(), foundUpdatedActorDTO.age());
        Assertions.assertEquals(updatedActorDTO.gender(), foundUpdatedActorDTO.gender());
    }

    @Test
    void testAssignActorsToMovie() throws Exception {
        MovieDetailsDTO movieDetailsDTO = TestData.getMovieDetailsDTO();
        String title = movieDetailsDTO.movie().title();
        int year = movieDetailsDTO.movie().year();
        List<ActorDTO> actorDTOS = movieDetailsDTO.actors();
        List<String> actorsNames = actorDTOS.stream().map(ActorDTO::name).toList();
        when(actorService.assignActorsToMovie(title, year, actorsNames)).thenReturn(movieDetailsDTO);

        MvcResult mvcResult = mockMvc.perform(put("/actors/assign-to-movie")
                        .param("movieTitle", title)
                        .param("year", String.valueOf(year))
                        .param("actorsNames", actorsNames.toArray(String[]::new)))
                .andExpect(status().isOk())
                .andReturn();

        MovieDetailsDTO foundMovieDetailsDTO = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), MovieDetailsDTO.class);

        Assertions.assertEquals(movieDetailsDTO.movie().title(), foundMovieDetailsDTO.movie().title());
        Assertions.assertEquals(movieDetailsDTO.movie().year(), foundMovieDetailsDTO.movie().year());
        Assertions.assertEquals(movieDetailsDTO.actors().size(), foundMovieDetailsDTO.actors().size());
    }

    @Test
    void testDeleteActor() throws Exception {
        ActorDTO actorDTO = TestData.getActorDTO();
        String name = actorDTO.name();

        mockMvc.perform(delete("/actors")
                        .param("name", name))
                .andExpect(status().isOk());

        Mockito.verify(actorService).deleteActor(name);
    }
}