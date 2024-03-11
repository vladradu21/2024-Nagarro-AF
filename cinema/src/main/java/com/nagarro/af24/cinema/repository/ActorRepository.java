package com.nagarro.af24.cinema.repository;

import com.nagarro.af24.cinema.model.Actor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Integer> {
    Optional<Actor> findByName(String name);

    List<Actor> findByNameIn(List<String> names);

    @Query("SELECT a FROM Actor a JOIN a.movies m WHERE m.title = :title AND m.year = :year")
    List<Actor> findByMovieTitleAndYear(String title, int year);
}