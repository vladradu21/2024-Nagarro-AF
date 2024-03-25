package com.nagarro.af24.cinema.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nagarro.af24.cinema.dto.LoginDTO;
import com.nagarro.af24.cinema.dto.RegisterDTO;
import com.nagarro.af24.cinema.dto.ResponseDTO;
import com.nagarro.af24.cinema.dto.UserDTO;
import com.nagarro.af24.cinema.service.AuthenticationService;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
class AuthenticationControllerUnitTest {
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Mock
    private AuthenticationService authenticationService;
    @InjectMocks
    private AuthenticationController authenticationController;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(authenticationController).build();
    }

    @Test
    void testRegister() throws Exception {
        RegisterDTO registerDTO = TestData.getRegisterDTO();
        UserDTO userDTO = TestData.getUserDTO();
        when(authenticationService.register(registerDTO)).thenReturn(userDTO);

        MvcResult mvcResult = mockMvc.perform(post("/auth/register")
                        .content(objectMapper.writeValueAsString(registerDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        UserDTO response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), UserDTO.class);
        Assertions.assertEquals(userDTO, response);
    }

    @Test
    void testLogin() throws Exception {
        LoginDTO loginDTO = TestData.getLoginDTO();
        UserDTO userDTO = TestData.getUserDTO();
        String token = "valid-token";
        ResponseDTO responseDTO = new ResponseDTO(userDTO, token);
        when(authenticationService.login(loginDTO)).thenReturn(responseDTO);

        MvcResult mvcResult = mockMvc.perform(post("/auth/login")
                        .content(objectMapper.writeValueAsString(loginDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        ResponseDTO response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ResponseDTO.class);
        Assertions.assertEquals(responseDTO, response);
    }
}