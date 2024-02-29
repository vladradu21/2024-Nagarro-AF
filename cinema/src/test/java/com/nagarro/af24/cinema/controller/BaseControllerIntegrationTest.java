package com.nagarro.af24.cinema.controller;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MySQLContainer;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("inttest-containers-flyway")
public abstract class BaseControllerIntegrationTest {
    static final MySQLContainer<?> MY_SQL_CONTAINER;

    static {
        MY_SQL_CONTAINER = new MySQLContainer<>("mysql:latest");
        MY_SQL_CONTAINER.start();
    }
}