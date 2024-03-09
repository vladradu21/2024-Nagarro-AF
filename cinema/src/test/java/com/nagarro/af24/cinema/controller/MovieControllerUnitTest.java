package com.nagarro.af24.cinema.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nagarro.af24.cinema.dto.MovieDTO;
import com.nagarro.af24.cinema.dto.MovieDetailsDTO;
import com.nagarro.af24.cinema.service.MovieService;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class MovieControllerUnitTest {
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Mock
    private MovieService movieService;
    @InjectMocks
    private MovieController movieController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(movieController).build();
    }

    @Test
    void testAddMovie() throws Exception {
        MovieDTO movieDTO = TestData.getMovieDTO();
        MovieDTO savedMovieDTO = TestData.getMovieDTO();
        when(movieService.addMovie(movieDTO)).thenReturn(savedMovieDTO);

        MvcResult mvcResult = mockMvc.perform(post("/movies")
                        .content(objectMapper.writeValueAsString(movieDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        Assertions.assertEquals(objectMapper.writeValueAsString(savedMovieDTO), mvcResult.getResponse().getContentAsString());
    }

    @Test
    void testGetMovie() throws Exception {
        MovieDTO movieDTO = TestData.getMovieDTO();
        String title = movieDTO.title();
        int year = movieDTO.year();
        when(movieService.getMovie(title, year)).thenReturn(movieDTO);

        MvcResult mvcResult = mockMvc.perform(get("/movies")
                        .param("title", title)
                        .param("year", String.valueOf(year)))
                .andExpect(status().isOk())
                .andReturn();

        MovieDTO foundMovieDTO = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), MovieDTO.class);

        Assertions.assertEquals(movieDTO.title(), foundMovieDTO.title());
        Assertions.assertEquals(movieDTO.year(), foundMovieDTO.year());
    }

    @Test
    void testGetMovieDetails() throws Exception {
        MovieDTO movieDTO = TestData.getMovieDTO();
        String title = movieDTO.title();
        int year = movieDTO.year();
        MovieDetailsDTO movieDetailsDTO = TestData.getMovieDetailsDTO();
        when(movieService.getMovieDetails(title, year)).thenReturn(movieDetailsDTO);

        MvcResult mvcResult = mockMvc.perform(get("/movies/with-details")
                        .param("movieTitle", title)
                        .param("year", String.valueOf(year)))
                .andExpect(status().isOk())
                .andReturn();

        MovieDetailsDTO foundMovieDetailsDTO = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), MovieDetailsDTO.class);

        Assertions.assertEquals(movieDetailsDTO.movie().title(), foundMovieDetailsDTO.movie().title());
        Assertions.assertEquals(movieDetailsDTO.movie().year(), foundMovieDetailsDTO.movie().year());
    }

    @Test
    void testUpdateMovie() throws Exception {
        MovieDTO movieDTO = TestData.getMovieDTO();
        MovieDTO updatedMovieDTO = TestData.getUpdatedMovieDTO();
        when(movieService.updateMovie(movieDTO)).thenReturn(updatedMovieDTO);

        MvcResult mvcResult = mockMvc.perform(put("/movies")
                        .content(objectMapper.writeValueAsString(movieDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        MovieDTO foundUpdatedMovieDTO = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), MovieDTO.class);

        Assertions.assertEquals(updatedMovieDTO.title(), foundUpdatedMovieDTO.title());
        Assertions.assertEquals(updatedMovieDTO.year(), foundUpdatedMovieDTO.year());
        Assertions.assertEquals(updatedMovieDTO.genres(), foundUpdatedMovieDTO.genres());
        Assertions.assertEquals(updatedMovieDTO.score(), foundUpdatedMovieDTO.score());
    }

    @Test
    void testDeleteMovie() throws Exception {
        MovieDTO movieDTO = TestData.getMovieDTO();
        String title = movieDTO.title();
        int year = movieDTO.year();

        mockMvc.perform(delete("/movies")
                        .param("title", title)
                        .param("year", String.valueOf(year))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.verify(movieService).deleteMovie(title, year);
    }
}