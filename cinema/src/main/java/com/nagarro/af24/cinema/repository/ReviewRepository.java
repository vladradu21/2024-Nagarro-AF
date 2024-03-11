package com.nagarro.af24.cinema.repository;

import com.nagarro.af24.cinema.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByMovieTitleAndMovieYear(String movieTitle, int movieYear);
}