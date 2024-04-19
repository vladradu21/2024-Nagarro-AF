package com.nagarro.af24.cinema.service;

import com.nagarro.af24.cinema.dto.OtpDTO;
import com.nagarro.af24.cinema.mapper.OtpMapper;
import com.nagarro.af24.cinema.model.ApplicationUser;
import com.nagarro.af24.cinema.model.Otp;
import com.nagarro.af24.cinema.repository.OtpRepository;
import com.nagarro.af24.cinema.utils.TestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@ExtendWith(MockitoExtension.class)
class OtpServiceTest {
    @Mock
    private OtpMapper otpMapper;
    @Mock
    private OtpRepository otpRepository;
    @Mock
    private Random random;
    @InjectMocks
    private OtpService otpService;

    @Captor
    private ArgumentCaptor<OtpDTO> otpDTOCaptor;

    @Test
    void generateOtp() {
        String email = "testdata@gmail.com";
        when(otpRepository.findByUserEmail(email)).thenReturn(Optional.empty());
        when(random.nextInt(900000)).thenReturn(23456);

        otpService.generateOtp(email);

        verify(otpMapper).toEntity(otpDTOCaptor.capture());
        OtpDTO capturedOtpDTO = otpDTOCaptor.getValue();

        assertEquals(email, capturedOtpDTO.email());
        assertEquals(123456, capturedOtpDTO.otp());  // 100000 + 23456
        assertTrue(capturedOtpDTO.expireAt().isAfter(LocalDateTime.now()));
    }

    @Test
    void getOtp() {
        Otp otp = TestData.getOtp();
        otp.setUser(TestData.getApplicationUser());
        when(otpRepository.findByUserEmail(otp.getUser().getEmail())).thenReturn(Optional.of(otp));
        OtpDTO otpDTO = TestData.getOtpDTO();
        when(otpMapper.toDTO(otp)).thenReturn(otpDTO);

        OtpDTO expected = otpService.getOtp(otp.getUser().getEmail());

        assertEquals(otpDTO, expected);
    }
}
