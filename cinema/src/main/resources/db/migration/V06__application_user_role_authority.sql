CREATE TABLE users (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    firstname VARCHAR(50),
    lastname VARCHAR(50),
    email VARCHAR(50) UNIQUE,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE roles (
    role_id BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    authority VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE user_role_junction (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (role_id) REFERENCES roles(role_id)
);

INSERT INTO roles (authority) VALUES ('ADMIN'), ('USER');