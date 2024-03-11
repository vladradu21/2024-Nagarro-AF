CREATE TABLE genres (
    genre_id BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    type VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE countries (
    country_id BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    name VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE movies (
    id BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    title VARCHAR(255) NOT NULL,
    year INT CHECK ( year > 1900 ),
    score DECIMAL(3, 1) CHECK ( score >= 0 AND score <= 10 ),
    UNIQUE (title, year)
);

CREATE TABLE actors (
    id BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    name VARCHAR(255) NOT NULL,
    age INT CHECK ( age > 0 ),
    gender ENUM('MALE', 'FEMALE', 'OTHER') NOT NULL,
    country_id BIGINT,
    UNIQUE (name, age, country_id),
    CONSTRAINT fk_country FOREIGN KEY (country_id) REFERENCES countries (country_id)
);

CREATE TABLE movie_actor (
    movie_id BIGINT,
    actor_id BIGINT,
    PRIMARY KEY (movie_id, actor_id),
    FOREIGN KEY (movie_id) REFERENCES movies (id),
    FOREIGN KEY (actor_id) REFERENCES actors (id)
);

CREATE TABLE movie_genre (
    movie_id BIGINT,
    genre_id BIGINT,
    PRIMARY KEY (movie_id, genre_id),
    FOREIGN KEY (movie_id) REFERENCES movies (id),
    FOREIGN KEY (genre_id) REFERENCES genres (genre_id)
);