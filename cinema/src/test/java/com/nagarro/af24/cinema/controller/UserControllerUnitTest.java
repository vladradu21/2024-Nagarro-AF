package com.nagarro.af24.cinema.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nagarro.af24.cinema.dto.UserDTO;
import com.nagarro.af24.cinema.dto.UserUpdateDTO;
import com.nagarro.af24.cinema.service.UserService;
import com.nagarro.af24.cinema.utils.TestData;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
class UserControllerUnitTest {
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Mock
    private UserService userService;
    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void testGetUser() throws Exception {
        UserDTO userDTO = TestData.getUserDTO();
        String username = userDTO.username();
        when(userService.getUserByUsername(username)).thenReturn(userDTO);

        MvcResult mvcResult = mockMvc.perform(get("/users")
                        .param("username", username))
                .andExpect(status().isOk())
                .andReturn();

        UserDTO responseUserDTO = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), UserDTO.class);

        assertEquals(userDTO, responseUserDTO);
    }

    @Test
    void testUpdateUser() throws Exception {
        UserUpdateDTO userUpdateDTO = TestData.getUserUpdateDTO();
        UserDTO updatedUserDTO = TestData.getUserDTO();
        when(userService.updateUser(userUpdateDTO)).thenReturn(updatedUserDTO);

        MvcResult mvcResult = mockMvc.perform(put("/users")
                        .content(objectMapper.writeValueAsString(userUpdateDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        UserDTO responseUserDTO = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), UserDTO.class);

        assertEquals(updatedUserDTO, responseUserDTO);
    }

    @Test
    void testDeleteUser() throws Exception {
        UserDTO userDTO = TestData.getUserDTO();
        String username = userDTO.username();

        mockMvc.perform(delete("/users")
                        .param("username", username))
                .andExpect(status().isOk());

        verify(userService).deleteUser(username);
    }
}