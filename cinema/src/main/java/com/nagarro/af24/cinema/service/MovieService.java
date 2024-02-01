package com.nagarro.af24.cinema.service;

import com.nagarro.af24.cinema.dto.MovieDTO;
import org.springframework.stereotype.Service;

@Service
public interface MovieService {

    MovieDTO addMovie(MovieDTO movieDTO);

    MovieDTO getMovie(String title);

    void deleteMovie(String title);
}