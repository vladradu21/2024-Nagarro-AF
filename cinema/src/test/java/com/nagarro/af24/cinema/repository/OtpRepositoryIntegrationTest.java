package com.nagarro.af24.cinema.repository;

import com.nagarro.af24.cinema.model.ApplicationUser;
import com.nagarro.af24.cinema.model.Otp;
import com.nagarro.af24.cinema.utils.TestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class OtpRepositoryIntegrationTest extends BaseRepositoryIntegrationTest {
    private ApplicationUser savedUser;
    @Autowired
    private OtpRepository otpRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        ApplicationUser userToSave = TestData.getApplicationUser();
        savedUser = userRepository.save(userToSave);
    }

    @Test
    void testFindByUserEmail() {
        //Arrange
        Otp otpToSave = TestData.getOtp();
        otpToSave.setUser(savedUser);
        Otp savedOtp = otpRepository.save(otpToSave);

        //Act
        Otp foundOtp = otpRepository.findByUserEmail(otpToSave.getUser().getEmail()).orElse(null);

        //Assert
        assertNotNull(foundOtp);
        assertEquals(savedOtp.getId(), foundOtp.getId());
        assertEquals(savedOtp.getUser().getEmail(), foundOtp.getUser().getEmail());
    }

    @Test
    void testDeleteByExpireAtIsBefore() {
        //Arrange
        Otp otpToSave = TestData.getOtp();
        otpToSave.setUser(savedUser);
        otpRepository.save(otpToSave);

        //Act
        otpRepository.deleteByExpireAtIsBefore(otpToSave.getExpireAt().plusSeconds(1));

        //Assert
        assertEquals(0, otpRepository.findAll().size());
    }
}