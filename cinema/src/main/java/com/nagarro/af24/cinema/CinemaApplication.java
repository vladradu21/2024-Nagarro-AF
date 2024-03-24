package com.nagarro.af24.cinema;

import com.nagarro.af24.cinema.model.ApplicationUser;
import com.nagarro.af24.cinema.model.Role;
import com.nagarro.af24.cinema.repository.RoleRepository;
import com.nagarro.af24.cinema.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class CinemaApplication {

    public static void main(String[] args) {
        SpringApplication.run(CinemaApplication.class, args);
    }

    @Bean
    CommandLineRunner run(RoleRepository roleRepository,
                          UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByUsername("admin").isPresent()) {
                return;
            }
            Set<Role> allRoles = new HashSet<>(roleRepository.findAll());
            ApplicationUser admin = new ApplicationUser("admin", passwordEncoder.encode("parola123"), allRoles);
            userRepository.save(admin);
        };
    }
}