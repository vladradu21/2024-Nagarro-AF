package com.nagarro.af24.cinema.repository;

import org.testcontainers.containers.MySQLContainer;

public abstract class AbstractMySQLContainer {
    static final MySQLContainer<?> MY_SQL_CONTAINER;

    static {
        MY_SQL_CONTAINER = new MySQLContainer<>("mysql:latest");
        MY_SQL_CONTAINER.start();
    }
}