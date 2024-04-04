package com.nagarro.af24.cinema.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nagarro.af24.cinema.dto.RegisterDTO;
import com.nagarro.af24.cinema.dto.UserDTO;
import com.nagarro.af24.cinema.dto.UserUpdateDTO;
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

class UserControllerIntegrationTest extends BaseControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(value = "spring", roles = {"ADMIN"})
    void getUser() throws Exception {
        // Arrange
        RegisterDTO registerDTO = TestData.getRegisterDTO();

        mockMvc.perform(post("/auth/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isOk())
                .andReturn();

        //Act
        MvcResult mvcResult = mockMvc.perform(get("/users")
                        .param("username", registerDTO.username()))
                .andExpect(status().isOk())
                .andReturn();

        //Assert
        UserDTO userDTO = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), UserDTO.class);
        Assertions.assertNotNull(userDTO);
        Assertions.assertEquals(userDTO.username(), registerDTO.username());
    }

    @Test
    @WithMockUser(value = "spring", roles = {"ADMIN"})
    void getUserNotFound() throws Exception {
        //Act
        MvcResult mvcResult = mockMvc.perform(get("/users")
                        .param("username", "notfound"))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @WithMockUser(value = "spring", roles = {"ADMIN"})
    void testUpdateUser() throws Exception {
        // Arrange
        RegisterDTO registerDTO = TestData.getRegisterDTO();

        mockMvc.perform(post("/auth/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isOk())
                .andReturn();

        MvcResult mvcResult = mockMvc.perform(get("/users")
                        .param("username", registerDTO.username()))
                .andExpect(status().isOk())
                .andReturn();

        UserDTO userDTO = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), UserDTO.class);

        //Act
        UserUpdateDTO userUpdateDTO = TestData.getUserUpdateDTODifferentData();

        mockMvc.perform(put("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userUpdateDTO)))
                .andExpect(status().isOk())
                .andReturn();

        mvcResult = mockMvc.perform(get("/users")
                        .param("username", registerDTO.username()))
                .andExpect(status().isOk())
                .andReturn();

        UserDTO updatedUserDTO = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), UserDTO.class);

        //Assert
        Assertions.assertNotNull(userDTO);
        Assertions.assertNotNull(updatedUserDTO);
        Assertions.assertEquals(userDTO.username(), updatedUserDTO.username());
        Assertions.assertNotEquals(userDTO.firstname(), updatedUserDTO.firstname());
        Assertions.assertNotEquals(userDTO.lastname(), updatedUserDTO.lastname());
        Assertions.assertNotEquals(userDTO.email(), updatedUserDTO.email());
    }


    @Test
    @WithMockUser(value = "spring", roles = {"ADMIN"})
    void testDeleteUser() throws Exception {
        // Arrange
        RegisterDTO registerDTO = TestData.getRegisterDTO();

        mockMvc.perform(post("/auth/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isOk())
                .andReturn();

        //Act
        mockMvc.perform(get("/users")
                        .param("username", registerDTO.username()))
                .andExpect(status().isOk())
                .andReturn();

        mockMvc.perform(delete("/users")
                        .param("username", registerDTO.username()))
                .andExpect(status().isOk())
                .andReturn();

        //Assert
        mockMvc.perform(get("/users")
                        .param("username", registerDTO.username()))
                .andExpect(status().isNotFound())
                .andReturn();
    }
}