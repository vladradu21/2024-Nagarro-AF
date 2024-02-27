package com.nagarro.af24.cinema.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nagarro.af24.cinema.dto.ActorDTO;
import com.nagarro.af24.cinema.repository.AbstractMySQLContainer;
import com.nagarro.af24.cinema.utils.TestData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("inttest")
class ActorControllerIntegrationTest extends AbstractMySQLContainer {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void addActor() throws Exception {
        //Arrange
        ActorDTO actorToSave = TestData.getActorDTO();

        //Act and Assert
        mockMvc.perform(post("/actors")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(actorToSave)))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void addActorWithWrongCountry() throws Exception {
        //Arrange
        ActorDTO actorToSave = TestData.getActorDTOWithWrongCountry();

        //Act and Assert
        mockMvc.perform(post("/actors")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(actorToSave)))
                .andExpect(status().isNotFound());
    }

    @Test
    void getActor() throws Exception {
        //Arrange
        ActorDTO actorToSave = TestData.getActorDTO();

        //Act
        mockMvc.perform(post("/actors")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(actorToSave)))
                .andExpect(status().isOk())
                .andReturn();

        MvcResult mvcResult = mockMvc.perform(get("/actors")
                        .param("name", actorToSave.name()))
                .andExpect(status().isOk())
                .andReturn();

        //Assert
        ActorDTO foundActor = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ActorDTO.class);
        Assertions.assertEquals(foundActor.name(), actorToSave.name());
    }

    @Test
    void getActorNotFound() throws Exception {
        //Act and Assert
        mockMvc.perform(get("/actors")
                        .param("name", "Not Found"))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void deleteActor() throws Exception {
        //Arrange
        ActorDTO actorToSave = TestData.getActorDTO();

        //Act and Assert
        mockMvc.perform(post("/actors")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(actorToSave)))
                .andExpect(status().isOk())
                .andReturn();

        mockMvc.perform(get("/actors")
                        .param("name", actorToSave.name()))
                .andExpect(status().isOk())
                .andReturn();

        mockMvc.perform(delete("/actors")
                        .param("name", actorToSave.name()))
                .andExpect(status().isOk())
                .andReturn();
    }
}