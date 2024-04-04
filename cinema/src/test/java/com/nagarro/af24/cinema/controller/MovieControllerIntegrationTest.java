package com.nagarro.af24.cinema.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nagarro.af24.cinema.dto.MovieDTO;
import com.nagarro.af24.cinema.utils.TestData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MovieControllerIntegrationTest extends BaseControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(value = "spring", roles = {"ADMIN"})
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
    @WithMockUser(value = "spring", roles = {"ADMIN"})
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
    @WithMockUser(value = "spring", roles = {"ADMIN"})
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
    @WithMockUser(value = "spring", roles = {"ADMIN"})
    void getMovieNotFound() throws Exception {
        //Act and Assert
        mockMvc.perform(get("/movies")
                        .param("title", "Not Found")
                        .param("year", String.valueOf(2021)))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @WithMockUser(value = "spring", roles = {"ADMIN"})
    void updateMovie() throws Exception {
        //Arrange
        MovieDTO movieToSave = TestData.getMovieDTO();

        //Act and Assert
        mockMvc.perform(post("/movies")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(movieToSave)))
                .andExpect(status().isOk());

        MovieDTO movieToUpdate = TestData.getUpdatedMovieDTO();
        mockMvc.perform(put("/movies")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(movieToUpdate)))
                .andExpect(status().isOk());

        MvcResult mvcResult = mockMvc.perform(get("/movies")
                        .param("title", movieToUpdate.title())
                        .param("year", String.valueOf(movieToUpdate.year())))
                .andExpect(status().isOk())
                .andReturn();

        MovieDTO updatedMovie = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), MovieDTO.class);
        Assertions.assertEquals(movieToUpdate.title(), updatedMovie.title());
        Assertions.assertEquals(movieToUpdate.year(), updatedMovie.year());
        Assertions.assertEquals(movieToUpdate.genres(), updatedMovie.genres());
        Assertions.assertEquals(movieToUpdate.score(), updatedMovie.score());
    }

    @Test
    @WithMockUser(value = "spring", roles = {"ADMIN"})
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