package com.nagarro.af24.cinema.repository;

import com.nagarro.af24.cinema.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<Country, Integer>{
    Optional<Country> findByName(String name);
}