package com.nagarro.af24.cinema.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nagarro.af24.cinema.dto.MovieDTO;
import com.nagarro.af24.cinema.dto.ReviewDTO;
import com.nagarro.af24.cinema.service.ReviewService;
import com.nagarro.af24.cinema.utils.TestData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ReviewControllerUnitTest {
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Mock
    private ReviewService reviewService;
    @InjectMocks
    private ReviewController reviewController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(reviewController).build();
    }

    @Test
    void testAddReview() throws Exception {
        //Arrange
        ReviewDTO reviewDTO = TestData.getReviewDTO();
        ReviewDTO savedReviewDTO = TestData.getReviewDTO();
        when(reviewService.addReview(reviewDTO)).thenReturn(savedReviewDTO);

        //Act and Assert
        mockMvc.perform(post("/reviews")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(reviewDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void testGetReviews() throws Exception {
        //Arrange
        List<ReviewDTO> reviewDTOS = TestData.getReviewDTOs();
        MovieDTO movieDTO = TestData.getMovieDTO();
        when(reviewService.getReviews(movieDTO.title(), movieDTO.year())).thenReturn(reviewDTOS);

        //Act
        MvcResult mvcResult = mockMvc.perform(get("/reviews")
                        .param("movieTitle", movieDTO.title())
                        .param("movieProductionYear", String.valueOf(movieDTO.year())))
                .andExpect(status().isOk())
                .andReturn();

        List<ReviewDTO> foundReviewDTOS = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), List.class);

        //Assert
        Assertions.assertEquals(reviewDTOS.size(), foundReviewDTOS.size());
    }

    @Test
    void testUpdateReview() throws Exception {
        //Arrange
        ReviewDTO reviewDTO = TestData.getReviewDTO();
        ReviewDTO updatedReviewDTO = TestData.getReviewDTO();
        when(reviewService.updateReview(reviewDTO)).thenReturn(updatedReviewDTO);

        //Act and Assert
        mockMvc.perform(put("/reviews")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(reviewDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteReview() throws Exception {
        //Arrange
        ReviewDTO reviewDTO = TestData.getReviewDTO();

        //Act and Assert
        mockMvc.perform(delete("/reviews")
                        .param("movieTitle", reviewDTO.movieTitle())
                        .param("movieProductionYear", String.valueOf(reviewDTO.movieProductionYear()))
                        .param("username", reviewDTO.username())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(reviewService).deleteReview(reviewDTO.movieTitle(), reviewDTO.movieProductionYear(), reviewDTO.username());
    }
}