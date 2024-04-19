package com.nagarro.af24.cinema.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nagarro.af24.cinema.dto.LoginDTO;
import com.nagarro.af24.cinema.dto.OtpDTO;
import com.nagarro.af24.cinema.dto.RegisterDTO;
import com.nagarro.af24.cinema.dto.ResponseDTO;
import com.nagarro.af24.cinema.dto.UserDTO;
import com.nagarro.af24.cinema.service.OtpService;
import com.nagarro.af24.cinema.utils.TestData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static java.lang.Thread.sleep;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthenticationControllerIntegrationTest extends BaseControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private OtpService otpService;

    @Test
    void register() throws Exception {
        // Arrange
        RegisterDTO registerDTO = TestData.getRegisterDTO();

        // Act and Assert
        mockMvc.perform(post("/auth/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void login() throws Exception {
        // Arrange
        LoginDTO loginDTO = TestData.getLoginDTO();

        RegisterDTO registerDTO = TestData.getRegisterDTO();
        mockMvc.perform(post("/auth/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isOk())
                .andReturn();

        // Act and Assert
        MvcResult result = mockMvc.perform(post("/auth/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andReturn();

        ResponseDTO responseDTO = objectMapper.readValue(result.getResponse().getContentAsString(), ResponseDTO.class);
        Assertions.assertNotNull(responseDTO);
        Assertions.assertNotNull(responseDTO.token());
        Assertions.assertEquals(responseDTO.user().username(), loginDTO.username());
    }

    @Test
    void forgotPassword() throws Exception {
        // Arrange
        RegisterDTO registerDTO = TestData.getRegisterDTO();

        mockMvc.perform(post("/auth/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isOk())
                .andReturn();

        // Act and Assert
        mockMvc.perform(post("/auth/forgot-password")
                        .param("email", registerDTO.email()))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void resetPassword() throws Exception {
        RegisterDTO registerDTO = new RegisterDTO("test", "data", "mevladradu@gmail.com", "testdata", "testdata");
        MvcResult result = mockMvc.perform(post("/auth/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isOk())
                .andReturn();
        UserDTO registeredUserDTO = objectMapper.readValue(result.getResponse().getContentAsString(), UserDTO.class);

        // Act and Assert
        mockMvc.perform(post("/auth/forgot-password")
                        .param("email", registeredUserDTO.email()))
                .andExpect(status().isOk())
                .andReturn();

        OtpDTO otp = otpService.getOtp(registeredUserDTO.email());

        mockMvc.perform(post("/auth/reset-password")
                        .param("email", registeredUserDTO.email())
                        .param("otp", String.valueOf(otp.otp()))
                        .param("newPassword", "newPassword"))
                .andExpect(status().isOk())
                .andReturn();
    }
}