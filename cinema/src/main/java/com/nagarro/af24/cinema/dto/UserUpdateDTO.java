package com.nagarro.af24.cinema.dto;

import java.util.List;

public record UserUpdateDTO(
        String firstname,
        String lastname,
        String email,
        String username,
        String password,
        List<String> roles
) {
}