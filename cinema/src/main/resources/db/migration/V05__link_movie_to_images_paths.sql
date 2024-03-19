CREATE TABLE movie_images_paths (
    movie_id BIGINT NOT NULL,
    images_paths VARCHAR(255),
    FOREIGN KEY (movie_id) REFERENCES movies(id)
);