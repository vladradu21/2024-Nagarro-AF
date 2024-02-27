package com.nagarro.af24.cinema.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nagarro.af24.cinema.dto.MovieDTO;
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
class MovieControllerIntegrationTest extends AbstractMySQLContainer {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void addMovie() throws Exception {
        //Arrange
        MovieDTO movieToSave = TestData.getMovieDTO();

        //Act and Assert
        mockMvc.perform(post("/movies")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(movieToSave)))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void addMovieWithWrongGenre() throws Exception {
        //Arrange
        MovieDTO movieToSave = TestData.getMovieDTOWithWrongGenre();

        //Act and Assert
        mockMvc.perform(post("/movies")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(movieToSave)))
                .andExpect(status().isNotFound());
    }

    @Test
    void getMovie() throws Exception {
        //Arrange
        MovieDTO movieToSave = TestData.getMovieDTO();

        //Act
        mockMvc.perform(post("/movies")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(movieToSave)))
                .andExpect(status().isOk());

        MvcResult mvcResult = mockMvc.perform(get("/movies")
                        .param("title", movieToSave.title())
                        .param("year", String.valueOf(movieToSave.year())))
                .andExpect(status().isOk())
                .andReturn();

        //Assert
        MovieDTO foundMovie = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), MovieDTO.class);
        Assertions.assertEquals(movieToSave.title(), foundMovie.title());
        Assertions.assertEquals(movieToSave.year(), foundMovie.year());
    }

    @Test
    void getMovieNotFound() throws Exception {
        //Act and Assert
        mockMvc.perform(get("/movies")
                        .param("title", "Not Found")
                        .param("year", String.valueOf(2021)))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void deleteMovie() throws Exception {
        //Arrange
        MovieDTO movieToSave = TestData.getMovieDTO();

        //Act and Assert
        mockMvc.perform(post("/movies")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(movieToSave)))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/movies")
                        .param("title", movieToSave.title())
                        .param("year", String.valueOf(movieToSave.year())))
                .andExpect(status().isOk())
                .andReturn();

        mockMvc.perform(get("/movies")
                        .param("title", movieToSave.title())
                        .param("year", String.valueOf(movieToSave.year())))
                .andExpect(status().isNotFound())
                .andReturn();
    }
}