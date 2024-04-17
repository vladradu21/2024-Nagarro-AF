package com.nagarro.af24.cinema.repository;

import com.nagarro.af24.cinema.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {
    Optional<Movie> findByTitleAndYear(String title, int year);

    @Query("SELECT m FROM Movie m JOIN FETCH m.reviews")
    List<Movie> findAllWithReviews();
}