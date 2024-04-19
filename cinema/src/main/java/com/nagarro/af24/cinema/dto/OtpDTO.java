package com.nagarro.af24.cinema.dto;

import java.time.LocalDateTime;

public record OtpDTO(
        String email,
        int otp,
        LocalDateTime expireAt
) {
}