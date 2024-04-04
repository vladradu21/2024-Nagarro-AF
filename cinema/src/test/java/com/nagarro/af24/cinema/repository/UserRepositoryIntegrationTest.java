package com.nagarro.af24.cinema.repository;

import com.nagarro.af24.cinema.model.ApplicationUser;
import com.nagarro.af24.cinema.utils.TestData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class UserRepositoryIntegrationTest extends BaseRepositoryIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindByUsername() {
        // arrange
        ApplicationUser userToSave = TestData.getApplicationUser();
        ApplicationUser savedUser = userRepository.save(userToSave);

        // act
        ApplicationUser foundUser = userRepository.findByUsername(userToSave.getUsername()).orElse(null);

        // assert
        Assertions.assertEquals(savedUser.getUsername(), foundUser.getUsername());
    }

    @Test
    void testFindByUsernameNotFound() {
        // act
        ApplicationUser foundUser = userRepository.findByUsername("Not Found").orElse(null);

        // assert
        Assertions.assertNull(foundUser);
    }

    @Test
    void testFindByEmail() {
        // arrange
        ApplicationUser userToSave = TestData.getApplicationUser();
        ApplicationUser savedUser = userRepository.save(userToSave);

        // act
        ApplicationUser foundUser = userRepository.findByEmail(userToSave.getEmail()).orElse(null);

        // assert
        Assertions.assertNotNull(foundUser);
        Assertions.assertEquals(savedUser.getEmail(), foundUser.getEmail());
    }

    @Test
    void testFindByEmailNotFound() {
        // act
        ApplicationUser foundUser = userRepository.findByEmail("Not Found").orElse(null);

        // assert
        Assertions.assertNull(foundUser);
    }
}