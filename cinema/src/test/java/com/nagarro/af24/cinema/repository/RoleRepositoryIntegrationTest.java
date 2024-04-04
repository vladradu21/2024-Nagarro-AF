package com.nagarro.af24.cinema.repository;

import com.nagarro.af24.cinema.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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

    @Test
    void testFindAllByAuthorityIn() {
        // act
        List<Role> foundRoles = roleRepository.findAllByAuthorityIn(List.of("USER", "ADMIN"));

        // assert
        Assertions.assertNotNull(foundRoles);
        Assertions.assertEquals(List.of("ADMIN", "USER"), foundRoles.stream().map(Role::getAuthority).toList());
    }

    @Test
    void testFindAllByAuthorityInNotFound() {
        // act
        List<Role> foundRoles = roleRepository.findAllByAuthorityIn(List.of("Not Found"));

        // assert
        Assertions.assertNotNull(foundRoles);
        Assertions.assertEquals(0, foundRoles.size());
    }
}