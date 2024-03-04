CREATE TABLE reviews (
    review_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    movie_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    rating DECIMAL(2, 1) NOT NULL CHECK ( rating >= 0 AND rating <= 10 ),
    description TEXT NOT NULL,
    FOREIGN KEY (movie_id) REFERENCES movies(id)
)