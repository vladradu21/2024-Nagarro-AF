package com.nagarro.af24.cinema.service;

import com.nagarro.af24.cinema.dto.OtpDTO;
import com.nagarro.af24.cinema.exception.CustomConflictException;
import com.nagarro.af24.cinema.exception.CustomNotFoundException;
import com.nagarro.af24.cinema.mapper.OtpMapper;
import com.nagarro.af24.cinema.model.Otp;
import com.nagarro.af24.cinema.repository.OtpRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@EnableScheduling
@Service
public class OtpService {
    private final OtpMapper otpMapper;
    private final OtpRepository otpRepository;
    private final Random random;

    @Autowired
    public OtpService(OtpMapper otpMapper, OtpRepository otpRepository, Random random) {
        this.otpMapper = otpMapper;
        this.otpRepository = otpRepository;
        this.random = random;
    }

    public OtpDTO generateOtp(String email) {
        if(otpRepository.findByUserEmail(email).isPresent()) {
            throw new CustomConflictException("Otp already exists for email: " + email);
        }

        OtpDTO otpDTO = new OtpDTO(
                email,
                randomOtp(),
                LocalDateTime.now().plusSeconds(60)
        );

        Otp savedOtp = otpRepository.save(otpMapper.toEntity(otpDTO));
        return otpMapper.toDTO(savedOtp);
    }

    private int randomOtp() {
        return 100000 + random.nextInt(900000);
    }

    @Transactional
    public OtpDTO getOtp(String email) {
        return otpMapper.toDTO(otpRepository.findByUserEmail(email).orElseThrow(
                () -> new CustomNotFoundException("Otp not found for email: " + email)
        ));
    }

    @Scheduled(fixedRate = 3000)
    void deleteExpiredOtp() {
        otpRepository.deleteByExpireAtIsBefore(LocalDateTime.now());
    }
}