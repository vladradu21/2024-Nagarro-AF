package com.nagarro.af24.cinema.repository;

import com.nagarro.af24.cinema.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class RoleRepositoryIntegrationTest extends BaseRepositoryIntegrationTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void testFindByAuthority() {
        // act
        Role foundRole = roleRepository.findByAuthority("USER").orElse(null);

        // assert
        Assertions.assertNotNull(foundRole);
        Assertions.assertEquals("USER", foundRole.getAuthority());
    }

    @Test
    void testFindByAuthorityNotFound() {
        // act
        Role foundRole = roleRepository.findByAuthority("Not Found").orElse(null);

        // assert
        Assertions.assertNull(foundRole);
    }
}