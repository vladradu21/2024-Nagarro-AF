package com.nagarro.af24.cinema.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nagarro.af24.cinema.dto.MovieDTO;
import com.nagarro.af24.cinema.dto.RegisterDTO;
import com.nagarro.af24.cinema.dto.ReviewDTO;
import com.nagarro.af24.cinema.dto.UserDTO;
import com.nagarro.af24.cinema.utils.TestData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ReviewControllerIntegrationTest extends BaseControllerIntegrationTest {
    private List<MovieDTO> moviesDTO;
    private UserDTO userDTO;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    @WithMockUser(value = "spring", roles = {"ADMIN"})
    void setUp() throws Exception {
        moviesDTO = TestData.getMoviesDTO();
        moviesDTO.forEach(movieDTO -> {
            try {
                mockMvc.perform(post("/movies")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(movieDTO)))
                        .andExpect(status().isOk());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        RegisterDTO registerDTO = TestData.getRegisterDTO();
        MvcResult result = mockMvc.perform(post("/auth/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isOk())
                .andReturn();

        userDTO = objectMapper.readValue(result.getResponse().getContentAsString(), UserDTO.class);
    }

    @Test
    @WithMockUser(value = "spring", roles = {"ADMIN"})
    void addReview() throws Exception {
        //Arrange
        ReviewDTO reviewDTO = new ReviewDTO(
                "Title",
                9.5,
                "description",
                moviesDTO.get(0).title(),
                moviesDTO.get(0).year(),
                userDTO.username()
        );

        //Act and Assert
        mockMvc.perform(post("/reviews")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(reviewDTO)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "spring", roles = {"ADMIN"})
    void getReviews() throws Exception {
        //Arrange
        List<ReviewDTO> reviewDTOS = new ArrayList<>(
                List.of(
                        new ReviewDTO(
                                "Title1",
                                9.5,
                                "description1",
                                moviesDTO.get(0).title(),
                                moviesDTO.get(0).year(),
                                userDTO.username()
                        ),
                        new ReviewDTO(
                                "Title2",
                                9.5,
                                "description2",
                                moviesDTO.get(1).title(),
                                moviesDTO.get(1).year(),
                                userDTO.username()
                        )
                )
        );

        reviewDTOS.forEach(reviewDTO -> {
            try {
                mockMvc.perform(post("/reviews")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(reviewDTO)))
                        .andExpect(status().isOk());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        //Act
        MvcResult mvcResult = mockMvc.perform(get("/reviews")
                        .param("movieTitle", reviewDTOS.get(0).movieTitle())
                        .param("movieProductionYear", String.valueOf(reviewDTOS.get(0).movieProductionYear())))
                .andExpect(status().isOk())
                .andReturn();
        List<ReviewDTO> foundReviews = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), List.class);

        //Assert
        Assertions.assertEquals(1, foundReviews.size());
    }

    @Test
    @WithMockUser(value = "spring", roles = {"ADMIN"})
    void updateReview() throws Exception {
        //Arrange
        ReviewDTO reviewDTO = new ReviewDTO(
                "Title",
                9.5,
                "description",
                moviesDTO.get(0).title(),
                moviesDTO.get(0).year(),
                userDTO.username()
        );

        mockMvc.perform(post("/reviews")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(reviewDTO)))
                .andExpect(status().isOk());

        ReviewDTO updatedReviewDTO = new ReviewDTO(
                "Updated Title",
                9.5,
                "updated description",
                moviesDTO.get(0).title(),
                moviesDTO.get(0).year(),
                userDTO.username()
        );

        //Act
        MvcResult result = mockMvc.perform(put("/reviews")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updatedReviewDTO)))
                .andExpect(status().isOk())
                .andReturn();

        ReviewDTO updatedReview = objectMapper.readValue(result.getResponse().getContentAsString(), ReviewDTO.class);

        //Assert
        Assertions.assertEquals(updatedReviewDTO.title(), updatedReview.title());
        Assertions.assertEquals(updatedReviewDTO.description(), updatedReview.description());
    }

    @Test
    @WithMockUser(value = "spring", roles = {"ADMIN"})
    void deleteReview() throws Exception {
        //Arrange
        ReviewDTO reviewDTO = new ReviewDTO(
                "Title",
                9.5,
                "description",
                moviesDTO.get(0).title(),
                moviesDTO.get(0).year(),
                userDTO.username()
        );

        mockMvc.perform(post("/reviews")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(reviewDTO)))
                .andExpect(status().isOk());

        //Act
        mockMvc.perform(delete("/reviews")
                        .param("movieTitle", reviewDTO.movieTitle())
                        .param("movieProductionYear", String.valueOf(reviewDTO.movieProductionYear()))
                        .param("username", reviewDTO.username()))
                .andExpect(status().isOk());

        //Assert
        MvcResult mvcResult = mockMvc.perform(get("/reviews")
                        .param("movieTitle", reviewDTO.movieTitle())
                        .param("movieProductionYear", String.valueOf(reviewDTO.movieProductionYear())))
                .andExpect(status().isOk())
                .andReturn();

        List<ReviewDTO> foundReviews = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), List.class);
        Assertions.assertEquals(0, foundReviews.size());
    }
}