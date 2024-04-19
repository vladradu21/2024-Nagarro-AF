package com.nagarro.af24.cinema.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nagarro.af24.cinema.dto.MovieDTO;
import com.nagarro.af24.cinema.dto.ReviewDTO;
import com.nagarro.af24.cinema.utils.TestData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ReviewControllerIntegrationTest extends BaseControllerIntegrationTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    @WithMockUser(value = "spring", roles = {"ADMIN"})
    void setUp() {
        MovieDTO movieDTO = TestData.getMovieDTO();
        try {
            mockMvc.perform(post("/movies")
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(movieDTO)))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @WithMockUser(value = "spring", roles = {"ADMIN"})
    void addReview() throws Exception {
        //Arrange
        ReviewDTO reviewDTO = TestData.getReviewDTO();

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
        List<ReviewDTO> reviewDTOS = TestData.getReviewDTOs();

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
        Assertions.assertEquals(reviewDTOS.size(), foundReviews.size());
    }
}